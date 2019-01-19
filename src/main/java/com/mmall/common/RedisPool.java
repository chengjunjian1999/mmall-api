package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {
    //声明static是因为启动tomcat就要把被修饰的static的属性和方法加载出来,初始化
    private static JedisPool pool;//jedis连接池
    private static Integer maxTotal= Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20")); //最大连接数
    private static Integer maxIdle= Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","20")); //在JedisPool最大连接空闲实例个数
    private static Integer minIdle= Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle","20"));//在JedisPool最小连接空闲实例个数
    private static Boolean testOnBorrow= Boolean.parseBoolean(PropertiesUtil.getProperty("redis.text.borrow","true")); //在使用这个连接的时候,如果为true验证连接是否可用
    private static Boolean textOnReturn= Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return","true")); //在返回redis实例的时候,是否进行验证操作

    private static String redisIp = PropertiesUtil.getProperty("redis.ip");
    private static Integer redisPort = Integer.parseInt(PropertiesUtil.getProperty("redis.port"));

    //初始化连接池
    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(textOnReturn);

        //连接耗尽的时候,是否阻塞,false会抛出异常,true阻塞到超时
        config.setBlockWhenExhausted(true);
        pool = new JedisPool(config,redisIp,redisPort,1000*2);
    }
    static {
        initPool();
    }
    //从连接池获取实例
    public static Jedis getJedis(){
        return pool.getResource();
    }

    //返回给连接池实例
    public static void returnResource(Jedis jedis){
            pool.returnResource(jedis);
    }
    //当实例不可用时调用此方法
    public static void returnBrokenResource(Jedis jedis){
        pool.returnBrokenResource(jedis);
    }

    public static void main(String[] args) {
        Jedis jedis = pool.getResource();
        jedis.set("aa","12");
        jedis.set("22","22");
    }
}
