package MessageServer;

import Messages.IMessage;
import Messages.MessageFactory;
import Producer.IProducer;
import Producer.producerFactory;
import io.netty.buffer.ByteBuf;


import io.netty.buffer.Unpooled;
import io.netty.channel.*;


import io.netty.util.ReferenceCountUtil;

import io.netty.handler.codec.http.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;



import java.nio.charset.Charset;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;





public class MessageHandler  extends ChannelInboundHandlerAdapter { // (1)

    private static MessageFactory mf = new MessageFactory() ;
    private static producerFactory kafkaProducerFactory = new producerFactory()   ;

    private Logger logger ;
    private String brokerList;

    public MessageHandler(String brokerList) {
        logger = LogManager.getLogger(MessageHandler.class.getName());
        this.brokerList = brokerList;
    }

    public IMessage parseMessage(String msg) throws IndexOutOfBoundsException
    {


             IMessage parsedMessage;
             String [] comp = msg.split("&") ;
             String topic = "";
             String message ="";
             boolean validMSG = false;


             if (comp.length == 2)
             {
                 String [] tp = comp[0].split("=")  ;
                 if (tp[0].equals("TOPIC") && tp.length==2)
                 {
                      topic = tp[1]  ;
                     String [] m = comp[1].split("=")  ;
                     if (m[0].equals("MSG") && m.length==2)
                     {
                         message = m[1]  ;
                         validMSG = true;
                     }
                 }
             }
             if (validMSG)
             {

                 parsedMessage = mf.createNewMessage(topic) ;
                 parsedMessage.addMessage(message);

             }
             else
             {
                 logger.warn("parseMessage: invalid msg received");
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

         int u =5;
         try {

            logger.info("starting channel read")   ;
            if (msg instanceof DefaultFullHttpRequest  )
            //if (msg instanceof DefaultLastHttpContent  )
            {

                DefaultFullHttpRequest  in =    (DefaultFullHttpRequest )msg;


                ByteBuf b = in.content()     ;

                StringBuilder  sb = new StringBuilder() ;
                if (!b.hasArray()) {

                    while(b.isReadable())
                    {
                        sb.append((char)b.readByte()) ;
                    }

                    System.out.println(sb.toString());

               }
                IMessage payload = parseMessage(sb.toString()) ;
                IProducer ms =   kafkaProducerFactory.getProducer(brokerList,logger) ;

                String serverMsg;
                if (ms.send(payload))
                {
                    serverMsg = buildReturnMessage("message received")  ;
                    logger.debug(payload.getMessage().toString());
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

