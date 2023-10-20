package com.stars.springbootinit.manager;

import com.stars.springbootinit.common.ErrorCode;
import com.stars.springbootinit.exception.BusinessException;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @author Prometheus
 * @description
 * @createDate 2023/8/31
 */
@Component
public class AiManager {


    @Resource
    private OpenAiClient openAiClient;

    /**
     *  AI 对话
     *
     * @param  message 输入的名字
     * @return String
     */
    public String doChat(String message) {
        Message msg = Message.builder().role(Message.Role.USER).content(message).build();
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(Arrays.asList(msg)).build();
        ChatCompletionResponse response = openAiClient.chatCompletion(chatCompletion);

        if (response == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Ai响应错误");
        }

        System.out.println(response.getChoices().get(0).getMessage().getContent());
        return response.getChoices().get(0).getMessage().getContent();
    }
}
