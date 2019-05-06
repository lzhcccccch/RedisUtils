package com.liuzhichao.redis.utils.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author 刘志超
 * @date 2019-04-19 下午4:07:00
 * @version 
 * 类说明 	Redis锁
 */
@Component
public class RedisLockUtils {

	@Autowired
	private StringRedisTemplate stringredisTemplate;

	/**
	 * 锁过期时间,精确到毫秒
	 */
	@Value("${edw.sdp.spring.redis.timeoutMsecs}")
	private String timeoutMsecs;
	/**
	 * 锁后缀
	 */
	@Value("${edw.sdp.spring.redis.lockSuf}")
	private String lockSuf;

	/**
	 * 加锁,重载,不设置过时时间则采用默认值10*1000ms;
	 * @param lockKey
	 * @return
	 */
	public synchronized boolean lock(String lockKey) {
		System.out.println("配置文件中的默认超时时间:"+this.timeoutMsecs);
		this.timeoutMsecs = String.valueOf(Long.parseLong(this.timeoutMsecs)+System.currentTimeMillis());
		return this.lock(lockKey, this.timeoutMsecs);
	}

	public synchronized boolean lock(String lockKey, String timeoutMsecs) {
		//超时时间早于当前时间,不能加锁
		if ( Long.parseLong(timeoutMsecs) < System.currentTimeMillis() ) {
			System.out.println("超时时间早于当前时间");
			return false;
		}
		lockKey = lockKey + lockSuf;
		String timeout = timeoutMsecs;
		if (stringredisTemplate.opsForValue().setIfAbsent(lockKey, timeout)) {
			return true;
		}
		String currentValue = (String) stringredisTemplate.opsForValue().get(lockKey);
		System.out.println("原锁的值:"+currentValue);
		if ( !StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis() ) {
			String oldValue = (String) stringredisTemplate.opsForValue().getAndSet(lockKey, timeout);
			if ( !StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue)) {
				return true;
			}
		}
		try {
			//采用随机值(0,1)*100ms
			Thread.sleep((int)Math.random()*100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 解锁
	 */
	public synchronized void unlock(String lockKey) {
		String timeoutMsecs = String.valueOf(Long.parseLong(this.timeoutMsecs)+System.currentTimeMillis());
		this.unlock(lockKey, timeoutMsecs);
	}
	
	/**
	 * 对锁有严格要求时,调用该方法:自己的锁未过期时只能自己释放
	 * @param lockKey
	 * @param timeoutMsecs 超时时间与加锁时的值一样
	 */
	public synchronized void unlock(String lockKey, String timeoutMsecs) {
		lockKey = lockKey + lockSuf;
		String currentValue = (String) stringredisTemplate.opsForValue().get(lockKey);
		if ( !StringUtils.isEmpty(currentValue) && currentValue.equals(timeoutMsecs) ) {
			stringredisTemplate.opsForValue().getOperations().delete(lockKey);
		}
	}

}
