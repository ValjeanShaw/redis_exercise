package com.lucky.redisboot.controller;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author xiaoran
 * @date 2020/07/09
 */
@RestController
public class IndexController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    private Redisson redisson;


    /**
     * 减库存   什么锁都不加的状态   单机单线程情况下不会有问题
     * @return
     */
    @RequestMapping("/deduct_stock")
    public String deductStock(){

        int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
        if(stock>0){
            int realStock = stock -1;
            stringRedisTemplate.opsForValue().set("stock",realStock+"");
            System.out.println("减库存成功，剩余："+realStock);
        }else{
            System.out.println("减库存失败，库存不足");
        }
        return "ok";

    }


    /**
     * 减库存   synchronized锁状态   单机多线程情况下不会有问题
     * @return
     */
    @RequestMapping("/deduct_stock1")
    public String deductStock1(){
        synchronized (this){
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if(stock>0){
                int realStock = stock -1;
                stringRedisTemplate.opsForValue().set("stock",realStock+"");
                System.out.println("减库存成功，剩余："+realStock);
            }else{
                System.out.println("减库存失败，库存不足");
            }
        }
        return "ok——synchronize";

    }



    /**
     * 减库存   redis setnx方式    入门级别的锁
     *
     * 使用这个方式的问题在于：超时时间设多少都不合理。多了容易降低性能，少了容易出现提前释放锁，导致获取删除key混乱
     *
     * @return
     */
    @RequestMapping("/deduct_stock2")
    public String deductStock2(){
        String lockKey = "lockKey";
        //抢占锁，上锁成功的才走下面的流程    非原子性操作
//        Boolean redisLock = stringRedisTemplate.opsForValue().setIfAbsent(lockKey,"value");
//        //设置超时时间
//        stringRedisTemplate.expire(lockKey,10, TimeUnit.SECONDS);

        //原子性操作
        Boolean redisLock = stringRedisTemplate.opsForValue().setIfAbsent(lockKey,"value",10,TimeUnit.SECONDS);

        if(!redisLock){
            return "failure";
        }
        try{
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if(stock>0){
                int realStock = stock -1;
                stringRedisTemplate.opsForValue().set("stock",realStock+"");
                System.out.println("减库存成功，剩余："+realStock);
            }else{
                System.out.println("减库存失败，库存不足");
            }
        }finally {
            //最后释放锁  即删除那个都去抢占的键
            stringRedisTemplate.delete(lockKey);
        }

        return "ok    redis setnx";

    }


    @RequestMapping("/deduct_stock3")
    public String deductStock3(){

        String lockKey = "lockKey";

        //1 获取锁
        RLock rlock = redisson.getLock(lockKey);
        try{
            //2 加锁  可以加时间,锁续命-1/3锁时间
            rlock.lock();

            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if(stock>0){
                int realStock = stock -1;
                stringRedisTemplate.opsForValue().set("stock",realStock+"");
                System.out.println("减库存成功，剩余："+realStock);
            }else{
                System.out.println("减库存失败，库存不足");
            }
        }finally {
            //最后释放锁  即删除那个都去抢占的键
            rlock.unlock();
        }

        return "ok    redis redisson";

    }

    /**
     * 补货
     * @return
     */
    @RequestMapping("/save_stock")
    public String savaStock(){
        stringRedisTemplate.opsForValue().set("stock","100");
        return "sava stock ok";
    }
}
