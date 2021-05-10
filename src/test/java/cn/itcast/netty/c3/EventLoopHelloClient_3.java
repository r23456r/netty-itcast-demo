package cn.itcast.netty.c3;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class EventLoopHelloClient_3 {
    public static void main(String[] args) throws InterruptedException {
        ChannelFuture future = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                // 异步非阻塞，main发起，真正执行Connect的是nio线程
                .connect(new InetSocketAddress("localhost", 8080));
        //connect未结束的两种解决方案
        //1.同步阻塞当前线程，直到nioConnect连接完毕
            /*
            future.sync();
            Channel channel = future.channel();
            System.out.println(channel);
            channel.writeAndFlush("hello, world");
            */
        //2. addListener(回调对象)异步处理结果
        future.addListener((ChannelFutureListener) channelFuture -> {
            Channel channel1 = future.channel();
            System.out.println(channel1);
            channel1.writeAndFlush("hello, world");
        });
    }
}
