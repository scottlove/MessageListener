package Messages;

import java.util.ArrayList;
import java.util.UUID;


public class BasicMessage implements IMessage <String>{
    private String ID;
    private ArrayList<String> content;
    private String topic;

    private String createUniqueID  ()
    {
       return UUID.randomUUID().toString();
    }

    public BasicMessage(String topic) {
        content = new ArrayList<String>() ;
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
    public ArrayList<String> getContent() {
        return content;
    }

    @Override
    public void addContent(String c) {
        content.add(c)       ;
    }
}
