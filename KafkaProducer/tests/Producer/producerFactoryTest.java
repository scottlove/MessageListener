package Producer;

import kafka.producer.KeyedMessage;
import org.junit.Test;
import static org.junit.Assert.assertTrue;


public class producerFactoryTest {
    @Test
    public void testGetProducer() throws Exception {

         IProducer p = producerFactory.getProducer()  ;
        KeyedMessage<String, String> data = p.buildKeyedMessage("Test", "123", "message")     ;
        assertTrue(data.key()=="123");
        assertTrue(data.message()=="message");
    }
}
