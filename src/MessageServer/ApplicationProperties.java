package MessageServer;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;


public class ApplicationProperties {

    public Properties getProperties() throws Exception
    {
        Properties defaultProps = new Properties();
        //InputStream in = this.getClass().getResourceAsStream( "app.properties" );
        FileInputStream in = new FileInputStream("app.properties")      ;
        defaultProps.load(in);
        in.close();
        return defaultProps;


    }
}
