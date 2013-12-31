package Messages;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.util.Properties;

public class MessageFactory {

   private static String TraceTopic = null;
   private static Logger logger = null ;

    private String getTraceTopic()
    {
        try
        {
        Properties defaultProps = new Properties();
        FileInputStream in = new FileInputStream("app.properties")      ;
        defaultProps.load(in);
        in.close();
        return defaultProps.getProperty("TRACE_TOPIC","TRACE");
        }
        catch (Exception E)
        {
            logger.error(E.getMessage().toString());
            //default to something reasonable
            return "TRACE" ;
        }
    }

   public MessageFactory()
   {
       if (logger == null)
            logger = LogManager.getLogger(MessageFactory.class.getName());
       if (TraceTopic == null)
            TraceTopic = getTraceTopic() ;
   }

   public  IMessage createNewMessage(String topic)
   {
       return new BasicMessage(topic)   ;
   }

    public  IMessage createTraceMessage(String messageID,String className)
    {
        return new TraceMessage(messageID,className)   ;
    }
}
