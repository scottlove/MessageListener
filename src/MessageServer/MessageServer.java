package MessageServer;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class MessageServer {



    private int port;
    private Logger logger ;


    public MessageServer(int port, Logger logger) {

        this.port = port;
        this.logger = logger;

    }



    public void run() throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)

        EventLoopGroup workerGroup = new NioEventLoopGroup();


        try {

            ServerBootstrap b = new ServerBootstrap(); // (2)

            b.group(bossGroup, workerGroup)

                    .channel(NioServerSocketChannel.class) // (3)

                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4)

                        @Override

                        public void initChannel(SocketChannel ch) throws Exception {


                            ch.pipeline().addLast( new HttpServerCodec());
                            ch.pipeline().addLast( new HttpObjectAggregator(512*1024));

                            ch.pipeline().addLast(new MessageHandler(logger));

                        }

                    })

                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)

                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)



            // Bind and start to accept incoming connections.

            ChannelFuture f = b.bind(port).sync(); // (7)



            // Wait until the server socket is closed.

            // In this example, this does not happen, but you can do that to gracefully

            // shut down your server.

            f.channel().closeFuture().sync();

        } finally {

            workerGroup.shutdownGracefully();

            bossGroup.shutdownGracefully();

        }

    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }

        Logger logger= LogManager.getLogger() ;
        new MessageServer(port,logger).run();
    }

}