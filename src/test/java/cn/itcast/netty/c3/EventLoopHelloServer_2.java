package cn.itcast.netty.c3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

@Slf4j
public class EventLoopHelloServer_2 {
    public static void main(String[] args) {
        new ServerBootstrap()
                // boos worker bossGroup无需设置nThreads=1 ，因为NioServerSocketChannel只会绑定一个线程。
                // 细分1：boss 负责accept，worker负责IO
                .group(new NioEventLoopGroup(), new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(
                        new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel(NioSocketChannel ch) throws Exception {
                                ch.pipeline().addLast(new StringDecoder());
                                ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        System.out.println("msg: " + msg);
                                        ByteBuf buf = (ByteBuf) msg;
                                        System.out.println("Bufmsg: " + buf);
                                        System.out.println("bufToString:  " + buf.toString(Charset.defaultCharset()));
                                    }
                                });

                            }
                        }).bind(8080);

    }
}
