package Messages;

import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;

import static org.junit.Assert.*;

public class BasicMessageTest {
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
    public void testGetMessageID() throws Exception {
                  assertTrue(testID, message.getMessageID().length() > 10) ;
    }

    @Test
    public void testGetContent() throws Exception {


        assertEquals(message.getMessage(),testMessage);


    }


}
