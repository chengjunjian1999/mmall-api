package com.mmall.util;

import com.mmall.common.RedisPool;
import com.mmall.common.RedisShardedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

@Slf4j
public class RedisPoolUtil {

    public static String set(String key,String value){
        ShardedJedis jedis = null;
        String result = null;

        try{
            jedis = RedisShardedPool.getJedis();
            result =jedis.set(key,value);
        }catch (Exception e){
            log.error("set key:{} value:{} error",key,value,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }
    public static String get(String key){
        ShardedJedis jedis = null;
        String result = null;

        try{
            jedis = RedisShardedPool.getJedis();
            result = jedis.get(key);
        }catch (Exception e){
            log.error("get key:{} error",key,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }


    //用于redis存储session里面的数据,需要设置有效期, exTime单位是秒  (设置有效期)
    public static String setEx(String key,String value,int exTime){
        ShardedJedis jedis = null;
        String result = null;
        try{
            jedis = RedisShardedPool.getJedis();
            result = jedis.setex(key,exTime,value);
        }catch (Exception e){
            log.error("setEx key:{} value:{} exTime:{} error",key,value,exTime,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }
    //重新设置key有效期
    public static Long expire(String key,int exTime){
        ShardedJedis jedis = null;
        Long result = null;

        try{
            jedis = RedisShardedPool.getJedis();
            result = jedis.expire(key,exTime);
        }catch (Exception e){
            log.error("expire key:{} exTime:{} error",key,exTime,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }
    //删除key
    public static Long del(String key){
        ShardedJedis jedis = null;
        Long result = null;

        try{
            jedis = RedisShardedPool.getJedis();
            result = jedis.del(key);
        }catch (Exception e){
            log.error("del key:{} error",key,e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static void main(String[] args) {
        ShardedJedis jedis = RedisShardedPool.getJedis();
        RedisPoolUtil.set("cheng","cjj");
        String value = RedisPoolUtil.get("cheng");
        RedisPoolUtil.setEx("keyes","valuesex",60*10);
        RedisPoolUtil.expire("keyes",60*20);
        System.out.println(RedisPoolUtil.get("keyes"));
        RedisPoolUtil.del("cheng");
        System.out.println("end");
        System.out.println(value);

    }
}
