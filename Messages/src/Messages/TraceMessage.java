package Messages;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;


public class TraceMessage extends BasicMessage {

    protected String ID;
    protected String message;
    protected String topic;

    public static Timestamp getTimeStamp() {

        java.util.Date date= new java.util.Date();
        Timestamp t =new Timestamp(date.getTime());
        return t;
    }

    private String buildOutTrace(String messageID, String className)
    {

        String hostName  ;
        try
        {
            hostName = InetAddress.getLocalHost().getHostName();
        }
        catch(UnknownHostException e)
        {
            hostName ="unknown host"  ;
        }
        String ip ;
        try
        {
            ip = InetAddress.getLocalHost().getHostAddress();
        }
        catch(UnknownHostException e)
        {
            ip ="unknown ip"  ;
        }

        StringBuilder o = new StringBuilder()   ;


        o.append(messageID) ;
        o.append(":")  ;
        o.append(hostName) ;
        o.append(":")  ;
        o.append(ip) ;
        o.append(":")  ;
        o.append(className) ;
        o.append(":")  ;
        o.append(getTimeStamp().toString());

        return o.toString()  ;
    }

    public TraceMessage(String messageID, String className) {

        this.ID = createUniqueID();
        this.topic = "TRACE";
        this.message =buildOutTrace(messageID,className)  ;

    }

    @Override
    public String getTopic()
    {
        return topic;
    }

    @Override
    public String getMessageID() {
        return ID;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void addMessage(String c) {
         //do nothing in trace message
    }

}
