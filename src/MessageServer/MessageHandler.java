package MessageServer;

import Messages.IMessage;
import Messages.MessageFactory;
import Producer.IProducer;
import Producer.producerFactory;
import io.netty.buffer.ByteBuf;


import io.netty.buffer.Unpooled;
import io.netty.channel.*;


import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.ReferenceCountUtil;

import io.netty.handler.codec.http.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;



import java.nio.charset.Charset;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;





public class MessageHandler  extends ChannelInboundHandlerAdapter { // (1)

    private static MessageFactory mf = new MessageFactory() ;
    //private static producerFactory kafkaProducerFactory = new producerFactory()   ;
    IProducer kafkaProducer;

    private Logger logger ;
    private String brokerList;

    public MessageHandler(String brokerList) {
        logger = LogManager.getLogger(MessageHandler.class.getName());
        this.brokerList = brokerList;
        kafkaProducer = producerFactory.getProducer(brokerList,logger)   ;
    }


    public IMessage parseMessage( List<InterfaceHttpData> data) throws Exception
    {

        IMessage parsedMessage;
        String topic = "";
        String message ="";
        boolean validMSG = false;

        if (data.size() == 2)
        {


            String name = data.get(0).getName();
            String value = ((Attribute)data.get(0)).getValue() ;

            if (name.equals("TOPIC") && !value.isEmpty())
            {
                topic = value ;
                name = data.get(1).getName();
                value = ((Attribute)data.get(1)).getValue() ;
                if (name.equals("MSG") &&!value.isEmpty() )
                {
                    message = value ;
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
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{ // (2)    ByteBuf in = (ByteBuf) msg;

         int u =5;
         try {

            logger.info("starting channel read")   ;
            if (msg instanceof DefaultFullHttpRequest  )
            //if (msg instanceof DefaultLastHttpContent  )
            {

                DefaultFullHttpRequest  in =    (DefaultFullHttpRequest )msg;
                HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false),in);



                List<InterfaceHttpData> data = decoder.getBodyHttpDatas() ;



                //IMessage payload = parseMessage(sb.toString()) ;
                IMessage payload = parseMessage(data) ;

                String serverMsg;
                if (kafkaProducer.send(payload))

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

                System.out.println(payload.getTopic() + ":" + payload.getMessage());

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

