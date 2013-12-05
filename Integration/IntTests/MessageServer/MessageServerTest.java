package MessageServer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

public class MessageServerTest {
    @org.junit.Before
    public void setUp() throws Exception {

    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @org.junit.Test
    public void testRun() throws Exception {
        int port = 8080;
//        if (args.length > 0) {
//            port = Integer.parseInt(args[0]);
//        } else {
//            port = 8080;
//        }

        String host = "127.0.0.1"   ;
        Logger logger= LogManager.getLogger() ;

        MessageServer msg = new MessageServer(port,host,logger)   ;
        msg.run();
    }
}
