package MessageServer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;


public class MessageServerTest {


    int port;
    String host;
    Logger logger;

    @org.junit.Before
    public void setUp() throws Exception {


        ApplicationProperties ap = new ApplicationProperties()  ;
        Properties p = ap.getProperties() ;
        host = p.getProperty("nettyhost")  ;
        port =   Integer.parseInt(p.getProperty("nettyport") ) ;

        logger= LogManager.getLogger() ;



    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @org.junit.Test
    public void testRun() throws Exception {


        PostMessageSender msg = new PostMessageSender(port,host,logger)   ;
        msg.run();
    }
}
