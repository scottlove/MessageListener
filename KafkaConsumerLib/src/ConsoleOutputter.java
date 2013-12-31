import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConsoleOutputter implements IOutputter {
    @Override
    public void writeString(String data) {
       Logger logger = LogManager.getLogger(ConsoleOutputter.class.getName());
       logger.info(data.toString());

    }
}
