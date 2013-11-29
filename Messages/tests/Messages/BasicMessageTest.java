package Messages;

import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;

import static org.junit.Assert.*;

public class BasicMessageTest {
    private String testID;
    private ArrayList<String>  testContent;
    private BasicMessage message;


    @Before
    public void setUp() throws Exception {

        testContent = new ArrayList<String>()  ;
        testID = "testID";
        for (Integer i=0;i<10;i++)
        {
            testContent.add(i.toString())   ;
        }
        message = new BasicMessage("testID")  ;
        for (String s : testContent)
        {
            message.addContent(s)     ;
        }

    }

    @Test
    public void testGetMessageID() throws Exception {
                  assertTrue(testID,message.getMessageID().length()>10) ;
    }

    @Test
    public void testGetContent() throws Exception {

        ArrayList content = (ArrayList) message.getContent();

        for(int i = 0;i<testContent.size();i++)
        {
                assertEquals(testContent.get(i),content.get(i));
        }

    }


}
