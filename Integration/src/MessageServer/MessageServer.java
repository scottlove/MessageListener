package MessageServer;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;



public class MessageServer {



    private final int port;
    private final String host;
    private Logger logger ;


    public MessageServer(int port,String host,Logger logger) {

        this.host = host;
        this.port = port;
        this.logger = logger;

    }




//    public void run() throws Exception {
//        EventLoopGroup group = new NioEventLoopGroup();
//        try {
//
//            ServerBootstrap b = new ServerBootstrap();
//            b.group(group)
//                    .channel(NioServerSocketChannel.class)
//                    .localAddress(new InetSocketAddress(port))
//                    .childHandler(new ChannelInitializer<SocketChannel>() {
//                        @Override
//                        public void initChannel(SocketChannel ch)     throws Exception {
//                            ch.pipeline().addLast(
//                                    new EchoServerHandler());
//                        }
//                    });
//            ChannelFuture f = b.bind().sync();
//            System.out.println(MessageServer.class.getName() +  "started and listen on “"+ f.channel().localAddress());
//            f.channel().closeFuture().sync();
//        }
//
//        finally {
//            group.shutdownGracefully().sync();
//        }
//
//
//    }

    public void run() throws Exception
    {

        EventLoopGroup group = new NioEventLoopGroup();
        try {



        Bootstrap b = new Bootstrap();
        //int port =8080;
        //String host ="127.0.0.1";
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



                URI uri = new URI("localhost:8080");
                String scheme = uri.getScheme() == null? "http" : uri.getScheme();
                String host = uri.getHost() == null? "localhost" : uri.getHost();
                ByteBuf buf = Unpooled.copiedBuffer("topic1:test", Charset.defaultCharset())  ;

            DefaultFullHttpRequest request =    new DefaultFullHttpRequest( HttpVersion.HTTP_1_1, HttpMethod.POST, uri.toASCIIString(),buf);
            //HttpPostRequestEncoder p = new HttpPostRequestEncoder(request,false) ;
            //HttpRequest h = p.finalizeRequest() ;
            ByteBuf bb = request.content()     ;






                f.channel().writeAndFlush(request)    ;


        System.out.println(MessageServer.class.getName() +  "started and listen on “"+ f.channel().localAddress());
        f.channel().closeFuture().sync();
     }
    finally {
        group.shutdownGracefully().sync();
      }


    }






}