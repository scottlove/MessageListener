package MessageServer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

@ChannelHandler.Sharable
public class EchoServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

        @Override
        public void channelActive(ChannelHandlerContext ctx)
        {

            try
            {
            URI uri = new URI("localhost:8080");
            String scheme = uri.getScheme() == null? "http" : uri.getScheme();
            String host = uri.getHost() == null? "localhost" : uri.getHost();
            ByteBuf b = Unpooled.copiedBuffer("testMessage", Charset.defaultCharset())  ;

            // Prepare the HTTP request.
            //HttpRequest request = new DefaultHttpRequest(
                   //HttpVersion.HTTP_1_1, HttpMethod.POST, uri.toASCIIString());


           DefaultFullHttpRequest request =    new DefaultFullHttpRequest( HttpVersion.HTTP_1_1, HttpMethod.POST, uri.toASCIIString(),b);


            ctx.writeAndFlush(request)  ;
            //ctx.writeAndFlush(b) ;
            } catch (URISyntaxException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) {
            ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                    .addListener(ChannelFutureListener.CLOSE);
        }

        @Override
        public void channelRead0(ChannelHandlerContext ctx,  ByteBuf in) {
            System.out.println("Channel Read 0: ");
            ctx.close();
        }
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx,  Throwable cause) {
            System.out.println("Server exception: ");
            ctx.close();
        }
}

