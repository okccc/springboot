package com.okccc.controller;

import com.okccc.bean.User;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: okccc
 * @Date: 2024/3/2 11:45:58
 * @Desc: 整合kafka
 */
@RestController
@RequestMapping(value = "kafka")
public class KafkaController {

    // 使用KafkaTemplate组件发送消息
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    // http://localhost:8080/kafka/producer
    @GetMapping(value = "producer")
    public void testProducer() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 模拟数据
        User user = new User(1, "aaa", "bbb", 19, "1", "aaa@qq.com");
        for (int i = 0; i < 10000; i++) {
            // 发送String类型消息,默认valueSerializer=StringSerializer
            kafkaTemplate.send("boot-kafka", "hello kafka " + i);
            // 发送JavaBean对象,需设置valueSerializer=JsonSerializer
            kafkaTemplate.send("boot-kafka", user);
        }
        stopWatch.stop();
        long millis = stopWatch.getTotalTimeMillis();
        System.out.println("发送10000条消息耗时(ms)：" + millis);
    }

    // 使用KafkaListener监听消息
    @KafkaListener(topics = "boot-kafka", groupId = "g01")
//    @KafkaListener(
//            groupId = "g01",  // 消费者组
//            topicPartitions = {
//                    @TopicPartition(
//                            topic = "boot-kafka",  // 主题
//                            partitionOffsets = {@PartitionOffset(partition = "0", initialOffset = "0")}  // 分区和起始偏移量
//                    )
//            }
//    )
    public void testConsumer(ConsumerRecord<String, Object> consumerRecord) {
        // 获取消息详细信息
        long timestamp = consumerRecord.timestamp();
        String topic = consumerRecord.topic();
        int partition = consumerRecord.partition();
        long offset = consumerRecord.offset();
        Object value = consumerRecord.value();
        System.out.println(timestamp + " - " + topic + " - " + partition + " - " + offset + " - " + value);
    }

}
