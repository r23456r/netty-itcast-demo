package cn.itcast.nio.c5;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static cn.itcast.nio.c2.ByteBufferUtil.debugAll;

@Slf4j
public class AioFileChannel_1 {
    public static void main(String[] args) throws IOException {
        AsynchronousFileChannel channel = AsynchronousFileChannel.open(Paths.get("data.txt"), StandardOpenOption.READ);
        try {
            // 参数1 ：byteBuffer 接收结果
            // 参数2 ：起始位置
            // 参数3 ：附件 一次读不完，接着读
            // 参数4 ：回调对象
            ByteBuffer buffer = ByteBuffer.allocate(16);
//            ByteBuffer attach = ByteBuffer.allocate(16);
            log.info("read begin");
            channel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override //success
                public void completed(Integer result, ByteBuffer attachment) {
                    log.info("read success");
                    attachment.flip();
                    debugAll(attachment);
                }

                @Override // false
                public void failed(Throwable exc, ByteBuffer attachment) {
                    exc.printStackTrace();
                }
            });
             log.info("read end");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //如果不加主线程的等待，直接39行main结束，守护线程没返回值，也得结束，就啥也没有了
        System.in.read();
    }
}
