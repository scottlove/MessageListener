package KafkaConsumerLib;

import kafka.javaapi.consumer.ConsumerConnector;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ConsumerGroup {
    private final ConsumerConnector consumer;
    private final String topic;
    private  ExecutorService executor;
    private List<IOutputter> outputs;

    public ConsumerGroup(String a_zookeeper, String a_groupId, String a_topic, List<IOutputter> outputters)
    {
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(createConsumerConfig(a_zookeeper, a_groupId));

        this.topic = a_topic;
        this.outputs = outputters;
    }

    public void shutdown()
    {
        if (consumer != null) consumer.shutdown();
        if (executor != null) executor.shutdown();
    }

    public void run(int a_numThreads)
    {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(a_numThreads));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);

        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

        // now launch all the threads
        executor = Executors.newFixedThreadPool(a_numThreads);
        // now create an object to consume the messages
        int threadNumber = 0;
        for (final KafkaStream stream : streams)
        {

            Consumer c = new Consumer(stream, threadNumber,outputs)  ;
            executor.submit(c);

            threadNumber++;
    }
}

    private static ConsumerConfig createConsumerConfig(String a_zookeeper, String a_groupId)
    {
        Properties props = new Properties();
        props.put("zookeeper.connect", a_zookeeper);
        props.put("group.id", a_groupId);
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");

        return new ConsumerConfig(props);
    }
}
