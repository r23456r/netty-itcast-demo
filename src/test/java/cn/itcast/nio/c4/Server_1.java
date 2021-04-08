package cn.itcast.nio.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static cn.itcast.nio.c2.ByteBufferUtil.debugRead;

@Slf4j
public class Server_1 {

    public static void main(String[] args) throws IOException {
        //单线程，nio理解阻塞模式
        ByteBuffer buffer = ByteBuffer.allocate(16);
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 设置channel的模式，阻塞和非阻塞
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(8080));
        List<SocketChannel> channels = new ArrayList<>();
        while (true) {
//            log.info("connecting----");
            SocketChannel sc = ssc.accept();//阻塞方法
            if (sc != null) {
                log.info("connected---{}", sc);
                sc.configureBlocking(false);
                channels.add(sc);
            }
            for (SocketChannel socketChannel : channels) {
//                log.info("before read...{}", socketChannel);
                int read = socketChannel.read(buffer);// read 是阻塞方法，如果没有客户端没发数据，read返回0
//buffer 写
                if (read > 0) {
                    buffer.flip();
                    debugRead(buffer);
                    buffer.clear();
                    log.info("after read...{}", socketChannel);
                }

            }
        }
    }
}
