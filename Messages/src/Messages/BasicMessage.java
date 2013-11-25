package Messages;

import java.util.ArrayList;


public class BasicMessage implements IMessage <String>{
    private String ID;
    private ArrayList<String> content;

    public BasicMessage(String id) {
        content = new ArrayList<String>() ;
        this.ID = id;
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
