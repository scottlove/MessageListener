package MessageServer;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;


public class ApplicationProperties {

    public static Properties getProperties() throws Exception
    {
        Properties defaultProps = new Properties();
        FileInputStream in = new FileInputStream("app.properties")      ;
        defaultProps.load(in);
        in.close();
        return defaultProps;
    }
}
