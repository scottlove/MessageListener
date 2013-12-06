package Messages;


import java.util.List;


public interface IMessage <E>  {

    public String getTopic();
    public String getMessageID()  ;
    public String getMessage() ;
    public void addMessage(String c)   ;

}
