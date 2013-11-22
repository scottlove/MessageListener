package MessageServer;

import Messages.IMessage;
import Messages.IMessageSender;
import Messages.MessageFactory;
import Messages.MessageSenderFactory;
import io.netty.buffer.ByteBuf;


import io.netty.buffer.Unpooled;
import io.netty.channel.*;


import io.netty.util.ReferenceCountUtil;

import io.netty.handler.codec.http.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;



import java.nio.charset.Charset;

import org.apache.logging.log4j.Logger;



/**

 * Created with IntelliJ IDEA.

 * User: scotlov

 * Date: 11/15/13

 * Time: 11:25 AM

 * To change this template use File | Settings | File Templates.

 */

public class MessageHandler  extends ChannelInboundHandlerAdapter { // (1)

    private static MessageFactory mf = new MessageFactory() ;
    private static MessageSenderFactory msf = new MessageSenderFactory()   ;

    private Logger logger ;

    public MessageHandler(Logger logger) {
        this.logger = logger;
    }

    private IMessage parseMessage(String msg) throws IndexOutOfBoundsException
    {

             IMessage parsedMessage;
             String [] split = msg.split(":") ;

             if (split.length > 1)
             {
                 parsedMessage = mf.createNewMessage(split[0]) ;
                 for (int i = 1; i< split.length;i++) {
                    parsedMessage.addContent(split[i]);

                 }
             }
             else
             {
                 logger.error("invalid msg received");
                 throw new IndexOutOfBoundsException("invalid message") ;
             }


            return parsedMessage;
    }



    private String buildReturnMessage(String message)
    {
        StringBuilder buf = new StringBuilder();
        buf.setLength(0);
        buf.append(message)      ;
        buf.append("\r\n");
        return String.valueOf(buf);

    }

    private void sendResponse(ChannelHandlerContext ctx,HttpResponseStatus status, String message)
    {
        ByteBuf b = Unpooled.copiedBuffer(message, Charset.defaultCharset())  ;

        DefaultFullHttpResponse re = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,b)  ;

        final ChannelFuture f =  ctx.channel().writeAndFlush(re);
        f.addListener(ChannelFutureListener.CLOSE);
    }

     @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)    ByteBuf in = (ByteBuf) msg;

        try {


            if (msg instanceof DefaultFullHttpRequest  )
            //if (msg instanceof DefaultLastHttpContent  )
            {
                //DefaultLastHttpContent  in =    (DefaultLastHttpContent )msg;
                DefaultFullHttpRequest  in =    (DefaultFullHttpRequest )msg;


                IMessage payload = parseMessage(in.content().toString(Charset.defaultCharset())) ;
                IMessageSender ms =   msf.getSender() ;

                String serverMsg;
                if (ms.sendMessage(payload))
                {
                    serverMsg = buildReturnMessage("message received")  ;
                    logger.debug(payload.getContent().toString());
                }
                else
                {
                    serverMsg = buildReturnMessage("unable to forward message")  ;
                    logger.error("unable to forward message");
                    logger.error(in.content().toString(Charset.defaultCharset()));

                }

                System.out.println(in.getMethod().toString() +":" +in.content().toString(Charset.defaultCharset()));

                sendResponse(ctx,OK,serverMsg);


            }

        }
        catch (IndexOutOfBoundsException e)
        {
            String serverMsg = buildReturnMessage("invalid message")  ;
            sendResponse(ctx,BAD_REQUEST,serverMsg);
            logger.error(serverMsg);

        }
        finally {

            ReferenceCountUtil.release(msg); // (2)

        }


    }
}

