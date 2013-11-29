package Producer;

import Messages.BasicMessage;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;





public class producerTest {
   @Test
    public void testSend() throws Exception {
        //producer p = new producer();

        IProducer p =producerFactory.getProducer()  ;

        ArrayList<String>  testContent = new ArrayList<String>()  ;
        String testID = "testID";
        for (Integer i=0;i<10;i++)
        {
            testContent.add(i.toString())   ;
        }
        BasicMessage message = new BasicMessage("testID")  ;
        for (String s : testContent)
        {
            message.addContent(s)     ;
        }

        p.send(message);
        assertTrue(true);
    }


}

