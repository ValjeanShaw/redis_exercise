package com.lucky.redisboot;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

@Slf4j
@SpringBootTest
class RedisBootApplicationTests {

    @Autowired
    RedisTemplate redisTemplate;


    @Test
    void testConnect(){
        log.info("redis client:{}",redisTemplate.getClientList());
    }

    @Test
    void testString(){
        ValueOperations valueOperations= redisTemplate.opsForValue();
        valueOperations.set("hello","world");

        log.info("get String value : {}",valueOperations.get("hello"));
    }

    @Test
    void testList(){
        ListOperations listOperations = redisTemplate.opsForList();
        listOperations.leftPush("queue","james");
        listOperations.leftPush("queue","kobe");

        log.info("left push data: {}",listOperations.leftPop("queue"));
        log.info("left push data: {}",listOperations.leftPop("queue"));

        listOperations.leftPush("queue","james");
        listOperations.leftPush("queue","kobe");

        log.info("right push data: {}",listOperations.rightPop("queue"));
        log.info("right push data: {}",listOperations.rightPop("queue"));
    }

    @Test
    void testHash(){
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put("nba","james","lakers");
        hashOperations.put("nba","letter bro","bucks");

        log.info("the team is {}",hashOperations.get("nba","james"));
        log.info("the team is {}",hashOperations.get("nba","letter bro"));
    }

    @Test
    void testSet(){
        SetOperations setOperations = redisTemplate.opsForSet();
        setOperations.add("collect",1,3,6,3,2,3,3,12,4,3);

        log.info("the set is {}",setOperations.members("collect"));
    }

    @Test
    void testSortedSet(){
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add("sortSet","v1",1);
        zSetOperations.add("sortSet","v2",2);
        zSetOperations.add("sortSet","v3",3);
        zSetOperations.add("sortSet","v4",4);

        log.info("the score in sortedset has :{}",zSetOperations.rangeByScore("sortSet",2,3));
    }
}
