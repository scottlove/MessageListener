package Producer;

import Messages.IMessage;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;



public class producerUtils {


    public static String buildOutString(IMessage m)
    {
        StringBuilder o = new StringBuilder()   ;


        o.append(m.getMessageID()) ;
        o.append(":")  ;
        o.append(m.getMessage())   ;

        return o.toString()  ;
    }

}
