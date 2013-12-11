package Producer;

import Messages.BasicMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;


public class producerTest {
    @Ignore("Only run this when kafka is running")
    @Test
    public void testSend() throws Exception {


       Logger logger= LogManager.getLogger() ;
       Properties defaultProps = new Properties();
       InputStream in = this.getClass().getResourceAsStream("app.properties");
       defaultProps.load(in);
       String brokers = defaultProps.getProperty("metadata.broker.list")  ;
       IProducer p = producerFactory.getProducer(brokers,logger)  ;


        ArrayList<String>  testContent = new ArrayList<String>()  ;
        String testID = "testID";
        for (Integer i=0;i<10;i++)
        {
            testContent.add(i.toString())   ;
        }
        BasicMessage message = new BasicMessage("testID")  ;
        for (String s : testContent)
        {
            message.addMessage(s);     ;
        }

        p.send(message);
        assertTrue(true);
    }


}

