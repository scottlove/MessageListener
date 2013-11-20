package MessageServer;

import io.netty.buffer.ByteBuf;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;


import io.netty.util.ReferenceCountUtil;

import io.netty.handler.codec.http.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;



import java.nio.charset.Charset;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



/**

 * Created with IntelliJ IDEA.

 * User: scotlov

 * Date: 11/15/13

 * Time: 11:25 AM

 * To change this template use File | Settings | File Templates.

 */

public class MessageHandler  extends ChannelInboundHandlerAdapter { // (1)

    Logger logger = LogManager.getLogger();

    @Override



    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)    ByteBuf in = (ByteBuf) msg;

        try {


            if (msg instanceof DefaultFullHttpRequest  )
            //if (msg instanceof DefaultLastHttpContent  )
            {
                //DefaultLastHttpContent  in =    (DefaultLastHttpContent )msg;
                DefaultFullHttpRequest  in =    (DefaultFullHttpRequest )msg;

                System.out.println(in.getMethod().toString() +":" +in.content().toString(Charset.defaultCharset()));

                logger.error("Did it again!");


                StringBuilder buf = new StringBuilder();
                buf.setLength(0);
                buf.append("message received:")      ;
                buf.append(in.content().toString(Charset.defaultCharset()));
                buf.append("\r\n");

                ByteBuf b = Unpooled.copiedBuffer(buf, Charset.defaultCharset())  ;
                DefaultFullHttpResponse re = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, OK,b)  ;

                final ChannelFuture f =  ctx.channel().writeAndFlush(re);
                f.addListener(ChannelFutureListener.CLOSE);

            }

        }
        finally {

            ReferenceCountUtil.release(msg); // (2)

        }


    }
}

