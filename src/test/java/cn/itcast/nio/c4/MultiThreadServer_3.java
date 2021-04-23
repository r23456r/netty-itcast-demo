package cn.itcast.nio.c4;

import lombok.extern.slf4j.Slf4j;
import org.omg.SendingContext.RunTime;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static cn.itcast.nio.c2.ByteBufferUtil.debugAll;

@Slf4j
public class MultiThreadServer_3 {
    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("boss");

        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        Selector boss = Selector.open();
        SelectionKey bossKey = ssc.register(boss, 0, null);
        bossKey.interestOps(SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress("localhost", 8080));
        //创建固定数量worker
        Worker[] workers = new Worker[Runtime.getRuntime().availableProcessors()];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker("worker-" + i);
        }
        AtomicInteger atomicInt = new AtomicInteger();
        while (true) {
            boss.select();
            Iterator<SelectionKey> iter = boss.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                if (key.isAcceptable()) {
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    log.info(String.format("connected...{%s}", sc.getRemoteAddress()));
                    // round robin 轮询
                    workers[atomicInt.getAndIncrement() % workers.length].register(sc); //worker-0 线程
                    log.info("after register" + sc.getRemoteAddress());
                }
            }
        }
    }

    static class Worker implements Runnable {
        private Thread thread;
        private Selector selector;
        // 区分worker
        private String name;
        //两个线程之间传输数据，可以用队列实现
        private ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue();
        //默认未初始化，目的为了保证一个worker只拥有一个线程
        private volatile boolean start = false;

        public Worker(String name) {
            this.name = name;
        }

        // 初始化线程和selector
        public void register(SocketChannel sc) throws IOException {
            if (!start) {
                //线程要执行的方法从this里去找，也就是当前worker的run
                selector = Selector.open();
                thread = new Thread(this, name);
                thread.start();
                start = true;
            }
            // 向队列添加任务，但任务并未执行
            queue.add(() -> {
                try {
                    sc.register(selector, SelectionKey.OP_READ, null); // 如果不用queue，这段代码还是实际还是boss线程执行的
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
                // 每次都要注册
            });
            selector.wakeup(); //唤醒 84 行 select（）
        }

        @Override
        public void run() {
            while (true) {
                try {
                    selector.select();
                    Runnable task = queue.poll();
                    if (null != task) {
                        task.run();
                    }
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isReadable()) {
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            SocketChannel sc = (SocketChannel) key.channel();
                            log.info(String.format("read...{%s}", sc.getRemoteAddress()));
                            sc.read(buffer);
                            buffer.flip();
                            debugAll(buffer);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

