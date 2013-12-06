package Producer;

import Messages.BasicMessage;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import java.sql.Timestamp;



public class producerUtilsTest {

    private String testTopic = "testTopic";
    private String testContent = "testContent";
    private BasicMessage m;


    @Before
    public void setUp() throws Exception {



        m = new BasicMessage(testTopic)  ;
        m.addMessage(testContent);  ;
    }



    @Test
    public void testGetTimeStamp() throws Exception {
        Timestamp s = producerUtils.getTimeStamp() ;
        Thread.sleep(10);
        Timestamp s1 =  producerUtils.getTimeStamp() ;
        int d= s1.compareTo(s)   ;
       assertTrue(d ==1);
    }

    @Test
    public void testBuildOutTrace() throws Exception {
        String msg = producerUtils.buildOutTrace(m)  ;

        assertTrue(msg.contains(m.getMessageID()));

    }

    @Test
    public void testBuildOutString() throws Exception {

        String msg = producerUtils.buildOutString(m)  ;

        assertTrue(msg.contains(m.getMessageID()));
        assertTrue(msg.contains(testContent))  ;
    }
}
