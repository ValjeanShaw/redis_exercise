package lucky;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;

/**
 * @author xiaoran
 * @date 2020/05/05
 */
public class TransactionDemo {
    public static void main(String[] args) {
        Jedis jedis = new Jedis();
        //setnx 做初始化
        jedis.setnx("test_key", String.valueOf(5));
        System.out.println(doubleAccount(jedis, "test_key"));
        jedis.close();
    }

    public static int doubleAccount(Jedis jedis, String userId) {
        String key = String.format("account_%s", userId);
        // 乐观锁 + 自旋
        while (true) {
            jedis.watch(key);
            int value = Integer.parseInt(jedis.get(key));
            value *= 2;
            //开始事务
            Transaction tx = jedis.multi();
            tx.set(key, String.valueOf(value));
            List<Object> res = tx.exec();
            if (res != null) {
                break;
            }
        }
        // 数据库中的结果
        return Integer.parseInt(jedis.get(key));
    }
}
