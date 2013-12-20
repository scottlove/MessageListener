package Messages;

import java.util.ArrayList;
import java.util.UUID;


public class BasicMessage implements IMessage <String>{
    protected String ID;
    protected String message;
    protected String topic;

    protected String createUniqueID  ()
    {
       return UUID.randomUUID().toString();
    }

    protected BasicMessage()
    {}

    public BasicMessage(String topic) {
        message = "" ;
        this.ID = createUniqueID();
        this.topic = topic;
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
        message = c    ;
    }
}
