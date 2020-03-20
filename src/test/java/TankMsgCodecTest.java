import com.maureen.netty.s12.TankMsg;
import com.maureen.netty.s12.TankMsgDecoder;
import com.maureen.netty.s12.TankMsgEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

public class TankMsgCodecTest {
    @Test
    public void testTankMsgEncoder() {
        TankMsg msg = new TankMsg(10, 10);
        EmbeddedChannel ch = new EmbeddedChannel(new TankMsgEncoder());
        ch.writeOutbound(msg); //往外写

        ByteBuf buf = (ByteBuf) ch.readOutbound();
        int x = buf.readInt();
        int y = buf.readInt();

        Assert.assertTrue(x == 10 && y == 10);
        buf.release();
    }

    @Test
    public void testTankMsgEncoder2() {
        //将一个TankMsg转换为为ByteBuf
        TankMsg msg = new TankMsg(10, 10);
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(msg.x);
        buf.writeInt(msg.y);

        EmbeddedChannel ch = new EmbeddedChannel(new TankMsgEncoder(), new TankMsgDecoder());
        ch.writeInbound(buf.duplicate());//因为通过EmbeddedChannel写入到客户端的是ByteBuf，符合Decoder的要求，所以会经过Decoder从ByteBuf转换为msg，而会忽略Encoder，因为Encoder需要的是ByteBuf

        TankMsg tm = (TankMsg) ch.readInbound();

        Assert.assertTrue(tm.x == 10 && tm.y == 10);
    }
}
