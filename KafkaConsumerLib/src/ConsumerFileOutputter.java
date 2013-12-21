

import java.io.FileOutputStream;


public class ConsumerFileOutputter implements IOutputter {
    String fileName;

    public ConsumerFileOutputter(String fileName)
    {
        this.fileName = fileName;
    }

    public synchronized  void writeString(String data)
    {
        try
        {

        String par = "<p>"  ;
        FileOutputStream f0 = new FileOutputStream(fileName,true)    ;
        f0.write(par.getBytes());
        f0.write(data.getBytes());
        f0.write(par.getBytes());
        f0.write(System.getProperty("line.separator").getBytes());
        f0.flush();
        f0.close();
        f0=null;
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage())   ;
        }

    }


}
