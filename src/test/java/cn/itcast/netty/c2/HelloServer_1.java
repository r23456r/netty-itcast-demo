package cn.itcast.netty.c2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LoggingHandler;

public class HelloServer_1 {
    public static void main(String[] args) {
        // 1. 服务器端的启动器，负责组装Netty组件
        new ServerBootstrap()
                //2. 添加组的概念 boss, worker(selector，thread)
                .group(new NioEventLoopGroup())
                // 3 用Nio的服务器ServerSocketChannel实现
                .channel(NioServerSocketChannel.class)
                //4.规定了worker的读写操作
                .childHandler(
                        //和客户端读写的通道，负责增加添加别的handler
                        new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel(NioSocketChannel ch) throws Exception {
                                //ByteBuf的字节流转String
                                ch.pipeline().addLast(new StringDecoder());
                                //我们自己的业务实现，自定义Handler
                                ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                    //只关注读事件
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        System.out.println(msg);
                                    }
                                });

                            }
                        }).bind(8080);

    }
}
