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
public class CloseFutureClient_1 {
    public static void main(String[] args) throws InterruptedException {
        ChannelFuture channelFuture = new Bootstrap().group(new NioEventLoopGroup()).channel(NioSocketChannel.class).handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) {
                ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                ch.pipeline().addLast(new StringEncoder());
            }
        }).connect(new InetSocketAddress(8080));

        Channel channel = channelFuture.sync().channel();
        log.info("----channel---{}",channel);
        new Thread(()->{
            System.out.println("请输入");
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String s = scanner.nextLine();
                if ("q".equals(s)) {
                    channel.close();
                    log.info("第二次尝试将关闭后的代码放这里？也不对，关闭也是异步，所以执行这一行，可能还没关闭");
                    break;
                }
                channel.writeAndFlush(s);
            }
        },"Input_Thread").start();
        log.info("第一次尝试将关闭后的代码放这里，不行，因为线程内无阻塞代码---channel关闭之后的代码");
    }
}
