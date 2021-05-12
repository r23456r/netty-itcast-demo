package cn.itcast.netty.c3;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

@Slf4j
public class TestNettyPromise_1 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        DefaultPromise<Integer> promise = new DefaultPromise<>();

    }

}
