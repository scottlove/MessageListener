package Messages;


public class MessageFactory {

   public static IMessage createNewMessage(String topic)
   {
       return new BasicMessage(topic)   ;
   }
}
