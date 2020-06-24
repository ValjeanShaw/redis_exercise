package lucky.pubsub;

import redis.clients.jedis.Jedis;

/**
 * @author xiaoran
 * @date 2020/05/15
 */
public class Publisher{

    public static void  main(String []args) throws InterruptedException {
        Jedis jedis = new Jedis("127.0.0.1",6379);//连接redis
        System.out.println("publish message one");
        jedis.publish("msgChannel", "publish message one");//发布消息
        Thread.sleep(2000);
        System.out.println("publish message two");
        jedis.publish("msgChannel", "publish message two");//发布消息
        Thread.sleep(2000);
        System.out.println("publish message three");
        jedis.publish("msgChannel", "publish message three");//发布消息
        if(jedis!=null){
            jedis.close();//关闭连接
        }
    }
}
