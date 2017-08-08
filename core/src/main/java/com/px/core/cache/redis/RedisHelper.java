package com.px.core.cache.redis;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Jedis tool
 */
public class RedisHelper implements IRedisHelper {

	private static final Logger logger = LoggerFactory.getLogger(RedisHelper.class);
	
    private Jedis jedis;

    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    @Autowired
    protected JedisPool jedisPool;

    @Override
	public Long incr(String key, int expire) {
    	Jedis jedis = getJedisFromPool();
        try{
        	Long value = jedis.incr(key);
        	jedis.expire(key, expire);
            return value;
        }
        catch (Exception ex){
            releaseForPool(jedis, true);
        }
        finally {
            releaseForPool(jedis, false);
        }
        return null;
	}
    
    @Override
	public Long incr(String key) {
    	Jedis jedis = getJedisFromPool();
        try{
            return jedis.incr(key);
        }
        catch (Exception ex){
            releaseForPool(jedis, true);
        }
        finally {
            releaseForPool(jedis, false);
        }
        return null;
	}
    
    /**
     * 通过key删除（字节）
     *
     * @param key
     */
    public void del(byte[] key) {
        Jedis jedis = getJedisFromPool();
        try{
            jedis.del(key);
        }
        catch (Exception ex){
            releaseForPool(jedis, true);
        }
        finally {
            releaseForPool(jedis, false);
        }
    }

    /**
     * 通过key删除
     *
     * @param key
     */
    public void del(String key) {
        Jedis jedis = getJedisFromPool();
        try{
            jedis.del(key);
        }
        catch (Exception ex){
            releaseForPool(jedis, true);
        }
        finally {
            releaseForPool(jedis, false);
        }
    }

    /**
     * 添加key value 并且设置存活时间(byte)
     *
     * @param key
     * @param value
     * @param liveTime
     */
    public void set(byte[] key, byte[] value, int liveTime) {
        Jedis jedis = getJedisFromPool();
        try{
            jedis.set(key, value);
            jedis.expire(key, liveTime);
        }
        catch (Exception ex){
            releaseForPool(jedis, true);
        }
        finally {
            releaseForPool(jedis, false);
        }
    }

    /**
     * 添加key value 并且设置存活时间
     *
     * @param key
     * @param value
     * @param liveTime
     */
    public void set(String key, String value, int liveTime) {
        Jedis jedis = getJedisFromPool();
        try{
            jedis.set(key, value);
            jedis.expire(key, liveTime);
        }
        catch (Exception ex){
            releaseForPool(jedis, true);
        }
        finally {
            releaseForPool(jedis, false);
        }
    }

    /**
     * 添加key value
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        Jedis jedis = getJedisFromPool();
        try{
            jedis.set(key, value);
        }
        catch (Exception ex){
            releaseForPool(jedis, true);
        }
        finally {
            releaseForPool(jedis, false);
        }
    }

    /**
     * 添加key value (字节)(序列化)
     *
     * @param key
     * @param value
     */
    public void set(byte[] key, byte[] value) {
        Jedis jedis = getJedisFromPool();
        try{
            jedis.set(key, value);
        }
        catch (Exception ex){
            releaseForPool(jedis, true);
        }
        finally {
            releaseForPool(jedis, false);
        }
    }

    /**
     * 获取redis value (String)
     *
     * @param key
     * @return
     */
    public String get(String key) {
        Jedis jedis = getJedisFromPool();
        String ret = "";
        try{
            ret = jedis.get(key);
        }
        catch (Exception ex){
            releaseForPool(jedis, true);
        }
        finally {
            releaseForPool(jedis, false);
        }
        return ret;
    }

    /**
     * 获取redis value (byte [] )(反序列化)
     *
     * @param key
     * @return
     */
    public byte[] get(byte[] key) {
        Jedis jedis = getJedisFromPool();
        byte[] ret = null;
        try{
            ret = jedis.get(key);
        }
        catch (Exception ex){
            releaseForPool(jedis, true);
        }
        finally {
            releaseForPool(jedis, false);
        }
        return ret;
    }

    /**
     * 通过正则匹配keys
     *
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern) {
        Jedis jedis = getJedisFromPool();
        Set<String> ret = null;
        try{
            ret = jedis.keys(pattern);
        }
        catch (Exception ex){
            releaseForPool(jedis, true);
        }
        finally {
            releaseForPool(jedis, false);
        }
        return ret;
    }

    /**
     * 检查key是否已经存在
     *
     * @param key
     * @return
     */
    public boolean exists(String key) {
        Jedis jedis = getJedisFromPool();
        boolean ret = false;
        try{
            ret = jedis.exists(key);
        }
        catch (Exception ex){
            releaseForPool(jedis, true);
        }
        finally {
            releaseForPool(jedis, false);
        }
        return ret;
    }
    
    @Override
	public Boolean sismember(String key, String member) {
    	Jedis jedis = getJedisFromPool();
    	try{
    		return jedis.sismember(key, member);
    	}catch(Exception ex){
    		releaseForPool(jedis, true);
    	}finally{
    		releaseForPool(jedis, false);
    	}
		return true;
	}
    
    /**
     * 清空redis 所有数据
     *
     * @return
     */
    public String flushDB() {
        Jedis jedis = getJedisFromPool();
        String ret = "";
        try{
            ret = jedis.flushDB();
        }
        catch (Exception ex){
            releaseForPool(jedis, true);
        }
        finally {
            releaseForPool(jedis, false);
        }
        return ret;
    }

    /**
     * 查看redis里有多少数据
     */
    public long dbSize() {
        Jedis jedis = getJedisFromPool();
        long ret = 0;
        try{
            ret = jedis.dbSize();
        }
        catch (Exception ex){
            releaseForPool(jedis, true);
        }
        finally {
            releaseForPool(jedis, false);
        }
        return ret;
    }

    /**
     * 检查是否连接成功
     *
     * @return
     */
    public String ping() {
        Jedis jedis = getJedisFromPool();
        String ret = "";
        try{
            ret = jedis.ping();
        }
        catch (Exception ex){
            releaseForPool(jedis, true);
        }
        finally {
            releaseForPool(jedis, false);
        }
        return ret;
    }
    
    @Override
	public Jedis get() {
		return getJedisFromPool();
	}

    /**
     * 获取一个jedis 客户端
     *
     * @return
     */
    private Jedis getJedis() {
        if (jedis == null) {
            jedis = jedisConnectionFactory.getShardInfo().createResource();
            return jedis;
        }
        return jedis;
    }

    public void releaseForPool(Jedis jedis, boolean isBroken) {
        if (jedis != null) {
        	try{
        		if (isBroken) {
                    jedis.close();
                    //jedisPool.returnBrokenResource(jedis);
                } else {
                    jedis.close();
                    //jedisPool.returnResource(jedis);
                }
        	}catch(Exception e){
        		// 在Pool以外强行销毁Jedis
        		if(jedis.isConnected()){
        			try{
        				try{
        					jedis.quit();
        				}catch(Exception ex){}
        				jedis.disconnect();
        			}catch(Exception ex){}
        		}
        	}
        }
    }

    private Jedis getJedisFromPool(){
        return jedisPool.getResource();
    }
}
