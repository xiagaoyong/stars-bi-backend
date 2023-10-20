package com.stars.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.stars.springbootinit.common.ErrorCode;
import com.stars.springbootinit.exception.BusinessException;
import com.stars.springbootinit.manager.AiManager;
import com.stars.springbootinit.model.entity.Chart;
import com.stars.springbootinit.service.ChartService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;

import static com.stars.springbootinit.constant.CommonConstant.BI_MODEL_ID;
import static com.stars.springbootinit.mq.config.DelayedConfig.*;


/**
 * @author Prometheus
 * @description 消费者
 * @createDate 2023/09/18 0018
 */
@Component
@Slf4j
public class BiMessageConsumer {
    @Resource
    private AiManager aiManager;
    @Resource
    private ChartService chartService;



    /**\
     *  监听消息
     * @param message 消息
     * @param channel 信道
     * @param deliveryTag 标签
     * @param redelivery 是否重新发送
     * @throws IOException
     */
    @RabbitListener(queues = BI_DELAYED_QUEUE)
    public void receivedMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
                                @Header(AmqpHeaders.REDELIVERED) boolean redelivery) throws IOException {
        log.info("当前时间：{},收到延迟队列的消息：{}",new Date(),message);
        if (StringUtils.isBlank(message)) {
            //如果失败，消息拒绝
            channel.basicNack(deliveryTag,false,false);
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"消息为空");
        }

        long chartId = Long.parseLong(message);
        Chart chart = chartService.getById(chartId);
        if (chart == null) {
            channel.basicNack(deliveryTag,false,false);
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"图表为空");
        }

        //先修改图表任务状态为"执行中“，等执行结束后，修改为”已完成“，保存执行结果；
        //执行失败后，状态修改为‘失败’，记录任务失败信息
        Chart updateChart = new Chart();
        updateChart.setId(chart.getId());
        updateChart.setStatus("running");
        boolean updateSave = chartService.updateById(updateChart);
        if (!updateSave) {
            channel.basicNack(deliveryTag,false,false);
            handleChartUpdateError(chart.getId(), "更新图表执行中状态失败");
            return;
        }
        //调用AI
        String result = aiManager.doChat(buildUserInput(chart));
        System.out.println(result);
        String[] splits = result.split("【【【【【");
        if (splits.length != 3) {
            channel.basicNack(deliveryTag,false,false);
            handleChartUpdateError(chart.getId(), "AI 生成错误");
            return;
        }
        String genChart = splits[1].trim();
        String genResult = splits[2].trim();

        Chart updateChartResult = new Chart();
        updateChartResult.setId(chart.getId());
        updateChartResult.setStatus("succeed");
        updateChartResult.setGenChart(genChart);
        updateChartResult.setGenResult(genResult);
        boolean updateResult = chartService.updateById(updateChartResult);
        if (!updateResult) {
            channel.basicNack(deliveryTag,false,false);
            handleChartUpdateError(chart.getId(), "更新图表成功状态失败");
        }
        log.info("receiveMessage message = {}, delivery = {}, redelivery = {}",message, deliveryTag,redelivery);
        //消息确认
        channel.basicAck(deliveryTag,false);

    }

    private void handleChartUpdateError(long chartId, String execMessage) {
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chartId);
        updateChartResult.setStatus("failed");
        updateChartResult.setExecMessage("execMessage");
        boolean updateResult = chartService.updateById(updateChartResult);
        if (!updateResult) {
            log.error("更新图表失败状态失败" + chartId + "," + execMessage);
        }
    }

    /**
     *  构建用户输入
     * @param chart 图表信息
     * @return 用户输入
     */
    private String buildUserInput(Chart chart){
        String goal = chart.getGoal();
        String chartType = chart.getChartType();
        String csvData = chart.getChartData();

        //构造用户输入
        StringBuilder userInput = new StringBuilder();

        final String prompt = "你是一个数据分析师和前端开发专家，接下来我会按照一下固定格式给你提供内容：\n" +
                "分析需求：\n" +
                "{数据分析的需求和目标}\n" +
                "原始数据：\n" +
                "{csv格式的原始数据，用,作为分隔符}\n" +
                "请根据以上内容，帮我按照指定格式生成内容（此外不要输出任何多余的开头，结尾等内容）\n" +
                "【【【【【\n" +
                "{前端Echarts V5 的option 配置对象json代码，合理地将数据进行可视化吗不要生成任何\n" +
                "多余的内容}\n" +
                "【【【【【\n" +
                "{明确的数据分析结论，越详细越好，不要生成多余的注释}";
        //分析身份
        userInput.append(prompt).append("\n");
        userInput.append("分析需求").append("\n");
        String userGoal = goal;
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "， 请使用" + chartType;
        }
        userInput.append(userGoal).append("\n");

        userInput.append("原始数据：").append("\n");

        userInput.append(csvData).append("\n");
        return userInput.toString();
    }

}
