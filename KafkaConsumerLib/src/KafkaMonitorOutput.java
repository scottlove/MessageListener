

import Messages.IMessage;
import Messages.MessageFactory;
import Producer.IProducer;
import Producer.producerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class KafkaMonitorOutput implements IOutputter {

    IProducer kafkaProducer;
    private Logger logger ;
    private String brokerList;
    private static MessageFactory mf = new MessageFactory();

    public KafkaMonitorOutput(String brokerList)
    {
        logger = LogManager.getLogger(KafkaMonitorOutput.class.getName());
        this.brokerList = brokerList;
        kafkaProducer = producerFactory.getProducer(brokerList, logger)   ;

    }

    @Override
    public void writeString(String data) {


        String [] d = data.split(":") ;


        IMessage m = mf.createTraceMessage("1234","test" )  ;

        kafkaProducer.send(m);
    }
}
