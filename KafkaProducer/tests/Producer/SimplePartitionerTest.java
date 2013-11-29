package Producer;

import kafka.utils.VerifiableProperties;
import org.junit.Test;
import static org.junit.Assert.assertTrue;


public class SimplePartitionerTest {
    @Test
    public void testPartition() throws Exception {

        SimplePartitioner p = new SimplePartitioner(new VerifiableProperties())   ;

        int result = p.partition("testkey",3)   ;

        assertTrue(result == 1);
    }
}
