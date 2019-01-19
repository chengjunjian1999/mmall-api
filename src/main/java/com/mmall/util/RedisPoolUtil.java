package com.mmall.util;

import com.mmall.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

@Slf4j
public class RedisPoolUtil {

    public static String set(String key,String value){
        Jedis jedis = null;
        String result = null;

        try{
            jedis = RedisPool.getJedis();
            result =jedis.set(key,value);
        }catch (Exception e){
            log.error("set key:{} value:{} error",key,value,e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }
    public static String get(String key){
        Jedis jedis = null;
        String result = null;

        try{
            jedis = RedisPool.getJedis();
            result = jedis.get(key);
        }catch (Exception e){
            log.error("get key:{} error",key,e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }


    //用于redis存储session里面的数据,需要设置有效期, exTime单位是秒  (设置有效期)
    public static String setEx(String key,String value,int exTime){
        Jedis jedis = null;
        String result = null;
        try{
            jedis = RedisPool.getJedis();
            result = jedis.setex(key,exTime,value);
        }catch (Exception e){
            log.error("setEx key:{} value:{} exTime:{} error",key,value,exTime,e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }
    //重新设置key有效期
    public static Long expire(String key,int exTime){
        Jedis jedis = null;
        Long result = null;

        try{
            jedis = RedisPool.getJedis();
            result = jedis.expire(key,exTime);
        }catch (Exception e){
            log.error("expire key:{} exTime:{} error",key,exTime,e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }
    //删除key
    public static Long del(String key){
        Jedis jedis = null;
        Long result = null;

        try{
            jedis = RedisPool.getJedis();
            result = jedis.del(key);
        }catch (Exception e){
            log.error("del key:{} error",key,e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static void main(String[] args) {
        Jedis jedis = RedisPool.getJedis();
        RedisPoolUtil.set("cheng","cjj");
        String value = RedisPoolUtil.get("cheng");
        RedisPoolUtil.setEx("keyes","valuesex",60*10);
        RedisPoolUtil.expire("keyes",60*20);
        RedisPoolUtil.del("cheng");
        System.out.println("end");
        System.out.println(value);

    }
}
