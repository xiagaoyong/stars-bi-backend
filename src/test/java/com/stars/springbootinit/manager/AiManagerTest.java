package com.stars.springbootinit.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


/**
 * @author Stars
 * @description
 * @createDate 2023/8/31
 */
@SpringBootTest
class AiManagerTest {

    @Resource
    private AiManager aiManager;
    @Test
    void doChat() {
        String answer = aiManager.doChat("你是一个数据分析师，请帮我分析网站用户的增长趋势\n" +
                "\n" +
                "原始数据如下：\n" +
                "\n" +
                "日期，用户数\n" +
                "\n" +
                "1号，10\n" +
                "\n" +
                "2号，20\n" +
                "\n" +
                "3号，30");
        System.out.println(answer);
    }
}