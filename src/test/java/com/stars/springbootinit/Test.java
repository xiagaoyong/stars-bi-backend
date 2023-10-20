package com.stars.springbootinit;

import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
@SpringBootTest
public class Test {
    public static void main(String[] args) {
        OpenAiClient  openAiClient = OpenAiClient.builder()
                .apiKey(Arrays.asList("sk-xxx"))
                //自定义key的获取策略：默认KeyRandomStrategy
                //.keyStrategy(new KeyRandomStrategy())
//                .keyStrategy(new FirstKeyStrategy())
                //自己做了代理就传代理地址，没有可不不传
                .apiHost("https://gpt-test.wake7720.top/")
                .build();
        //聊天模型：gpt-3.5
        Message message = Message.builder().role(Message.Role.USER).content("如何学习C++").build();
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(Arrays.asList(message)).build();
        ChatCompletionResponse chatCompletionResponse = openAiClient.chatCompletion(chatCompletion);
        chatCompletionResponse.getChoices().forEach(e -> {
            System.out.println(e.getMessage());
        });
    }
}