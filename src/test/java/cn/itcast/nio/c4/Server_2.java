package cn.itcast.nio.c4;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static cn.itcast.nio.c2.ByteBufferUtil.debugRead;

@Slf4j
public class Server_2 {

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
            selector.select();

            // 4. 处理事件 sscKeys包含所有发生的事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                log.info("registed key----{}", key);
                ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                SocketChannel sc = channel.accept();
                log.info("connected------{}", sc);
            }
        }
    }
}
