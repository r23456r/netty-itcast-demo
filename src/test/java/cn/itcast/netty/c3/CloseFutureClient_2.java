package cn.itcast.netty.c3;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Scanner;

@Slf4j
public class CloseFutureClient_2 {
    public static void main(String[] args) throws InterruptedException {
        ChannelFuture channelFuture = new Bootstrap().group(new NioEventLoopGroup()).channel(NioSocketChannel.class).handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) {
                ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                ch.pipeline().addLast(new StringEncoder());
            }
        }).connect(new InetSocketAddress(8080));

        Channel channel = channelFuture.sync().channel();
        log.info("----channel---{}", channel);
        new Thread(() -> {
            System.out.println("请输入");
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String s = scanner.nextLine();
                if ("q".equals(s)) {
                    channel.close();
                    break;
                }
                channel.writeAndFlush(s);
            }
        }, "Input_Thread").start();
        ChannelFuture closeFuture = channel.closeFuture();
        // 同样的，和最后waitAndFlush一样，第一种方式：sync同步
            /*
            log.info("waiting close.....");
            closeFuture.sync();
            log.info("关闭之后的操作");
            */
        // 第二种方式 回调方法(调用关闭的NIO线程执行)
        closeFuture.addListener((ChannelFutureListener) channelFuture1 -> log.info("关闭之后的操作"));
    }
}
