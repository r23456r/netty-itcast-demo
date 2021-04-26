package cn.itcast.netty.c3;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class TestEventLoop_1 {
    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup(2); //IO事件，
//        EventLoopGroup group = new DefaultEventLoopGroup(); //普通任务和定时任务
        // 获取下一个事件循环对象
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());

        //执行普通任务 这明显是个异步操作哦~~~~
        group.next().submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("ok");
        });
        log.info("main先执行？？ 牛逼");
    }
}
