package Producer;





import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class producerFactory {

    public static IProducer  getProducer(String brokerList)  {


        Properties props = new Properties();

        props.put("metadata.broker.list",brokerList)  ;
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("partitioner.class", "Producer.SimplePartitioner");
        props.put("request.required.acks", "1");
        props.put("request.timout.ms","1000") ;

        return new producer(props)   ;
    }
}