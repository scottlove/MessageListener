package Producer;





import java.util.Properties;

public class producerFactory {

    public static IProducer  getProducer()
    {
        Properties props = new Properties();
        props.put("metadata.broker.list", "broker1:9092,broker2:9092 ");
        props.put("metadata.broker.list", "localhost:9092,localhost:9093")  ;
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("partitioner.class", "Producer.SimplePartitioner");
        props.put("request.required.acks", "1");

        return new producer(props)   ;
    }
}