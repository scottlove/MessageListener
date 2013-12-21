import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpServerCodec;
import org.apache.logging.log4j.core.Logger;

public class MessageServer {

    private int port;
    private static Logger logger ;
    String brokerList;


    public MessageServer(int port, String brokerList) {

        this.port = port;
        this.brokerList = brokerList;

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
                            ch.pipeline().addLast(new HttpRequestDecoder());
                            ch.pipeline().addLast( new HttpObjectAggregator(512*1024));


                            ch.pipeline().addLast(new MessageHandler(brokerList));

                        }

                    })

                    .option(ChannelOption.SO_BACKLOG, 128)          // (5)

                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)



            // Bind and start to accept incoming connections.

            ChannelFuture f = b.bind(port).sync(); // (7)




            f.channel().closeFuture().sync();

        } finally {

            workerGroup.shutdownGracefully();

            bossGroup.shutdownGracefully();

        }

    }

}
