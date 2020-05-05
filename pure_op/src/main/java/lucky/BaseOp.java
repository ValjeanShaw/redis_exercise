package lucky;

import redis.clients.jedis.Jedis;

/**
 * @author xiaoran
 * @date 2020/05/05
 */
public class BaseOp {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1",6379);

        System.out.println(jedis.ping());

        jedis.flushAll();

        //String
        jedis.set("k1","v1");
        System.out.println(jedis.get("k1"));

        //list
        jedis.lpush("list_k1","list_v1");
        System.out.println(jedis.lpop("list_k1"));

        //map
        jedis.hset("map_name_1","k1","v1");
        jedis.hset("map_name_1","k2","v2");
        jedis.hset("map_name_1","k3","v3");
        System.out.println(jedis.hget("map_name_1","k1"));
        System.out.println(jedis.hget("map_name_1","k2"));
        System.out.println(jedis.hget("map_name_1","k3"));

        //set
        jedis.sadd("set_k1","v1","v2","v1","v2");
        System.out.println(jedis.smembers("set_k1"));

        //sorted set
        jedis.zadd("sset_k1",1,"v1");
        jedis.zadd("sset_k1",2,"v2");
        jedis.zadd("sset_k1",3,"v3");
        System.out.println(jedis.zrange("sset_k1",0,10));

    }
}
