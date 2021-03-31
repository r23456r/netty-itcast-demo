package cn.itcast.nio.c2;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author: zhangyuhang
 * @modified By：
 * @date ：Created in 2021/3/31 11:02
 **/
@Slf4j
public class TestByteBuffer_1 {
    public static void main(String[] args) {
        try (FileChannel channel = new FileInputStream("data.txt").getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            //从Channel读，到buffer写
            while (true) {
                int i = channel.read(buffer);
                log.info("读取到的字节" + i);
                if (i == -1) {
                    break;
                }
                //切换读模式
                buffer.flip();
                while (buffer.hasRemaining()) { //是否有剩余未读数据
                    //无参，就是一个字节
                    byte b = buffer.get();
                    log.info("buff读的字节" + (char) (b));
//                    System.out.println((char) b);
                }
                // 切换到写模式，实测否则一直循环读
                buffer.clear();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
