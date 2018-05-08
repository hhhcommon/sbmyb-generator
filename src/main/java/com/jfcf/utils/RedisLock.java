/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: 玖富时代</p>
 * @author ducongcong
 * @version 1.0
 */
package com.jfcf.utils;

import java.util.Random;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
/**
 * Redis分布式锁<br>
 * <p>注意事项：以key对应的资源加锁，</p>
 * <b>用法一：</b>
 * <p> Lock lock = new RedisLock("test",true);//获取资源锁</p>
   <p> lock.lock();//加锁业务等待时间30秒，如果锁住就锁60秒(不unlock的情况下)</p>
   <p> lock.lock(3000);//业务等待时间3秒，如果锁住就锁60秒(不unlock的情况下)</p>
   <p> lock.isLocked()//加锁后判断是否锁住了</p>
   <p> lock.lockTime(20) 锁住就锁20秒，默认30秒内去获取redis资源(不unlock的情况下)</p>
   <p> lock.lock(3000,10) 业务等待时间3秒，锁住就锁10秒(不unlock的情况下) </p>
   <p> 业务逻辑代码</p>
   <p> lock.unlock();//释放锁</p>
 * <b>用法二：</b>
 * <p> Lock lock = RedisLock.getInstance("test", true);//获取资源锁，并缓存</p>
   <p> lock.lock();//加锁业务等待时间30秒，如果锁住就锁60秒(不unlock的情况下)</p>
   <p> lock.lock(3000);//业务等待时间3秒，如果锁住就锁60秒(不unlock的情况下)</p>
   <p> lock.isLocked()//加锁后判断是否锁住了</p>
   <p> lock.lockTime(20) 锁住就锁20秒，默认30秒内去获取redis资源(不unlock的情况下)</p>
   <p> lock.lock(3000,10) 业务等待时间3秒，锁住就锁10秒(不unlock的情况下) </p>
   <p> 业务逻辑代码</p>
   <p> lock.unlock();//释放锁</p>
 * @author ducongcong
 * @date 2016年9月23日
 */
public class RedisLock implements Lock {
	private static  Logger logger =  LoggerFactory.getLogger(RedisLock.class);
	/**
	 * redis中锁住存储的值
	 */
    private static final String LOCKED = "LOCKED";
    /**
     * redis缓存模块   
     * 例如  onecard_LOCK_资源
     */
    private static final String MODULE = "LOCK";
    

    /**
     * 30000毫秒内去设置redis,无法设置则
     */
    private static final long TIME_OUT = 30000;

    /**
     * 失效时间 默认60s
     */
    public static final int EXPIRE = 60;

    /**
     * 存储在redis中的key
     */
    private String key;

    /**
     * 状态标志，=true 对应的key加锁成功
     * unlock后= false
     */
    private volatile boolean locked = false;
    /**
     * 存储锁对象（防止重复创建锁），根据具体业务
     */
    private static ConcurrentMap<String, RedisLock> map = Maps.newConcurrentMap();

    /**
     * 设置锁的key
     * @param key
     */
    public RedisLock(String key){
    	 this.key =  CacheUtil.getKey(MODULE, key);
    }
    /**
     * 设置锁的key
     * @param key key
     * @param isJoin 是否拼接
     */
    public RedisLock(String key,boolean isJoin) {
    	if(isJoin){
        	 this.key =  CacheUtil.getKey(MODULE, key);
    	}else{
    		this.key = key;
    	}
    }
    /**
     * 验证当前设置的key是否被锁住，
     * @return 返回true表示资源被锁，返回false表示资源空闲中
     * @author ducongcong
     * @createDate 2016年12月15日
     * @updateDate
     */
    public boolean isLock(){
    	String str = JedisUtil.get(this.key);
    	if(StringUtils.isNotEmpty(str)){
    		return Boolean.TRUE;
    	}
    	return Boolean.FALSE;
    }
    /**
     * 查看锁是否被锁住，加锁住可以调用这个判断
     * @return
     * @author ducongcong
     * @createDate 2017年8月25日
     * @updateDate
     */
    public boolean isLocked(){
    	return this.locked;
    }
    /**
     * 获取针对key的锁,可以重复利用
     * @param key
     * @return
     * @author ducongcong
     * @createDate 2016年9月23日
     * @updateDate
     */
    public static RedisLock getInstance(String key,boolean isJoin) {
    	if(isJoin){
        	key = CacheUtil.getKey(MODULE, key);
    	}
    	RedisLock redisLock = map.get(key);
    	if(null == redisLock){
    		redisLock = new RedisLock(key,Boolean.TRUE);
    		map.put(key, redisLock);
    	}
        return redisLock;
    }
    /**
     * 加锁
     * @param timeout  获取锁的时间，即业务等待时间
     * @author ducongcong
     * @createDate 2016年9月23日
     * @updateDate
     */
    public boolean lock(long timeout) {
    	return lock(timeout,EXPIRE);
    }
    /**
     * 加锁,默认30秒去获取锁资源
     * @param lockTime   锁多长时间释放
     * @author ducongcong
     * @createDate 2017年8月25日
     * @updateDate
     */
    public boolean lockTime(int lockTime) {
    	return lock(TIME_OUT,lockTime);
    }
    /**
     * 加锁
     * @param timeout  获取锁的时间，即业务等待时间
     * @param lockTime  锁多长时间释放(不unlock的情况下)
     * @author ducongcong
     * @createDate 2016年9月23日
     * @updateDate
     */
    public boolean lock(long timeout,int  lockTime) {
    	long nano = System.nanoTime();
    	timeout *= 1000000;
    	final Random r = new Random();
    	try {
    		while ((System.nanoTime() - nano) < timeout) {
    			if (JedisUtil.setnx(key, LOCKED) > 0) {
    				if(lockTime > 0){
    					JedisUtil.expire(key, lockTime);
    				}else{
    					JedisUtil.expire(key, EXPIRE);
    				}
    				locked = true;
    				if(logger.isDebugEnabled()){
    					logger.debug("add RedisLock[" + key + "].");
    				}
    				break;
    			}
    			Thread.sleep(3, r.nextInt(500));
    		}
    	} catch (Exception e) {
    		if(logger.isErrorEnabled()){
    			logger.error("",e);
    		}
    	}
    	return locked;
    }
    /**
     * 加锁
     * 默认业务等待30秒，如果30秒还未获取到锁，则业务还会往下走，
     * 最好使用isLocked()判断是否加锁成功
     */
	@Override
	public void lock() {
		lock(TIME_OUT);
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
	}

	@Override
	public boolean tryLock() {
		return false;
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit)
			throws InterruptedException {
		return false;
	}

	@Override
	public void unlock() {
		unlock(true);
	}
	/**
	 * 释放资源
	 * @param isClear 是否从map中清除 true清除（防止重复创建锁）
	 * @author ducongcong
	 * @createDate 2016年9月23日
	 * @updateDate
	 */
	public void unlock(boolean isClear) {
		if (locked) {
			if(logger.isDebugEnabled()){
				logger.debug("release RedisLock[" + key + "].");
			}
            if(isClear){
                //map中有就删除
                map.remove(key);
            }
            JedisUtil.del(key);
            locked = false;
        }
	}

	@Override
	public Condition newCondition() {
		return null;
	}

}
