package Producer;

import kafka.producer.KeyedMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.Assert.assertTrue;


public class producerFactoryTest {
    @Test
    public void testGetProducer() throws Exception {

        Logger logger= LogManager.getLogger()  ;
        Properties defaultProps = new Properties();
        FileInputStream in = new FileInputStream("app.properties")      ;
        defaultProps.load(in);
        String brokers = defaultProps.getProperty("metadata.broker.list")  ;
        IProducer p = producerFactory.getProducer(brokers,logger)  ;
        KeyedMessage<String, String> data = p.buildKeyedMessage("Test", "123", "message")     ;
        assertTrue(data.key()=="123");
        assertTrue(data.message()=="message");
    }
}
