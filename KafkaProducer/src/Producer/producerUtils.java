package Producer;

import Messages.IMessage;


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


        o.append(m.getMessageID()) ;
        o.append(":")  ;
        o.append(getTimeStamp().toString());

        return o.toString()  ;
    }

    public static String buildOutString(IMessage m)
    {
        StringBuilder o = new StringBuilder()   ;
        ArrayList content = (ArrayList) m.getContent();

        o.append(m.getMessageID()) ;
        o.append(":")  ;
        for(int i = 0;i<content.size();i++)
        {
            o.append(content.get(i))  ;
            o.append(":")  ;
        }

        return o.toString()  ;
    }

}
