package com.px.core.cache.redis;

import java.util.Set;

import redis.clients.jedis.Jedis;

/**
 * Restful token helper 2015-12-03
 *
 * @author Jeff Xiao
 * @author jason
 */
public interface IRedisHelper {

	Jedis get();
	
	
	/**
     * 自增+1
     *
     * @param key
     */
	Long incr(String key);
	
	/**
     * 自增+1
     *
     * @param key
     * @param expire 过期时间
     */
	Long incr(String key, int expire);

    /**
     * 通过key删除（字节）
     *
     * @param key
     */
    void del(byte[] key);


    /**
     * 通过key删除
     *
     * @param key
     */
    void del(String key);


    /**
     * 添加key value 并且设置存活时间(byte)
     *
     * @param key
     * @param value
     * @param liveTime
     */
    void set(byte[] key, byte[] value, int liveTime);


    /**
     * 添加key value 并且设置存活时间
     *
     * @param key
     * @param value
     * @param liveTime
     */
    void set(String key, String value, int liveTime);


    /**
     * 添加key value
     *
     * @param key
     * @param value
     */
    void set(String key, String value);


    /**
     * 添加key value (字节)(序列化)
     *
     * @param key
     * @param value
     */
    void set(byte[] key, byte[] value);


    /**
     * 获取redis value (String)
     *
     * @param key
     * @return
     */
    String get(String key);


    /**
     * 获取redis value (byte [] )(反序列化)
     *
     * @param key
     * @return
     */
    byte[] get(byte[] key);


    /**
     * 通过正则匹配keys
     *
     * @param pattern
     * @return
     */
    Set<String> keys(String pattern);


    /**
     * 检查key是否已经存在
     *
     * @param key
     * @return
     */
    boolean exists(String key);

    /**
     * 判断是否有值
     * @param key
     * @param member
     * @return
     */
    Boolean sismember(String key, String member);
    
    /**
     * 清空redis 所有数据
     *
     * @return
     */
    String flushDB();

    /**
     * 查看redis里有多少数据
     *
     * @return
     */
    long dbSize();

    /**
     * ping
     *
     * @return
     */
    String ping();
    
    /**
     * 释放redis连接
     * @param jedis
     * @param isBroken
     */
    void releaseForPool(Jedis jedis, boolean isBroken);
}