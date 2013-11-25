package Messages;


public class MessageFactory {

   public IMessage createNewMessage(String id)
   {
       return new BasicMessage(id)   ;
   }
}
