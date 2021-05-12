package cn.itcast.netty.c3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class TestJdkFuture_1 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Integer> future = executorService.submit(() -> {
            log.info("我睡1秒");
            Thread.sleep(1000);
            log.info("我醒了");
            return 50;
        });
        log.info("等待结果....");
        Integer integer = future.get();
        log.info("返回结果{}", integer);
    }
}
