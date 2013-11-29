package Producer;


import Messages.IMessage;
import kafka.producer.KeyedMessage;

public interface IProducer {
    Boolean  send (IMessage m)     ;
    KeyedMessage<String, String> buildKeyedMessage(String topic,String key,String msg)     ;

}



