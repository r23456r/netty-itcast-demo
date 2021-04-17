package cn.itcast.nio.c4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Client_2 {
    public static void main(String[] args) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.connect(new InetSocketAddress(8080));
        channel.write(Charset.defaultCharset().encode("hel55555555555555555555555555555555555555555555555555555555555555555lo\nword\n!"));
        //等待输入，让程序先不结束
        System.in.read();
    }
}