

import Messages.IMessage;
import Messages.MessageFactory;
import Producer.IProducer;
import Producer.producerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class KafkaMonitorOutput implements IOutputter {

    IProducer kafkaProducer;
    private Logger logger ;
    private String className;

    private static MessageFactory mf = new MessageFactory();


    public KafkaMonitorOutput(String brokerList,String className)
    {
        logger = LogManager.getLogger(KafkaMonitorOutput.class.getName());
        this.className = className;

        kafkaProducer = producerFactory.getProducer(brokerList, logger)   ;

    }

    @Override
    public void writeString(String data) {

        logger.info(data.toString())   ;
        String [] d = data.split(":") ;


        IMessage m = mf.createTraceMessage(d[0],className )  ;

        kafkaProducer.send(m);
    }
}
