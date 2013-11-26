package Messages;


import java.util.List;


public interface IMessage <E>  {

    public String getMessageID()  ;
    public List<E> getContent() ;
    public void addContent(String c)   ;

}