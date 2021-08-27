package cn.itcast.advance.c2;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

@Slf4j
public class TestRedis_2 {
    /*
    set name zhangsan
    *3
    $3
    set
    $4
    name
    $8
    zhangsan
     */
    public static void main(String[] args) {
        final byte[] LINE = {13, 10};
        NioEventLoopGroup worker = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(worker);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
                        .addLast(new LoggingHandler())
                        .addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                ByteBuf buf = ctx.alloc().buffer();
                                buf.writeBytes("*3".getBytes()).writeBytes(LINE)
                                        .writeBytes("$3".getBytes()).writeBytes(LINE)
                                        .writeBytes("set".getBytes()).writeBytes(LINE)
                                        .writeBytes("$4".getBytes()).writeBytes(LINE)
                                        .writeBytes("name".getBytes()).writeBytes(LINE)
                                        .writeBytes("$8".getBytes()).writeBytes(LINE)
                                        .writeBytes("zhangsan".getBytes()).writeBytes(LINE);
                                ctx.writeAndFlush(buf);
                            }

                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                System.out.println(buf.toString(Charset.defaultCharset()));
                            }
                        });
                ChannelFuture channelFuture = bootstrap.connect("192.168.4.19", 6379);
                channelFuture.channel().closeFuture().sync();
            }
        });
    }
}
