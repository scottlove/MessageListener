package Producer;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

public class SimplePartitioner implements Partitioner<String>
{

    public SimplePartitioner (VerifiableProperties props) {       }

    public int partition(String key, int a_numPartitions) {
        int partition = 0;
        byte  val = 0;
        int length = key.length();

        if(length >0)
            val = (byte) key.charAt(key.length()-1)     ;

        if (val > 0) {
           // partition = Integer.parseInt( key.substring(offset+1)) % a_numPartitions;
            partition = val % a_numPartitions;
        }
        return partition;
    }
}
