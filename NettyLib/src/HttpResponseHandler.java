import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.util.ReferenceCountUtil;
import org.apache.logging.log4j.Logger;

import java.nio.charset.Charset;


public class HttpResponseHandler extends ChannelInboundHandlerAdapter {


      private Logger logger;
      public HttpResponseHandler(Logger logger)
      {
             this.logger = logger;
      }

     @Override
     public void channelRead(ChannelHandlerContext ctx, Object msg) {

         try {


             if (msg instanceof DefaultFullHttpResponse)

             {

                 DefaultFullHttpResponse  in =    (DefaultFullHttpResponse )msg;


                 System.out.println(in.getStatus().toString() +": " +in.content().toString(Charset.defaultCharset()));


             }

         }
         catch (IndexOutOfBoundsException e)
         {
             logger.error(e.toString());


         }
         finally {

             ReferenceCountUtil.release(msg);

         }


     }
    }

