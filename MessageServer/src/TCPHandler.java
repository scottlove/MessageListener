import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

/**
 * Handles a server-side channel.
 */
public class TCPHandler extends ChannelInboundHandlerAdapter {

    private IOutputter outputter;
    public TCPHandler (IOutputter outputter)
    {
         this.outputter = outputter;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
   // public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        StringBuilder sb = new StringBuilder();
        try {
            while (in.isReadable()) {

                sb.append((char) in.readByte());

            }
            outputter.writeString(sb.toString());
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }






}
