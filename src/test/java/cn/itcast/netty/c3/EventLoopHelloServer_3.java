package cn.itcast.netty.c3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * IO太慢，会阻塞worker线程，创建独立的group处理长时间的任务
 */
@Slf4j
public class EventLoopHelloServer_3 {
    public static void main(String[] args) {
        // 细分2： 创建一个独立的EventLoopGroup处理长时间的任务
        DefaultEventLoopGroup group = new DefaultEventLoopGroup(); //通过handler的参数，指定给专门的业务。
        new ServerBootstrap()
                // boos worker bossGroup无需设置nThreads=1 ，因为NioServerSocketChannel只会绑定一个线程。而线程池只有在有任务时才创建线程。
                // 细分1：boss 负责accept，worker负责IO
                .group(new NioEventLoopGroup(), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(
                        new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel(NioSocketChannel ch) throws Exception {
                                ch.pipeline().addLast(new StringDecoder());
                                ch.pipeline().addLast("handler1", new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        System.out.println("msg: " + msg);
                                        Thread.sleep(1000);
                                        System.out.println("睡醒了");

                                    }
                                }).addLast(group, "handler1", new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        System.out.println("msg: " + msg);
                                        Thread.sleep(1000);
                                        System.out.println("睡醒了");

                                    }
                                });

                            }
                        }).bind(8080);

    }
}
