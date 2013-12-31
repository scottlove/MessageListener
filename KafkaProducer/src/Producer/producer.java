package Producer;

import Messages.IMessage;
import kafka.producer.ProducerConfig;
import kafka.producer.KeyedMessage;
import kafka.javaapi.producer.Producer   ;


import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class producer implements IProducer{

    private ProducerConfig config  ;
    private Producer<String, String> producer   ;
    private Logger logger;

    public producer (Properties p, org.apache.logging.log4j.Logger logger)
    {
        config = new ProducerConfig(p)  ;
        producer = new Producer<String, String>(config);
        this.logger = LogManager.getLogger(producer.class.getName());
    }


    public KeyedMessage<String, String> buildKeyedMessage(String topic,String key,String msg)
    {


        KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic, key, msg);
        return data;
    }

    public Boolean send(IMessage m) {

        String msg = producerUtils.buildOutString(m)  ;
        KeyedMessage<String, String> data   = buildKeyedMessage(m.getTopic(), m.getMessageID(), msg)  ;

        logger.info(m.getTopic() + "::" + m.getMessage());
;
        try
        {
            producer.send(data);
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            return false;
        }



        return true;
    }

    public void close()
    {
        producer.close();
    }
}