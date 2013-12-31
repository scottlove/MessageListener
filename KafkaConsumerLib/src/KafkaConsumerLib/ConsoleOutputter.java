package KafkaConsumerLib;


public class ConsoleOutputter implements IOutputter {
    @Override
    public void writeString(String data) {
       System.out.println("Kafka consumer:" + data.toString())    ;
    }
}
