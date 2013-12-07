package MessageServer;

import io.netty.bootstrap.Bootstrap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;

import org.apache.logging.log4j.Logger;


import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;



public class PostMessageSender {



    private final int port;
    private final String host;
    private Logger logger ;


    public PostMessageSender(int port, String host, Logger logger) {

        this.host = host;
        this.port = port;
        this.logger = logger;

    }




    public void setContent()
    {


    }


    private static void formPost(Bootstrap bootstrap, String host, int port, URI uriSimple) throws Exception {



        // setup the factory: here using a mixed memory/disk based on size threshold
        HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);


        // Start the connection attempt
        Channel channel = bootstrap.connect(host, port).sync().channel();


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
            bodyRequestEncoder.addBodyAttribute("TOPIC", "intTest");
            bodyRequestEncoder.addBodyAttribute("MSG", "first");


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




        // send request
        channel.write(request);


        // test if request was chunked and if so, finish the write
        if (bodyRequestEncoder.isChunked()) {
            // could do either request.isChunked()
            // either do it through ChunkedWriteHandler
            channel.writeAndFlush(bodyRequestEncoder).awaitUninterruptibly();
        }  else {
            channel.flush();
        }




        // Wait for the server to close the connection.
        channel.closeFuture().sync();


    }



    public void run() throws Exception
    {

        EventLoopGroup group = new NioEventLoopGroup();
        try {



        Bootstrap b = new Bootstrap();

        b.group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(host, port))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {

                        //ch.pipeline().addLast(
                               // new EchoServerHandler());
                        ch.pipeline().addLast("codec", new HttpClientCodec());
                        ch.pipeline().addLast(new HttpObjectAggregator(512 * 1024));
                        ch.pipeline().addLast("handler", new HttpResponseHandler());
                    }
                });

        ChannelFuture f = b.connect().sync();


            String postSimple = "http://127.0.0.1:8080";
            URI uriSimple;
            try {
                uriSimple = new URI(postSimple);
            } catch (URISyntaxException e) {
                //logger.log(Level.WARNING, "Invalid URI syntax", e);
                return;
            }
            String scheme = uriSimple.getScheme() == null ? "http" : uriSimple.getScheme();
            String host = uriSimple.getHost() == null ? "localhost" : uriSimple.getHost();
            int port = uriSimple.getPort();
            if (port == -1) {
                if ("http".equalsIgnoreCase(scheme)) {
                    port = 8080;
                }
            }



            //f.channel().writeAndFlush(request)    ;
            formPost(b,"localhost",port,uriSimple);


        System.out.println(PostMessageSender.class.getName() +  "started and listen on “"+ f.channel().localAddress());
        //f.channel().closeFuture().sync();
     }
    finally {
        group.shutdownGracefully().sync();
      }


    }






}