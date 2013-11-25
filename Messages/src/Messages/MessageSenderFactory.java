package Messages;


public class MessageSenderFactory {

   public IMessageSender getSender()
   {
         return new KafkaSender();
   }
}
