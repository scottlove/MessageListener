package Producer;





import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class producerFactory {

    public static IProducer  getProducer(String brokerList)  {


        Properties props = new Properties();



        //props.put("metadata.broker.list", "localhost:9092,localhost:9093")  ;
        props.put("metadata.broker.list",brokerList)  ;
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("partitioner.class", "Producer.SimplePartitioner");
        props.put("request.required.acks", "1");

        return new producer(props)   ;
    }
}