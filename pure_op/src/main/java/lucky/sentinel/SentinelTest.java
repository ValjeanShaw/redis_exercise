package lucky.sentinel;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

/**
 * @author xiaoran
 * @date 2020/05/17
 */
public class SentinelTest {

    public static void main(String[] args) {
        Set<String> nodeSet = new HashSet<>();
        nodeSet.add("172.23.7.13:26379");
        nodeSet.add("172.23.7.14:26379");
        nodeSet.add("172.23.7.16:26379");
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //哨兵配置中的服务器名称为 mymaster  哨兵ip和端口    config
        JedisSentinelPool jedisSentinelPool = new JedisSentinelPool("mymaster",nodeSet,jedisPoolConfig);

        Jedis jedis = jedisSentinelPool.getResource();
        jedis.flushAll();

        //String
        jedis.set("k1","v1");
        System.out.println(jedis.get("k1"));

    }
}
