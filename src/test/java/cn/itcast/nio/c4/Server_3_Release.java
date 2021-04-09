package cn.itcast.nio.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import static cn.itcast.nio.c2.ByteBufferUtil.debugRead;

/**
 * 读取事件的处理  第一章28 空指针异常版本
 */
@Slf4j
public class Server_3_Release {

    public static void main(String[] args) throws IOException {

        // 1. 创建Selector 管理多个channel
        Selector selector = Selector.open();


        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 设置channel的模式，阻塞和非阻塞
        ssc.configureBlocking(false);

        // 2. 建立selector和channel的关系
        // selectionKey 就是事件发生后，通过它，知道事件和哪个channel的事件
        SelectionKey sscKey = ssc.register(selector, 0, null); // 0表示不关注任何事件
        log.info("registor key----{}", sscKey);
        sscKey.interestOps(SelectionKey.OP_ACCEPT);//表示修改为只关注accept事件
        ssc.bind(new InetSocketAddress(8080));
        while (true) {
            // 3.   select 方法 没事件阻塞，有事件恢复线程，该歇就歇~~~
            //       select在事件未处理时，不会阻塞。
            selector.select();

            // 4. 处理事件 sscKeys包含所有发生的事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                log.info("registed key----{}", key);
                // 5. 区分事件类型

                if (key.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    //accept方法代表对事件处理了，如果不处理，
                    SocketChannel sc = channel.accept();
                    //和Server_2 不同，因为selector都是和非阻塞模式一起用的
                    sc.configureBlocking(false);
                    //把sc也注册给selector，以达成事件驱动
                    SelectionKey scKey = sc.register(selector, 0, null);
                    scKey.interestOps(SelectionKey.OP_READ);
                    log.info("connected------{}", sc);
                } else if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel(); //拿到触发读事件的channel
                    ByteBuffer buffer = ByteBuffer.allocate(16);
                    channel.read(buffer);
                    buffer.flip();
                    debugRead(buffer);

                }
            }
        }
    }
}
