/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: 玖富时代</p>
 * @author ducongcong
 * @version 1.0
 */
package com.jfcf.utils;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import com.jfinal.kit.PropKit;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;

public class JedisUtil {
	private static Cache cache = Redis.use(PropKit.get(ConstantConfig.CONFIG_REDIS_CACHENAME,"onecard"));
	public static final int EXRP_HOUR = 60 * 60; // 一小时
	public static final int EXRP_12HOURS = 60 * 60 * 12; // 12小时
	public static final int EXRP_DAY = 60 * 60 * 24; // 一天
	public static final int EXRP_MONTH = 60 * 60 * 24 * 30; // 一个月
	public static final int THREE_DAY = 60 * 60 * 24 * 3; // 三天
	public static final String ASC = "asc"; // 正序
	public static final String DESC = "desc"; // 倒序
	public static Cache getCache(){
		return cache;
	}
	/**
	 * 删除一个或多个key
	 * 
	 * @param keys
	 * @since >=1.0.0
	 * @return
	 */
	public static Long del(String... keys) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.del(keys);
		}finally{
			cache.close(jedis);
		}
	}
	/**
	 * 判断key是否存在
	 * 
	 * @param key
	 * @since >=1.0.0
	 * @return
	 */
	public static Boolean exists(String key) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.exists(key);
		}finally{
			cache.close(jedis);
		}
	}
	/**
	 * 返回 key 所关联的字符串值,如果 key 不存在那么返回特殊值 nil 假如 key 储存的值不是字符串类型，返回一个错误，因为 GET
	 * 只能用于处理字符串值。
	 * 
	 * @param key
	 * @return 当 key 不存在时，返回 nil ，否则，返回 key 的值。 如果 key 不是字符串类型，那么返回一个错误。
	 */
	public static String get(String key) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.get(key);
		}finally{
			cache.close(jedis);
		}
	}
	/**
	 * 将字符串值 value 关联到 key。如果 key 已经持有其他值， SET 就覆写旧值，无视类型。
	 * 
	 * @param key
	 * @param value
	 * @return 在 Redis 2.6.12 版本以前， SET 命令总是返回 OK 。 从 Redis 2.6.12 版本开始， SET
	 *         在设置操作成功完成时，才返回 OK 。 如果设置了 NX 或者 XX
	 *         ，但因为条件没达到而造成设置操作未执行，那么命令返回空批量回复（NULL Bulk Reply）。
	 */
	public static String set(String key, String value) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.set(key, value);
		}finally{
			cache.close(jedis);
		}
	}
	/**
	 * 将字符串值 value 关联到 key ,
	 * 
	 * @param key
	 * @param value
	 * @param nxxx
	 *            NX ：只在键不存在时，才对键进行设置操作。 XX ：只在键已经存在时，才对键进行设置操作
	 * @return
	 */
	public static String set(String key, String value, String nxxx) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.set(key, value, nxxx);
		}finally{
			cache.close(jedis);
		}
	}
	
	/**
	 * 将值 value 关联到 key ，并将 key 的生存时间设为 seconds (以秒为单位)。 如果 key 已经存在， SETEX
	 * 命令将覆写旧值。
	 * 
	 * @param key
	 * @param seconds
	 * @param value
	 * @return 设置成功时返回 OK 。 当 seconds 参数不合法时，返回一个错误。
	 */
	public static String setex(String key, int seconds, String value) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.setex(key, seconds, value);
		}finally{
			cache.close(jedis);
		}
	}
	/**
	 * 这个命令和 SETEX 命令相似，但它以毫秒为单位设置 key 的生存时间，而不是像 SETEX 命令那样，以秒为单位。
	 * 
	 * @param key
	 * @param milliseconds
	 * @param value
	 * @return
	 */
	public static String psetex(String key, long milliseconds, String value) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.psetex(key, milliseconds, value);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 将 key 的值设为 value ，当且仅当 key 不存在.若给定的 key 已经存在，则 SETNX 不做任何动作
	 * 
	 * @param key
	 * @param value
	 * @return 设置成功，返回 1 。设置失败，返回 0 。
	 */
	public static Long setnx(String key, String value) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.setnx(key, value);
		}finally{
			cache.close(jedis);
		}
	}
	/**
	 * 给key设置生存时间
	 * 
	 * @param key
	 * @param seconds
	 *            生存时间（秒）
	 * @since >=1.0.0
	 */
	public static Long expire(String key, int seconds) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.expire(key, seconds);
		}finally{
			cache.close(jedis);
		}
	}
	/**
	 * 返回 key 所储存的字符串值的长度,当 key 储存的不是字符串值时，返回一个错误。
	 * 
	 * @param key
	 * @return 字符串值的长度。当 key 不存在时，返回 0 。
	 */
	public static Long strlen(String key) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.strlen(key);
		}finally{
			cache.close(jedis);
		}
	}
	/**
	 * 给key设置生存时间
	 * 
	 * @param key
	 * @param unixTime
	 *            unix时间戳，已秒为单位
	 * @since >=1.2.0
	 * @return
	 */
	public static Long expireAt(String key, long unixTime) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.expireAt(key, unixTime);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 根据表达式匹配所有的key
	 * 
	 * @param pattern
	 *            <p>
	 *            ‘*’表示所有，‘?’表示单个
	 *            <p>
	 *            <p>
	 *            ‘*’ 匹配数据库中所有 key 。
	 *            <p>
	 *            <p>
	 *            h?llo 匹配 hello ， hallo 和 hxllo 等。
	 *            <p>
	 *            <p>
	 *            h*llo 匹配 hllo 和 heeeeello 等。
	 *            <p>
	 *            <p>
	 *            h[ae]llo 匹配 hello 和 hallo ，但不匹配 hillo 。
	 *            <p>
	 *            <p>
	 *            特殊符号用 \ 隔开
	 *            <p>
	 * @since >=1.0.0
	 * @return 返回找到的key的set集合
	 */
	public static Set<String> keys(String pattern) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.keys(pattern);
		}finally{
			cache.close(jedis);
		}
	}
	
	/**
	 * 移除给定 key 的生存时间，将这个 key 从『易失的』(带生存时间 key )转换成『持久的』(一个不带生存时间、永不过期的 key )。
	 * 
	 * @param key
	 * @since 2.2.0
	 * @return 当生存时间移除成功时，返回 1 .如果 key 不存在或 key 没有设置生存时间，返回 0
	 */
	public static Long persist(String key) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.persist(key);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 这个命令和 EXPIRE 命令的作用类似，但是它以毫秒为单位设置 key 的生存时间，而不像 EXPIRE 命令那样，以秒为单位
	 * 
	 * @param key
	 * @param milliseconds
	 *            毫秒时间
	 * @since 2.6.0
	 * @return
	 */
	public static Long pexpire(String key, long milliseconds) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.pexpire(key, milliseconds);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 这个命令和 EXPIREAT 命令类似，但它以毫秒为单位设置 key 的过期 unix 时间戳，而不是像 EXPIREAT 那样，以秒为单位
	 * 
	 * @param key
	 * @param milliseconds
	 * @since >=2.6.0
	 * @return
	 */
	public static Long pexpireAt(String key, long millisecondsTimestamp) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.pexpireAt(key, millisecondsTimestamp);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)。
	 * 
	 * @param key
	 * @since >=1.0.0
	 * @return 当 key 不存在时，返回 -2 。<br>
	 *         当 key 存在但没有设置剩余生存时间时，返回 -1 。<br>
	 *         否则，以秒为单位，返回 key 的剩余生存时间。
	 */
	public static Long ttl(String key) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.ttl(key);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 这个命令类似于 TTL 命令，但它以毫秒为单位返回 key 的剩余生存时间，而不是像 TTL 命令那样，以秒为单位。
	 * 
	 * @param key
	 * @since >=2.6.0
	 * @return 当 key 不存在时，返回 -2 。<br>
	 *         当 key 存在但没有设置剩余生存时间时，返回 -1 。<br>
	 *         否则，以毫秒为单位，返回 key 的剩余生存时间。
	 */
	public static Long pttl(String key) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.pttl(key);
		}finally{
			cache.close(jedis);
		}
	}
	/**
	 * 返回 key 所储存的值的类型。
	 * 
	 * @param key
	 * @since 1.0.0
	 * @return none (key不存在)<br>
	 *         string (字符串)<br>
	 *         list (列表)<br>
	 *         set (集合)<br>
	 *         zset (有序集)<br>
	 *         hash (哈希表)
	 */
	public static String type(String key) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.type(key);
		}finally{
			cache.close(jedis);
		}
	}
	/**
	 * 将 key 中储存的数字值增一。如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR
	 * 操作。如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。 本操作的值限制在 64 位(bit)有符号数字表示之内。
	 * 
	 * @param key
	 * @return 执行 INCR 命令之后 key 的值。
	 */
	public static Long incr(String key) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.incr(key);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 将 key 所储存的值加上增量 increment 。如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR
	 * 操作。如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。 本操作的值限制在 64 位(bit)有符号数字表示之内。
	 * 
	 * @param key
	 * @param increment
	 * @return 加上 increment 之后， key 的值。
	 */
	public static Long incrBy(String key, Long increment) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.incrBy(key, increment);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 为 key 中所储存的值加上浮点数增量 increment。如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR
	 * 操作。如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。 本操作的值限制在 64 位(bit)有符号数字表示之内。
	 * 当以下任意一个条件发生时，返回一个错误： key 的值不是字符串类型(因为 Redis
	 * 中的数字和浮点数都以字符串的形式保存，所以它们都属于字符串类型） key 当前的值或者给定的增量 increment
	 * 不能解释(parse)为双精度浮点数(double precision floating point number）
	 * 除此之外，无论加法计算所得的浮点数的实际精度有多长， INCRBYFLOAT 的计算结果也最多只能表示小数点的后十七位。
	 * 
	 * @param key
	 * @param increment
	 * @return 执行命令之后 key 的值。
	 */
	public static Double incrByFloat(String key, double increment) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.incrByFloat(key, increment);
		}finally{
			cache.close(jedis);
		}
	}
	/**
	 * 将 key 中储存的数字值减一。如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作。
	 * 
	 * @param key
	 * @return 执行 DECR 命令之后 key 的值
	 */
	public static Long decr(String key) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.decr(key);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 将 key 所储存的值减去减量 decrement 。如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECRBY
	 * 操作。
	 * 
	 * @param key
	 * @param decrement
	 * @return 减去 decrement 之后， key 的值。
	 */
	public static Long decrBy(String key, Long decrement) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.decrBy(key, decrement);
		}finally{
			cache.close(jedis);
		}
	}
	/**
	 * 返回指定keys的列表数据
	 * 
	 * @param keys
	 * @return 一个包含所有给定 key 的值的列表。
	 */
	public static List<String> mget(String... keys) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.mget(keys);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 同时设置一个或多个 key-value 对。 如果某个给定 key 已经存在，那么 MSET
	 * 会用新值覆盖原来的旧值，如果这不是你所希望的效果，请考虑使用 MSETNX 命令：它只会在所有给定 key 都不存在的情况下进行设置操作。
	 * MSET 是一个原子性(atomic)操作，所有给定 key 都会在同一时间内被设置，某些给定 key 被更新而另一些给定 key
	 * 没有改变的情况，不可能发生。
	 * 
	 * @param keysvalues
	 * @return 总是返回 OK (因为 MSET 不可能失败)
	 */
	public static String mset(String... keysvalues) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.mset(keysvalues);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在。 即使只有一个给定 key 已存在， MSETNX
	 * 也会拒绝执行所有给定 key 的设置操作。 MSETNX 是原子性的
	 * 
	 * @param keysvalues
	 * @return 当所有 key 都成功设置，返回 1 。 如果所有给定 key 都设置失败(至少有一个 key 已经存在)，那么返回 0 。
	 */
	public static Long msetnx(String... keysvalues) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.msetnx(keysvalues);
		}finally{
			cache.close(jedis);
		}
	}
	/**
	 * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
	 * 
	 * @param key
	 * @param fields
	 * @return 被成功移除的域的数量，不包括被忽略的域。
	 */
	public static Long hdel(String key, String... fields) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.hdel(key, fields);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 查看哈希表 key 中，给定域 field 是否存在。
	 * 
	 * @param key
	 * @param field
	 * @return 如果哈希表含有给定域，返回 1 。如果哈希表不含有给定域，或 key 不存在，返回 0 。
	 */
	public static Boolean hexists(String key, String field) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.hexists(key, field);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 返回哈希表 key 中给定域 field 的值。
	 * 
	 * @param key
	 * @param field
	 * @return 给定域的值。当给定域不存在或是给定 key 不存在时，返回 nil 。
	 */
	public static String hget(String key, String field) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.hget(key, field);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 返回哈希表 key 中，所有的域和值。在返回值里，紧跟每个域名(field
	 * name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍。
	 * 
	 * @param key
	 * @return 以列表形式返回哈希表的域和域的值。若 key 不存在，返回空列表。
	 */
	public static Map<String, String> hgetAll(String key) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.hgetAll(key);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 为哈希表 key 中的域 field 的值加上增量 increment ,增量也可以为负数，相当于对给定域进行减法操作。 如果 key
	 * 不存在，一个新的哈希表被创建并执行 HINCRBY 命令。如果域 field 不存在，那么在执行命令前，域的值被初始化为 0 。
	 * 对一个储存字符串值的域 field 执行 HINCRBY 命令将造成一个错误。本操作的值被限制在 64 位(bit)有符号数字表示之内。
	 * 
	 * @param key
	 * @param field
	 * @param increment
	 * @return
	 */
	public static Long hincrBy(String key, String field, long increment) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.hincrBy(key, field, increment);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 为哈希表 key 中的域 field 加上浮点数增量 increment 。如果哈希表中没有域 field ，那么 HINCRBYFLOAT
	 * 会先将域 field 的值设为 0 ，然后再执行加法操作。 如果键 key 不存在，那么 HINCRBYFLOAT 会先创建一个哈希表，再创建域
	 * field ，最后再执行加法操作。 当以下任意一个条件发生时，返回一个错误： 域 field 的值不是字符串类型(因为 redis
	 * 中的数字和浮点数都以字符串的形式保存，所以它们都属于字符串类型） 域 field 当前的值或给定的增量 increment
	 * 不能解释(parse)为双精度浮点数(double precision floating point number)
	 * 
	 * @param key
	 * @param field
	 * @param increment
	 * @return 执行加法操作之后 field 域的值
	 */
	public static Double hincrByFloat(String key, String field, double increment) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.hincrByFloat(key, field, increment);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 返回哈希表 key 中的所有域。
	 * 
	 * @param key
	 * @return 一个包含哈希表中所有域的表。 当 key 不存在时，返回一个空表。
	 */
	public static Set<String> hkeys(String key) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.hkeys(key);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 返回哈希表 key 中域的数量。
	 * 
	 * @param key
	 * @return 哈希表中域的数量。当 key 不存在时，返回 0
	 */
	public static Long hlen(String key) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.hlen(key);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 返回哈希表 key 中，一个或多个给定域的值。如果给定的域不存在于哈希表，那么返回一个 nil 值。 因为不存在的 key
	 * 被当作一个空哈希表来处理，所以对一个不存在的 key 进行 HMGET 操作将返回一个只带有 nil 值的表
	 * 
	 * @param key
	 * @param fields
	 * @return 一个包含多个给定域的关联值的表，表值的排列顺序和给定域参数的请求顺序一样。
	 */
	public static List<String> hmget(String key, String... fields) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.hmget(key, fields);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 同时将多个 field-value (域-值)对设置到哈希表 key 中,此命令会覆盖哈希表中已存在的域
	 * 
	 * @param key
	 * @param map
	 * @return 如果命令执行成功，返回 OK 。当 key 不是哈希表(hash)类型时，返回一个错误。
	 */
	public static String hmset(String key, Map<String, String> map) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.hmset(key, map);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 将哈希表 key 中的域 field 的值设为 value 。如果 key 不存在，一个新的哈希表被创建并进行 HSET 操作。如果域 field
	 * 已经存在于哈希表中，旧值将被覆盖。
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return 如果 field 是哈希表中的一个新建域，并且值设置成功，返回 1 。如果哈希表中域 field 已经存在且旧值已被新值覆盖，返回
	 *         0 。
	 */
	public static Long hset(String key, String field, String value) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.hset(key, field, value);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 当且仅当域 field 不存在时， 将哈希表 key 中的域 field 的值设置为 value。 若域 field 已经存在，该操作无效。
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return 设置成功，返回 1 。 如果给定域已经存在且没有操作被执行，返回 0 。
	 */
	public static Long hsetnx(String key, String field, String value) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.hsetnx(key, field, value);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 返回哈希表 key 中所有域的值。
	 * 
	 * @param key
	 * @return 一个包含哈希表中所有值的表。当 key 不存在时，返回一个空表。
	 */
	public static List<String> hvals(String key) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.hvals(key);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 命令用于迭代哈希键中的键值对
	 * 
	 * @param key
	 *            自己填写从0开始,值不确定
	 * @param cursor
	 * @since >=2.8.0
	 * @return ScanResult getStringCursor() 获取返回的游标，如果为0代表结束<br>
	 *         getResult() 返回迭代出的key的集合，默认10条
	 */
	public static ScanResult<Entry<String, String>> hscan(String key, String cursor) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.hscan(key, cursor);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 命令用于迭代哈希键中的键值对
	 * 
	 * @param cursor
	 *            自己填写从0开始,值不确定
	 * @param scanParams
	 *            count 返回多少条记录 match 先迭代，后过滤，可能为空，但返回的游标不为0
	 * @since >=2.8.0
	 * @return ScanResult getStringCursor() 获取返回的游标，如果为0代表结束<br>
	 *         getResult() 返回迭代出的key的集合，默认10条
	 */
	public static ScanResult<Entry<String, String>> hscan(String key, String cursor,
			ScanParams params) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.hscan(key, cursor, params);
		}finally{
			cache.close(jedis);
		}
	}
	/**
	 * 获取keys对应的列表 第一个不为空的头元素 非阻塞行为 当 BLPOP 被调用时，如果给定 key
	 * 内至少有一个非空列表，那么弹出遇到的第一个非空列表的头元素，并和被弹出元素所属的列表的名字一起，组成结果返回给调用者。 阻塞行为 如果所有给定
	 * key 都不存在或包含空列表，那么 BLPOP 命令将阻塞连接，直到等待超时，或有另一个客户端对给定 key 的任意一个执行 LPUSH 或
	 * RPUSH 命令为止。
	 * 
	 * @param keys
	 * @return 如果列表为空，返回一个 nil 。 否则，返回一个含有两个元素的列表，第一个元素是被弹出元素所属的 key
	 *         ，第二个元素是被弹出元素的值。
	 */
	public static List<String> blpop(String... keys) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.blpop(keys);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 获取keys对应的列表 第一个不为空的头元素 非阻塞行为 当 BLPOP 被调用时，如果给定 key
	 * 内至少有一个非空列表，那么弹出遇到的第一个非空列表的头元素，并和被弹出元素所属的列表的名字一起，组成结果返回给调用者。 阻塞行为 如果所有给定
	 * key 都不存在或包含空列表，那么 BLPOP 命令将阻塞连接，直到等待超时，或有另一个客户端对给定 key 的任意一个执行 LPUSH 或
	 * RPUSH 命令为止。
	 * 
	 * @param timeout
	 *            超时时间
	 * @param keys
	 * @return 如果列表为空，返回一个 nil 。 否则，返回一个含有两个元素的列表，第一个元素是被弹出元素所属的 key
	 *         ，第二个元素是被弹出元素的值。
	 */
	public static List<String> blpop(int timeout, String... keys) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.blpop(timeout, keys);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 获取keys对应的列表 第一个不为空的尾元素 当给定多个 key 参数时，按参数 key
	 * 的先后顺序依次检查各个列表，弹出第一个非空列表的尾部元素。
	 * 
	 * @param keys
	 * @return 假如在指定时间内没有任何元素被弹出，则返回一个 nil 和等待时长。
	 *         反之，返回一个含有两个元素的列表，第一个元素是被弹出元素所属的 key ，第二个元素是被弹出元素的值。
	 */
	public static List<String> brpop(String... keys) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.brpop(keys);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 获取keys对应的列表 第一个不为空的尾元素 当给定多个 key 参数时，按参数 key
	 * 的先后顺序依次检查各个列表，弹出第一个非空列表的尾部元素。
	 * 
	 * @param timeout
	 *            超时时间
	 * @param keys
	 * @return 假如在指定时间内没有任何元素被弹出，则返回一个 nil 和等待时长。
	 *         反之，返回一个含有两个元素的列表，第一个元素是被弹出元素所属的 key ，第二个元素是被弹出元素的值。
	 */
	public static List<String> brpop(int timeout, String... keys) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.brpop(timeout, keys);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 非阻塞 将srckey对应列表的尾元素弹出返回客户端，并将该元素放入到dstkey对应列表的头部 如果 srckey 和 dstkey
	 * 相同，则列表中的表尾元素被移动到表头，并返回该元素，可以把这种特殊情况视作列表的旋转(rotation)操作。
	 * 
	 * @param srckey
	 * @param dstkey
	 * @return
	 */
	public static String rpoplpush(String srckey, String dstkey) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.rpoplpush(srckey, dstkey);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * srckey 不为空时和rpoplpush效果一样 srckey为空时，将阻塞连接，直到超时或有另一个客户端对 source 执行 LPUSH 或
	 * RPUSH 命令为止。
	 * 
	 * @param source
	 * @param destination
	 * @param timeout
	 *            接受一个以秒为单位的数字作为值。超时参数设为 0 表示阻塞时间可以无限期延长
	 * @return
	 */
	public static String brpoplpush(String srckey, String dstkey, int timeout) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.brpoplpush(srckey, dstkey, timeout);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 返回key对应的列表对应index索引的值
	 * 
	 * @param key
	 * @param index
	 *            以 0 表示列表的第一个元素 以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素
	 * @return 列表中下标为 index 的元素。
	 */
	public static String lindex(String key, long index) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.lindex(key, index);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 根据where 将value值插入到key对应的列表pivot位置之前或之后
	 * 
	 * @param key
	 * @param where
	 *            BEFORE|AFTER 之前or之后
	 * @param pivot
	 *            要插入值的位置
	 * @param value
	 * @return 如果命令执行成功，返回插入操作完成之后，列表的长度。 如果没有找到 pivot ，返回 -1 。如果 key
	 *         不存在或为空列表，返回 0 。
	 */
	public static Long linsert(String key, LIST_POSITION where, String pivot,
			String value) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.linsert(key, where, pivot, value);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 返回key对应的列表的长度，如果key不是列表类型，返回一个错误
	 * 
	 * @param key
	 * @return 列表长度
	 */
	public static Long llen(String key) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.llen(key);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 移除并返回key对应列表的头元素
	 * 
	 * @param key
	 * @return 列表的头元素 当 key 不存在时，返回 nil
	 */
	public static String lpop(String key) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.lpop(key);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 将一个或多个值插入到key对应的列表的表头，key不存在生成一个空列表并放入
	 * 
	 * @param key
	 * @param values
	 * @return 返回列表长度
	 */
	public static Long lpush(String key, String... values) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.lpush(key, values);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 当key存在并是一个列表时，将values插入表头
	 * 
	 * @param key
	 * @param values
	 * @return 返回列表长度
	 */
	public static Long lpushx(String key, String... values) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.lpushx(key, values);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 返回列表指定区间的元素，包含startIndex和stopIndex
	 * 
	 * @param key
	 * @param startIndex
	 * @param stopIndex
	 * @return
	 */
	public static List<String> lrange(String key, long startIndex, long stopIndex) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.lrange(key, startIndex, stopIndex);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 根据参数 count 的值，移除列表中与参数 value 相等的元素。
	 * 
	 * @param key
	 * @param count
	 *            count > 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count 。 count < 0 :
	 *            从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值 count = 0 : 移除表中所有与
	 *            value 相等的值
	 * @param value
	 * @return 被移除元素的数量。因为不存在的 key 被视作空表(empty list)，所以当 key 不存在时， LREM 命令总是返回 0
	 *         。
	 */
	public static Long lrem(String key, Long count, String value) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.lrem(key, count, value);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 将key对应得列表下标为index的元素的值设置为value 当 index 参数超出范围，或对一个空列表( key 不存在)进行 LSET
	 * 时，返回一个错误。
	 * 
	 * @param key
	 * @param index
	 * @param value
	 * @return 操作成功返回 ok ，否则返回错误信息
	 */
	public static String lset(String key, Long index, String value) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.lset(key, index, value);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 对key对应的列表进行修剪，只保留[startIndex,stopIndex]区间内的值
	 * 
	 * @param key
	 * @param startIndex
	 * @param stopIndex
	 * @return 命令执行成功时，返回 ok
	 */
	public static String ltrim(String key, long startIndex, long stopIndex) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.ltrim(key, startIndex, stopIndex);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 移除并返回列表的尾元素
	 * 
	 * @param key
	 * @return 列表的尾元素。当 key 不存在时，返回 nil 。
	 */
	public static String rpop(String key) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.rpop(key);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 将一个或多个值插入到表尾，如果 key 不存在，一个空列表会被创建并执行 RPUSH 操作。
	 * 
	 * @param key
	 * @param values
	 * @return 执行 RPUSH 操作后，表的长度。
	 */
	public static Long rpush(String key, String... values) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.rpush(key, values);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 将一个或多个值插入到表尾，当 key 不存在时， LPUSHX 命令什么也不做。
	 * 
	 * @param key
	 * @param values
	 * @return RPUSHX 命令执行之后，表的长度
	 */
	public static Long rpushx(String key, String... values) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.rpushx(ASC, values);
		}finally{
			cache.close(jedis);
		}
	}
	/* ######################## List（列表）end ############################### */
	/* ######################## Set（集合）start ############################### */
	/**
	 * 将一个或多个value元素加入到集合中，已存在的元素将被忽略，key不存在创建新的集合
	 * 
	 * @param key
	 * @param values
	 * @return 被添加到集合中的新元素的数量，不包括被忽略的元素
	 */
	public static  Long sadd(String key, String... values) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.sadd(ASC, values);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 返回集合的size
	 * 
	 * @param key
	 * @return 集合的size，key不存在返回0
	 */
	public static Long scard(String key) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.scard(key);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 返回 第一个集合不在后面集合中的值
	 * 
	 * @param keys
	 * @return 一个包含差集成员的列表
	 */
	public static Set<String> sdiff(String... keys) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.sdiff(keys);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 将keys的差集保存到dstkey集合中
	 * 
	 * @param dstkey
	 * @param keys
	 * @return 结果集中的元素数量。
	 */
	public static Long sdiffstore(String dstkey, String... keys) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.sdiffstore(dstkey, keys);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 返回keys集合的交集
	 * 
	 * @param keys
	 * @return 交集成员的列表
	 */
	public static Set<String> sinter(String... keys) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.sinter(keys);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 将keys集合的交集保存到dstkey中，并返回长度
	 * 
	 * @param dstkey
	 * @param keys
	 * @return
	 */
	public static Long sinterstore(String dstkey, String... keys) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.sinterstore(dstkey, keys);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 判断member是否是key对应的集合的成员
	 * 
	 * @param key
	 * @param member
	 * @return
	 */
	public static Boolean sismember(String key, String member) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.sismember(key, member);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 返回key对应的集合，key不存在返回空集合
	 * 
	 * @param key
	 * @return
	 */
	public static Set<String> smembers(String key) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.smembers(key);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 将member从srckey对应的集合移动到dstkey对应的集合，原子操作
	 * 当dstkey已经有member时，只会将srckey的member删掉
	 * 
	 * @param srckey
	 * @param dstkey
	 * @param member
	 * @return 如果 member 元素被成功移除，返回 1 。如果 member 元素不是 source 集合的成员，并且没有任何操作对
	 *         destination 集合执行，那么返回 0 。
	 */
	public static Long smove(String srckey, String dstkey, String member) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.smove(srckey, dstkey, member);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 移除并返回集合中的一个随机元素
	 * 
	 * @param key
	 * @return
	 */
	public static String spop(String key) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.spop(key);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 随机返回并移除count个元素
	 * 
	 * @param key
	 * @param count
	 * @return
	 */
	public static Set<String> spop(String key, long count) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.spop(key, count);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 随机返回集合的一个元素
	 * 
	 * @param key
	 * @return
	 */
	public static String srandmember(String key) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.srandmember(key);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 随机返回集合的count个元素
	 * 
	 * @param key
	 * @param count
	 *            如果 count 为正数，且小于集合基数，那么命令返回一个包含 count 个元素的数组，数组中的元素各不相同。如果
	 *            count 大于等于集合基数，那么返回整个集合。 如果 count
	 *            为负数，那么命令返回一个数组，数组中的元素可能会重复出现多次，而数组的长度为 count 的绝对值
	 * @return
	 */
	public static List<String> srandmember(String key, int count) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.srandmember(key, count);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 移除集合中的一个或多个member元素，不存在的member元素会忽略
	 * 
	 * @param key
	 * @param members
	 * @return 被成功移除的元素的数量，不包括被忽略的元素。
	 */
	public static Long srem(String key, String... members) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.srem(key, members);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 返回所有集合的并集，不存在的key被视为空集
	 * 
	 * @param keys
	 * @return 并集成员的列表
	 */
	public static Set<String> sunion(String... keys) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.sunion(keys);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 将keys所有集合的并集存储到dstkey对应的集合，如果dstkey存在则覆盖
	 * 
	 * @param dstkey
	 * @param keys
	 * @return 结果集中的元素数量
	 */
	public static Long sunionstore(String dstkey, String... keys) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.sunionstore(dstkey, keys);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 命令用于迭代集合键中的元素
	 * 
	 * @param key
	 *            自己填写从0开始,值不确定
	 * @param cursor
	 * @return ScanResult getStringCursor() 获取返回的游标，如果为0代表结束<br>
	 *         getResult() 返回迭代出的key的集合，默认10条
	 */
	public static ScanResult<String> sscan(String key, String cursor) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.sscan(key, cursor);
		}finally{
			cache.close(jedis);
		}
	}

	/**
	 * 命令用于迭代集合键中的元素
	 * 
	 * @param cursor
	 *            自己填写从0开始,值不确定
	 * @param scanParams
	 *            count 返回多少条记录 match 先迭代，后过滤，可能为空，但返回的游标不为0
	 * @return ScanResult getStringCursor() 获取返回的游标，如果为0代表结束<br>
	 *         getResult() 返回迭代出的key的集合，默认10条
	 */
	public static ScanResult<String> sscan(String key, String cursor, ScanParams params) {
		Jedis jedis = cache.getJedis();
		try {
			return jedis.sscan(key, cursor, params);
		}finally{
			cache.close(jedis);
		}
	}

	/* ######################## Set（集合）end ############################### */
}
