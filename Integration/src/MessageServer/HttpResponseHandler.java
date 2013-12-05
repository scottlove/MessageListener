package MessageServer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.util.ReferenceCountUtil;

import java.nio.charset.Charset;


public class HttpResponseHandler extends ChannelInboundHandlerAdapter {

   private boolean readingChunks;

     @Override
     public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)    ByteBuf in = (ByteBuf) msg;

         try {


             if (msg instanceof DefaultFullHttpResponse)
             //if (msg instanceof DefaultLastHttpContent  )
             {
                 //DefaultLastHttpContent  in =    (DefaultLastHttpContent )msg;
                 DefaultFullHttpResponse  in =    (DefaultFullHttpResponse )msg;


                 System.out.println(in.getStatus().toString() +":" +in.content().toString(Charset.defaultCharset()));



             }

         }
         catch (IndexOutOfBoundsException e)
         {
            // String serverMsg = buildReturnMessage("invalid message")  ;
;

         }
         finally {

             ReferenceCountUtil.release(msg); // (2)

         }


     }
    }

