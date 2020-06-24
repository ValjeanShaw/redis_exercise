package lucky.pubsub;

import redis.clients.jedis.JedisPubSub;

/**
 * @author xiaoran
 * @date 2020/05/15
 */
class JedisPubSubListener extends JedisPubSub {

    // 取得订阅的消息后的处理
    @Override
    public void onMessage(String channel, String message) {
        System.out.println("channel:" + channel + " receives message :" + message);
        // this.unsubscribe();
    }
    // 取得按表达式的方式订阅的消息后的处理
    @Override
    public void onPMessage(String pattern, String channel, String message) {
    }
    // 初始化订阅时候的处理
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        System.out.println("channel:" + channel + " is been subscribed:" + subscribedChannels);
    }
    // 取消按表达式的方式订阅时候的处理
    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
    }
    // 初始化按表达式的方式订阅时候的处理
    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
    }
    // 取消订阅时候的处理
    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        System.out.println("channel:" + channel + " is been unsubscribed:" + subscribedChannels);
    }

}
