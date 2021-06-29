package com.Re;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolService {
    public static void main(String[] args) {
        ThreadPoolService service = new ThreadPoolService();
        service.Add();
    }

    public Map<String,Long> currentMap=new ConcurrentHashMap<>();

    public  synchronized void  increase(String url){
        Long val=currentMap.get(url);
        val=val==null?1:val+1;
        currentMap.put(url,val);
    }

    public void increaseA(String url){
        while(true){
            Long oldValue=currentMap.get(url);
            if(oldValue==null){
                if(currentMap.putIfAbsent(url,1l)==null){
                    break;
                }
            }else{
                //currentMap.put(url,oldValue+1);
                if(currentMap.replace(url,oldValue,oldValue+1)){
                    break;
                }
            }
        }
    }

    public void Add(){
        long pre = System.currentTimeMillis();
        ExecutorService service = Executors.newFixedThreadPool(10);
        final int time=10000;
        final String url="www.bilibili.com";
        CountDownLatch latch = new CountDownLatch(time);
        for(int i=0;i<time;i++){
            service.execute(new Runnable() {
                @Override
                public void run() {
                    increase(url);
                    //increaseA(url);
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        service.shutdown();
        System.out.println(currentMap.get(url));
        System.out.println(System.currentTimeMillis()-pre);
    }
}
