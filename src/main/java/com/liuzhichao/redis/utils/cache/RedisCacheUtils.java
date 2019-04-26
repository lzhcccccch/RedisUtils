package com.liuzhichao.redis.utils.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @author 刘志超
 * @date 2019-04-22 下午7:34:20
 * @version 类说明 操作Redis缓存工具类,封装StringRedisTemplate对象.
 */
@Component
public class RedisCacheUtils {

	@Autowired
	StringRedisTemplate stringRedisTemplate;

	// =============================common============================
	/**
	 * 指定缓存失效时间,时间需>0
	 * 
	 * @param key  键
	 * @param time 时间(秒)
	 * @return
	 */
	public boolean expire(String key, long time) {
		try {
			if (time > 0) {
				stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 根据key 获取过期时间
	 * 
	 * @param key 键 不能为null
	 * @return 时间(秒) 返回0代表为永久有效
	 */
	public long getExpire(String key) {
		return stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
	}

	/**
	 * 判断key是否存在
	 * 
	 * @param key 键
	 * @return true 存在 false不存在
	 */
	public boolean hasKey(String key) {
		try {
			return stringRedisTemplate.hasKey(key);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除缓存
	 * 
	 * @param key 可以传一个值 或多个
	 */
	@SuppressWarnings("unchecked")
	public void del(String... key) {
		if (key != null && key.length > 0) {
			if (key.length == 1) {
				stringRedisTemplate.delete(key[0]);
			} else {
				stringRedisTemplate.delete(CollectionUtils.arrayToList(key));
			}
		}
	}

	// ============================String=============================
	/**
	 * 普通缓存获取
	 * 
	 * @param key 键
	 * @return 值
	 */
	public String get(String key) {
		return key == null ? null : stringRedisTemplate.opsForValue().get(key);
	}

	/**
	 * 普通缓存放入,不过期
	 * 
	 * @param key   键
	 * @param value 值
	 * @return true成功 false失败
	 */
	public boolean set(String key, String value) {
		try {
			System.out.println(stringRedisTemplate == null);
			stringRedisTemplate.opsForValue().set(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 普通缓存放入并设置时间
	 * 
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
	 * @return true成功 false 失败
	 */
	public boolean set(String key, String value, long time) {
		try {
			if (time > 0) {
				stringRedisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
			} else {
				set(key, value);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 值递增
	 * 
	 * @param key 键
	 * @param by  要增加几(大于0)
	 * @return
	 */
	public long incr(String key, long delta) {
		if (delta < 0) {
			throw new RuntimeException("递增因子必须大于0");
		}
		return stringRedisTemplate.opsForValue().increment(key, delta);
	}

	/**
	 * 值递减
	 * 
	 * @param key 键
	 * @param by  要减少几(小于0)
	 * @return
	 */
	public long decr(String key, long delta) {
		if (delta < 0) {
			throw new RuntimeException("递减因子必须大于0");
		}
		return stringRedisTemplate.opsForValue().increment(key, -delta);
	}

	// ================================Map=================================
	/**
	 * HashGet
	 * 
	 * @param key  键 不能为null
	 * @param item 集合元素的key 不能为null
	 * @return 值
	 */
	public Object hget(String key, String item) {
		return stringRedisTemplate.opsForHash().get(key, item);
	}

	/**
	 * 获取hashKey对应的所有键值
	 * 
	 * @param key 键
	 * @return 对应的多个键值
	 */
	public Map<String, String> hmget(String key) {
		Map<Object, Object> map = new HashMap<>();
		map = stringRedisTemplate.opsForHash().entries(key);
		//将Map<Object, Object>转换为Map<Stirng, String>
		Map<String, String> result = new HashMap<>();
		Set<Object> keys=map.keySet();
        Iterator<Object> it=keys.iterator();
        while(it.hasNext())
        {
            String  itemKey = (String) it.next();
            String itemValue = (String) map.get(key);
            result.put(itemKey, itemValue);
        }
		return result;
	}

	/**
	 * HashSet
	 * 
	 * @param key 键
	 * @param map 对应多个键值
	 * @return true 成功 false 失败
	 */
	public boolean hmset(String key, Map<String, String> map) {
		try {
			stringRedisTemplate.opsForHash().putAll(key, map);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * HashSet 并设置时间
	 * 
	 * @param key  键
	 * @param map  对应多个键值
	 * @param time 时间(秒)
	 * @return true成功 false失败
	 */
	public boolean hmset(String key, Map<String, String> map, long time) {
		try {
			stringRedisTemplate.opsForHash().putAll(key, map);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 * 
	 * @param key   键
	 * @param item  项
	 * @param value 值
	 * @return true 成功 false失败
	 */
	public boolean hset(String key, String item, String value) {
		try {
			stringRedisTemplate.opsForHash().put(key, item, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 * 
	 * @param key   键
	 * @param item  项
	 * @param value 值
	 * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
	 * @return true 成功 false失败
	 */
	public boolean hset(String key, String item, String value, long time) {
		try {
			stringRedisTemplate.opsForHash().put(key, item, value);
			if (time > 0) {
				expire(key, time);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 删除hash表中的值
	 * 
	 * @param key  键 不能为null
	 * @param item 项 可以使多个 不能为null
	 */
	public void hdel(String key, String... item) {
		//将String转为Object
		Object[] itemObj = item;
		//方法接收参数为Object...
		stringRedisTemplate.opsForHash().delete(key, itemObj);
	}

	/**
	 * 判断hash表中是否有该项的值
	 * 
	 * @param key  键 不能为null
	 * @param item 项 不能为null
	 * @return true 存在 false不存在
	 */
	public boolean hHasKey(String key, String item) {
		return stringRedisTemplate.opsForHash().hasKey(key, item);
	}

	/**
	 * hash递增 如果不存在,就会创建一个 并把新增后的值返回
	 * 
	 * @param key  键
	 * @param item 项
	 * @param by   要增加几(大于0)
	 * @return
	 */
	public double hincr(String key, String item, double by) {
		return stringRedisTemplate.opsForHash().increment(key, item, by);
	}

	/**
	 * hash递减
	 * 
	 * @param key  键
	 * @param item 项
	 * @param by   要减少记(小于0)
	 * @return
	 */
	public double hdecr(String key, String item, double by) {
		return stringRedisTemplate.opsForHash().increment(key, item, -by);
	}

	// ============================set=============================
	/**
	 * 根据key获取Set中的所有值
	 * 
	 * @param key 键
	 * @return
	 */
	public Set<String> sGet(String key) {
		try {
			return stringRedisTemplate.opsForSet().members(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据value从一个set中查询,是否存在
	 * 
	 * @param key   键
	 * @param value 值
	 * @return true 存在 false不存在
	 */
	public boolean sHasKey(String key, String value) {
		try {
			return stringRedisTemplate.opsForSet().isMember(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将数据放入set缓存
	 * 
	 * @param key    键
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public long sSet(String key, String... values) {
		try {
			return stringRedisTemplate.opsForSet().add(key, values);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 将set数据放入缓存
	 * 
	 * @param key    键
	 * @param time   时间(秒)
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public long sSetAndTime(String key, long time, String... values) {
		try {
			Long count = stringRedisTemplate.opsForSet().add(key, values);
			if (time > 0)
				expire(key, time);
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 获取set缓存的长度
	 * 
	 * @param key 键
	 * @return
	 */
	public long sGetSetSize(String key) {
		try {
			return stringRedisTemplate.opsForSet().size(key);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 移除值为value的
	 * 
	 * @param key    键
	 * @param values 值 可以是多个
	 * @return 移除的个数
	 */
	public long setRemove(String key, String... values) {
		try {
			//将String转换为Object
			Object[] valuesObj = values;
			//方法接收参数为Object...
			Long count = stringRedisTemplate.opsForSet().remove(key, valuesObj);
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	// ===============================list=================================

	/**
	 * 获取list缓存的内容
	 * 
	 * @param key   键
	 * @param start 开始
	 * @param end   结束 0 到 -1代表所有值
	 * @return
	 */
	public List<String> lGet(String key, long start, long end) {
		try {
			return stringRedisTemplate.opsForList().range(key, start, end);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取list缓存的长度
	 * 
	 * @param key 键
	 * @return
	 */
	public long lGetListSize(String key) {
		try {
			return stringRedisTemplate.opsForList().size(key);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 通过索引 获取list中的值
	 * 
	 * @param key   键
	 * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
	 * @return
	 */
	public Object lGetIndex(String key, long index) {
		try {
			return stringRedisTemplate.opsForList().index(key, index);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将list放入缓存
	 * 
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒)
	 * @return
	 */
	public boolean lSet(String key, String value) {
		try {
			stringRedisTemplate.opsForList().rightPush(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将list放入缓存
	 * 
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒)
	 * @return
	 */
	public boolean lSet(String key, String value, long time) {
		try {
			stringRedisTemplate.opsForList().rightPush(key, value);
			if (time > 0)
				expire(key, time);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将list放入缓存
	 * 
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒)
	 * @return
	 */
	public boolean lSet(String key, List<String> value) {
		try {
			stringRedisTemplate.opsForList().rightPushAll(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将list放入缓存
	 * 
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒)
	 * @return
	 */
	public boolean lSet(String key, List<String> value, long time) {
		try {
			stringRedisTemplate.opsForList().rightPushAll(key, value);
			if (time > 0)
				expire(key, time);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 根据索引修改list中的某条数据
	 * 
	 * @param key   键
	 * @param index 索引
	 * @param value 值
	 * @return
	 */
	public boolean lUpdateIndex(String key, long index, String value) {
		try {
			stringRedisTemplate.opsForList().set(key, index, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 移除N个值为value
	 * 
	 * @param key   键
	 * @param count 移除多少个
	 * @param value 值
	 * @return 移除的个数
	 */
	public long lRemove(String key, long count, String value) {
		try {
			Long remove = stringRedisTemplate.opsForList().remove(key, count, value);
			return remove;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

}
