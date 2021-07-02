package com.Re;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

public class ThreadPoolService {
    public static void main(String[] args) {
        ThreadPoolService service = new ThreadPoolService();
        //service.Add();
        //service.calculateByConcurrentHashMap();
        service.calculateByHashMap();
    }

    public static ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(10,10,0,
            TimeUnit.SECONDS,new LinkedBlockingDeque<>(1000));

    /**
     * 采用HashMap进行单词数量统计
     */
    public void calculateByHashMap(){
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        CountDownLatch latch=new CountDownLatch(100000);
        Long pre=System.currentTimeMillis();
        for(int i=0;i<10000;i++){
            for(int k=0;k<arr.length;k++){
                String str=arr[k];
                threadPool.execute(()->{
                    increaseMap(str,latch);
                });
            }
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadPool.shutdown();
        System.out.println(System.currentTimeMillis()-pre);
        System.out.println();
        Set<String> keySet = map.keySet();
        for(String item:keySet){
            System.out.println(item+" : "+map.get(item));
        }
    }

    /**
     * ConCurrentHashMap，统计每个单词的出现次数
     */
    public void calculateByConcurrentHashMap(){
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        CountDownLatch latch=new CountDownLatch(100000);
        Long pre=System.currentTimeMillis();
        for(int i=0;i<10000;i++){
            for(int k=0;k<arr.length;k++){
                String str=arr[k];
                threadPool.execute(()->{
                    increaseA(str);
                    latch.countDown();
                });
            }
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadPool.shutdown();
        Set<String> set = currentMap.keySet();
        System.out.println(System.currentTimeMillis()-pre);
        for(String item:set){
            System.out.println(item+": "+currentMap.get(item));
        }
    }

    public String arr[]=new String[]{"advice","before","component","date","execute","final","get","hashMap","int","join"};

    public Map<String,Long> currentMap=new ConcurrentHashMap<>();

    public Map<String,Long> map=new HashMap<>();

    public synchronized void increaseMap(String url,CountDownLatch latch){
        Long val=map.get(url);
        val=val==null?1:val+1;
        map.put(url,val);
        latch.countDown();
    }

    public  synchronized void  increase(String url){
        Long val=currentMap.get(url);
        val=val==null?1:val+1;
        currentMap.put(url,val);
    }

    /**
     * 使用ConCurrentHashMap的CAS机制，快速并且线程安全的统计每个单词出现的次数
     * @param url
     */
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

    /**
     * ConcurrentHashMap Test
     */
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
