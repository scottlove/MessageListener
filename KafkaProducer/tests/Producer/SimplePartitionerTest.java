package Producer;

import kafka.utils.VerifiableProperties;
import org.junit.Test;
import static org.junit.Assert.assertTrue;


public class SimplePartitionerTest {
    @Test
    public void testPartition() throws Exception {

        SimplePartitioner p = new SimplePartitioner(new VerifiableProperties())   ;

        int result = p.partition("testkey3",4)   ;
        assertTrue(result == 3);

        result = p.partition("25b7cd8-56e7-4cec-9589-17be850a2d91",4)   ;
        assertTrue(result == 1);

    }
}
