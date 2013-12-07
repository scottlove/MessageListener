package Producer;

import Messages.IMessage;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;



public class producerUtils {

    public static Timestamp getTimeStamp() {

        java.util.Date date= new java.util.Date();
        Timestamp t =new Timestamp(date.getTime());
        return t;
    }




    public static String buildOutTrace(IMessage m)
    {
        StringBuilder o = new StringBuilder()   ;

        String module = "messageListener";

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


        o.append(module) ;
        o.append(":")  ;
        o.append(hostName) ;
        o.append(":")  ;
        o.append(ip) ;
        o.append(":")  ;
        o.append(m.getMessageID()) ;
        o.append(":")  ;
        o.append(getTimeStamp().toString());

        return o.toString()  ;
    }

    public static String buildOutString(IMessage m)
    {
        StringBuilder o = new StringBuilder()   ;


        o.append(m.getMessageID()) ;
        o.append(":")  ;
        o.append(m.getMessage())   ;

        return o.toString()  ;
    }

}
