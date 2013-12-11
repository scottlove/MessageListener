package Producer;

import Messages.IMessage;
import kafka.producer.ProducerConfig;
import kafka.producer.KeyedMessage;
import kafka.javaapi.producer.Producer   ;


import java.util.Properties;




public class producer implements IProducer{

    private ProducerConfig config  ;
    private Producer<String, String> producer   ;
    private org.apache.logging.log4j.Logger logger;

    public producer (Properties p, org.apache.logging.log4j.Logger logger)
    {
        config = new ProducerConfig(p)  ;
        producer = new Producer<String, String>(config);
        this.logger = logger;
    }

    private void sendTraceData(IMessage m)
    {

        KeyedMessage<String, String> data   = buildKeyedMessage("TRACE", m.getMessageID(), producerUtils.buildOutTrace(m))  ;


        producer.send(data);
    }


    public KeyedMessage<String, String> buildKeyedMessage(String topic,String key,String msg)
    {


        KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic, key, msg);
        return data;
    }

    public Boolean send(IMessage m) {

        String msg = producerUtils.buildOutString(m)  ;
        KeyedMessage<String, String> data   = buildKeyedMessage(m.getTopic(), m.getMessageID(), msg)  ;
        try
        {
            producer.send(data);
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            return false;
        }

        sendTraceData(m);


        producer.close();
        return true;
    }
}