package lucky.pubsub;

import redis.clients.jedis.Jedis;

/**
 * @author xiaoran
 * @date 2020/05/15
 */
public class Subscriber {
    public  static void main (String []args) throws Exception{
        Jedis jedis = new Jedis("127.0.0.1",6379);
        JedisPubSubListener listener = new JedisPubSubListener();
        jedis.subscribe(listener, "msgChannel");
    }
}
