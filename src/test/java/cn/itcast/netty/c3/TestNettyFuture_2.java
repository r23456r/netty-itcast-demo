package cn.itcast.netty.c3;

import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;

@Slf4j
public class TestNettyFuture_2 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        EventLoopGroup executors = new NioEventLoopGroup();
        EventLoop eventLoop = executors.next();
        //queue没实际意义，仅做回顾
        ConcurrentLinkedQueue<Callable> queue = new ConcurrentLinkedQueue();
        queue.add((Callable<Integer>) () -> {
            log.debug("执行计算");
            Thread.sleep(10000);
            return 50;
        });
        Future<Integer> future = eventLoop.submit(queue.poll());
        log.debug("listener----start");
        //getNow netty不阻塞

        //get jdk阻塞
        future.addListener(future1 -> log.debug("异步结果：{}", future1.getNow()));
        log.debug("listener---stop-");
    }
}


 
