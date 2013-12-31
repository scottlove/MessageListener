package Messages;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class TraceMessageTest {


    private String testID;
    private String testMessage;
    private BasicMessage message;

    @Before
    public void setUp() throws Exception {
        testMessage = "testMessage" ;
        testID = "testID";

        message = new BasicMessage("testID")  ;
        message.addMessage(testMessage);
    }

    @Test
    public void testTraceMessage() throws Exception {

             TraceMessage tm = new TraceMessage(message.getMessageID(),TraceMessageTest.class.getName())  ;

            assertEquals(tm.getTopic(),"TRACE");

            String[] m = tm.getMessage().split(":");

            assertEquals(m[0],message.getMessageID());
            assertEquals(m[3],TraceMessageTest.class.getName());

    }


}
