package Producer;

import Messages.BasicMessage;
import Messages.IMessage;
import Messages.MessageFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;


public class producerTest {
  // @Ignore("Only run this when kafka is running")
    @Test
    public void testTrace() throws Exception {


        Logger logger= LogManager.getLogger() ;
        Properties defaultProps = new Properties();
        InputStream in = this.getClass().getResourceAsStream("app.properties");
        defaultProps.load(in);
        String brokers = defaultProps.getProperty("metadata.broker.list")  ;
        IProducer p = producerFactory.getProducer(brokers,logger)  ;
        MessageFactory mf = new MessageFactory();


        for (Integer i=0;i<10;i++)
        {
            IMessage message = mf.createNewMessage("ptTest")  ;
            message.addMessage(i.toString());
            p.send(message);
            IMessage trace = mf.createTraceMessage(message.getMessageID(),producerTest.class.getName())  ;

            IProducer kp = producerFactory.getProducer(brokers, logger)   ;

            //p.send(trace)    ;
            kp.send(trace);

        }


;
        assertTrue(true);
    }


}

