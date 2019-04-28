package com.fengwenyi.springbootkafkademo;

import com.alibaba.fastjson.JSON;
import com.fengwenyi.springbootkafkademo.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * @author Erwin Feng
 * @since 2019-04-28 09:37
 */
@Component
public class KafkaSender {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send() {
        Message message = new Message();
        message.setId(System.currentTimeMillis());
        message.setMsg(UUID.randomUUID().toString());
        message.setTime(new Date());
        kafkaTemplate.send("abc123", JSON.toJSONString(message));
    }

}
