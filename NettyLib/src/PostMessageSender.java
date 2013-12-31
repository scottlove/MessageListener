import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.*;


public class PostMessageSender {



    private final int port;
    private final String host;
    private Logger logger ;
    Bootstrap b;
    EventLoopGroup group;


    public void close() throws Exception
    {

        group.shutdownGracefully().sync();
    }

    public PostMessageSender(int port, String host) throws Exception {

        this.host = host;
        this.port = port;
        this.logger = LogManager.getLogger(PostMessageSender.class.getName());
        this.logger = logger;
        group = new NioEventLoopGroup();
        try {


            b = new Bootstrap();

            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {


                            ch.pipeline().addLast("codec", new HttpClientCodec());
                            ch.pipeline().addLast(new HttpObjectAggregator(512 * 1024));
                            ch.pipeline().addLast("handler", new HttpResponseHandler(logger));
                        }
                    });
        }
        catch (Exception e)
        {
            logger.error(e.toString());
        }



    }







   private URI getURI (String host, int port)
   {
       StringBuilder sb = new StringBuilder();
       sb.append("http://");
       sb.append(host) ;
       sb.append(":");
       sb.append(port) ;
       String postSimple = sb.toString()  ;
       URI uriSimple;
       try {
           return new URI(postSimple);
       } catch (URISyntaxException e) {
           logger.error("invalid URI");
           return null;
       }

   }






    private HttpRequest formPost( String host, int port, String topic, String message) throws Exception {


        URI uriSimple  = getURI(host,port)     ;
        String ip ;
        try
        {
            ip = InetAddress.getLocalHost().getHostAddress();
        }
        catch(UnknownHostException e)
        {
            ip ="unknown ip"  ;
        }

        // setup the factory: here using a mixed memory/disk based on size threshold
        HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);


        // Prepare the HTTP request.
        HttpRequest request =
                new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, uriSimple.toASCIIString());


        // Use the PostBody encoder
        HttpPostRequestEncoder bodyRequestEncoder = null;
        try {
            bodyRequestEncoder = new HttpPostRequestEncoder(factory, request, false); // false not multipart
        } catch (NullPointerException e) {
            // should not be since args are not null
            e.printStackTrace();
        } catch (HttpPostRequestEncoder.ErrorDataEncoderException e) {
            // test if getMethod is a POST getMethod
            e.printStackTrace();
        }


        try {
            bodyRequestEncoder.addBodyAttribute("TOPIC", topic);
            bodyRequestEncoder.addBodyAttribute("MSG", message);





        } catch (NullPointerException e) {
            // should not be since not null args
            e.printStackTrace();
        } catch (HttpPostRequestEncoder.ErrorDataEncoderException e) {
            // if an encoding error occurs
            e.printStackTrace();
        }



        // finalize request
        try {
            request = bodyRequestEncoder.finalizeRequest();
        } catch (HttpPostRequestEncoder.ErrorDataEncoderException e) {
            // if an encoding error occurs
            e.printStackTrace();
        }


         return request;



    }



    public void sendMessage(String topic, String message) throws Exception
    {


        try {


           Channel channel = b.connect(host, port).sync().channel();


           HttpRequest request = formPost(host,port,topic,  message)   ;


           channel.write(request);
           channel.flush();

           channel.closeFuture().sync() ;



        }
        catch (Exception e)
        {
            logger.error(e.toString());
        }



    }






}