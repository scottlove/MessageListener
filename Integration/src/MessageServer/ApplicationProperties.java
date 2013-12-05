package MessageServer;

import java.io.InputStream;
import java.util.Properties;


public class ApplicationProperties {

    public Properties getProperties() throws Exception
    {
        Properties defaultProps = new Properties();
        InputStream in = this.getClass().getResourceAsStream( "app.properties" );
        defaultProps.load(in);
        in.close();
        return defaultProps;


    }
}
