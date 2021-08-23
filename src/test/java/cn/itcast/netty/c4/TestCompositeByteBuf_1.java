package cn.itcast.netty.c4;

import cn.itcast.nio.c2.TestByteBufferAllocate;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;

import static cn.itcast.netty.c4.TestByteBuf.log;

public class TestCompositeByteBuf_1 {
    public static void main(String[] args) {
        ByteBuf buf1 = ByteBufAllocator.DEFAULT.buffer();
        buf1.writeBytes(new byte[]{1, 2, 3, 4, 5});

        ByteBuf buf2 = ByteBufAllocator.DEFAULT.buffer();
        buf2.writeBytes(new byte[]{6, 7, 8, 9, 10});


//        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
//        //产生了真正的数据复制，不推荐
//        buffer.writeBytes(buf1).writeBytes(buf2);
//        log(buffer);

        //避免内存复制，但带来更复杂的维护
        CompositeByteBuf byteBuf = ByteBufAllocator.DEFAULT.compositeBuffer();
        byteBuf.addComponents(true,buf1, buf2);
        log(byteBuf);

    }
}
