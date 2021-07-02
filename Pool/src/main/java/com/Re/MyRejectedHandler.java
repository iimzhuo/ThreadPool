package com.Re;

import sun.misc.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyRejectedHandler {
    public static void main(String[] args) {

    }
}

/*ExecutorService executorService = new ThreadPoolExecutor(
                5,
                10,
                60,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(100),
                new ThreadFactoryBuilder().setNameFormat("test-%d").build(),
                (r, executor) -> {
                    if (!executor.isShutdown()) {
                        try {
                            // 主线程将会被阻塞
                            executor.getQueue().put(r);   //put方式添加，如果队列满了，那么就会阻塞主线程
                        } catch (InterruptedException e) {
                            // should not be interrupted
                        }
                    }
                });
        File file = new File("文件路径");

        try (LineIterator iterator = IOUtils.lineIterator(new FileInputStream(file), "UTF-8")) {
            while (iterator.hasNext()) {
                String line = iterator.nextLine();
                executorService.submit(() -> convertToDB(line));
            }
        }*/