package Messages;

import org.junit.Test;
import static org.junit.Assert.*;



public class MessageFactoryTest {
    @Test
    public void testCreateNewMessage() throws Exception {

        MessageFactory mf = new MessageFactory();
        IMessage m = mf.createNewMessage("testTopic") ;

        assertTrue(m.getTopic()== "testTopic");
    }
}
