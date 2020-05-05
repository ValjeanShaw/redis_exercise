package lucky;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * @author xiaoran
 * @date 2020/05/05
 */
public class RedisTrans {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1",6379);
        System.out.println(jedis.ping());
        jedis.flushAll();

        Transaction multi = jedis.multi();

        try {
            multi.set("k1","v1");
            multi.set("k2","v2");
            multi.set("k3","v3");
            multi.set("k4","v4");

            //抛出exception
            if(System.currentTimeMillis()%2 == 0){
                throw new Exception("");
            }else {
                //提交
                multi.exec();
            }
        } catch (Exception e) {
            //失败取消
            multi.discard();
            e.printStackTrace();
        } finally {

            System.out.println(jedis.get("k1"));
            System.out.println(jedis.get("k2"));
            System.out.println(jedis.get("k3"));
            System.out.println(jedis.get("k4"));
            multi.close();
        }
    }
}
