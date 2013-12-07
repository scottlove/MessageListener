package Producer;

import kafka.producer.KeyedMessage;
import org.junit.Test;

import java.io.InputStream;
import java.util.Properties;

import static org.junit.Assert.assertTrue;


public class producerFactoryTest {
    @Test
    public void testGetProducer() throws Exception {


        Properties defaultProps = new Properties();
        InputStream in = this.getClass().getResourceAsStream("app.properties");
        defaultProps.load(in);
        String brokers = defaultProps.getProperty("metadata.broker.list")  ;
       IProducer p = producerFactory.getProducer(brokers)  ;
        KeyedMessage<String, String> data = p.buildKeyedMessage("Test", "123", "message")     ;
        assertTrue(data.key()=="123");
        assertTrue(data.message()=="message");
    }
}
