package Messages;


public class MessageFactory {

   public IMessage createNewMessage(String topic)
   {
       return new BasicMessage(topic)   ;
   }
}
