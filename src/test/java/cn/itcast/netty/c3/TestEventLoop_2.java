package cn.itcast.netty.c3;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 定时任务
 */

@Slf4j
public class TestEventLoop_2 {
    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup(2); //IO事件，
//        EventLoopGroup group = new DefaultEventLoopGroup(); //普通任务和定时任务
        // 获取下一个事件循环对象
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());

        //执行普通任务 这明显是个异步操作哦~~~~
        group.next().scheduleAtFixedRate(() -> {

            log.info("我被定时执行了");
        }, 1, 1, TimeUnit.SECONDS);//延迟一秒后，一秒的间隔执行
        log.info("main先执行？？ 牛逼");
    }
}
