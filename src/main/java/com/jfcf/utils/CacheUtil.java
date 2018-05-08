package com.jfcf.utils;

import com.jfinal.kit.PropKit;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * 缓存统一入口工具类
 * getStr/setStr 如果开启了redis配置，将用redis
 */
public  class CacheUtil {
	/**
	 * 缓存域，默认为jfcf(玖富消金)，使用redis时，统一
	 */
	public static String CACHE_DOMAIN = PropKit.get("jfcf");
	public  final static String CACHE_TYPE="ehcache";

	/**
	 * 系统级的缓存 长久
	 */
	public  final static String CACHE_NAME="system";
	/**
	 * 30秒缓存
	 */
	public  final static String CACHE_NAME_SNAP_TIME_30="SNAP_TIME_30";
	/**
	 * 60秒缓存
	 */
	public  final static String CACHE_NAME_SNAP_TIME_60="SNAP_TIME_60";
	/**
	 * 120秒缓存
	 */
	public  final static String CACHE_NAME_SNAP_TIME_120="SNAP_TIME_120";
	/**
	 * 180秒缓存
	 */
	public  final static String CACHE_NAME_SNAP_TIME_180="SNAP_TIME_180";
	/**
	 * 300秒缓存
	 */
	public  final static String CACHE_NAME_SNAP_TIME_300="SNAP_TIME_300";
	/**
	 * 600秒缓存
	 */
	public  final static String CACHE_NAME_SNAP_TIME_600="SNAP_TIME_600";
	/**
	 * 1800秒缓存
	 */
	public  final static String CACHE_NAME_SNAP_TIME_1800="SNAP_TIME_1800";
	/**
	 * 一天缓存
	 */
	public  final static String CACHE_NAME_SNAP_TIME_129600="SNAP_TIME_129600";
	/**
	 * 白名单缓存key
	 */
	public  final static String CACHE_KEY_TRANSIT="SYSTEM_KEY_TRANSIT";

	/**
	 * 临时保存30秒
	 */
	public static final int SNAP_TIME_30 = 30;
	/**
	 * 临时保存60秒
	 */
	public static final int SNAP_TIME_60 = 60;
	/**
	 * 临时保存120秒
	 */
	public static final int SNAP_TIME_120 = 120;
	/**
	 * 临时保存180秒
	 */
	public static final int SNAP_TIME_180 = 180;
	/**
	 * 临时保存300秒
	 */
	public static final int SNAP_TIME_300 = 300;
	/**
	 * 临时保存600秒
	 */
	public static final int SNAP_TIME_600 = 600;
	/**
	 * 临时保存1800秒
	 */
	public static final int SNAP_TIME_1800 = 1800;
	/**
	 * 临时保存一小时
	 */
	public static final int SNAP_TIME_3600 = 3600;
	/**
	 * 临时保存一天
	 */
	public static final int SNAP_TIME_129600 = 129600;
	/**
     * 按规则生成key
     * @author ducongcong
     * @createDate 2016年7月20日
     * @updateDate 
     * @param prefix 加上前置，使用手机号类似的key的时候保持唯一
     * @param key
     * @return
     */
	public static String getKey(String prefix,String key){
		StringBuilder sb = new StringBuilder();
		sb.append(PropKit.get(ConstantConfig.CONFIG_REDIS_CACHENAME,CACHE_DOMAIN));
		if(StringUtils.isNotEmpty(prefix)){
			sb.append("_").append(prefix);
		}
		sb.append("_").append(key);
		return sb.toString();
	}
	/**
	 * 按规则生成缓存key
	 * @author ducongcong
	 * @createDate 2016年7月21日
	 * @updateDate 
	 * @param key
	 * @return
	 */
	public static String getKey(String key){
		return getKey(null, key);
	}

	/**
	 * 缓存类型
	 */
	private static String cacheType;
	
	/**
	 * 获取缓存类型
	 * @return
	 */
	public static String getCacheType(){
		if(cacheType == null){
			cacheType = CACHE_TYPE;
		}
		return cacheType;
	}
	/**
	 * 根据sqlId 获取缓存中的sql
	 * @author ducongcong
	 * @createDate 2016年7月20日
	 * @updateDate 
	 * @param sqlId
	 * @return
	 */
	public static <T> T getSql(String sqlId){
		return CacheKit.get(CACHE_NAME, sqlId);
	}
	/**
	 * 将xml中的sql按对应的SqlId存储到缓存中
	 * @author ducongcong
	 * @createDate 2016年7月20日
	 * @updateDate 
	 * @param sqlId
	 * @param value
	 */
	public static  void setSql(Object sqlId, String value) {
		CacheKit.put(CACHE_NAME, sqlId, value);
	}
	/**
	 * 获取缓存 ehcache专用
	 * @param key
	 * @param second
	 * @return
	 * @author ducongcong
	 * @createDate 2016年8月2日
	 * @updateDate
	 */
	public static <T> T get(String key,Integer second){
		if(null == second  ){
			return CacheKit.get(CACHE_NAME, key);
		}else if(SNAP_TIME_30 == second){
			return CacheKit.get(CACHE_NAME_SNAP_TIME_30, key);
		}else if(SNAP_TIME_60 == second){
			return CacheKit.get(CACHE_NAME_SNAP_TIME_60, key);
		}else if(SNAP_TIME_120 == second){
			return CacheKit.get(CACHE_NAME_SNAP_TIME_120, key);
		}else if(SNAP_TIME_180 == second){
			return CacheKit.get(CACHE_NAME_SNAP_TIME_180, key);
		}else if(SNAP_TIME_300 == second){
			return CacheKit.get(CACHE_NAME_SNAP_TIME_300, key);
		}else if(SNAP_TIME_600 == second){
			return CacheKit.get(CACHE_NAME_SNAP_TIME_600, key);
		}else if(SNAP_TIME_1800 == second){
			return CacheKit.get(CACHE_NAME_SNAP_TIME_1800, key);
		}else if(SNAP_TIME_129600 == second){
			return CacheKit.get(CACHE_NAME_SNAP_TIME_129600, key);
		}else{
			return null;
		}
	}
	/**
	 * 获取缓存 ehcache专用
	 * @param key
	 * @return
	 */
	public static <T> T get(String key) {
		if( CacheKit.get(CACHE_NAME, key) != null){
			return CacheKit.get(CACHE_NAME, key);
		}else if(CacheKit.get(CACHE_NAME_SNAP_TIME_30, key) != null){
			return CacheKit.get(CACHE_NAME_SNAP_TIME_30, key);
		}else if(CacheKit.get(CACHE_NAME_SNAP_TIME_60, key) != null){
			return CacheKit.get(CACHE_NAME_SNAP_TIME_60, key);
		}else if(CacheKit.get(CACHE_NAME_SNAP_TIME_120, key) != null){
			return CacheKit.get(CACHE_NAME_SNAP_TIME_120, key);
		}else if(CacheKit.get(CACHE_NAME_SNAP_TIME_180, key) != null){
			return CacheKit.get(CACHE_NAME_SNAP_TIME_180, key);
		}else if(CacheKit.get(CACHE_NAME_SNAP_TIME_300, key) != null){
			return CacheKit.get(CACHE_NAME_SNAP_TIME_300, key);
		}else if(CacheKit.get(CACHE_NAME_SNAP_TIME_600, key) != null){
			return CacheKit.get(CACHE_NAME_SNAP_TIME_600, key);
		}else if(CacheKit.get(CACHE_NAME_SNAP_TIME_1800, key) != null){
			return CacheKit.get(CACHE_NAME_SNAP_TIME_1800, key);
		}else if(CacheKit.get(CACHE_NAME_SNAP_TIME_129600, key) != null){
			return CacheKit.get(CACHE_NAME_SNAP_TIME_129600, key);
		}
		return null;
	}
	/**
	 * 获取缓存,为了兼容redis，会将数据保存成string类型
	 * @param key 缓存key
	 * @return
	 */
	public static String getStr(String key) {
		return getStr(null, key);
	}
	
	/**
	 * 将 key 中储存的数字值增一。如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR
	 * 操作。如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。 本操作的值限制在 64 位(bit)有符号数字表示之内。
	 * 
	 * @param key
	 * @return 执行 INCR 命令之后 key 的值。
	 */
	public static Long incr(String key) {
		key = getKey(key);
		return JedisUtil.incr(key);
	}
	
	/**
	 * 获取缓存,为了兼容redis，会将数据保存成string类型
	 * @param module 表名或者功能名
	 * @param key 缓存key
	 * @return 缓存的json字符串
	 */
	public static String getStr(String module,String key) {
		key = getKey(module,key);
		//redis开关开启后，将缓存切换到redis上
		if(PropKit.getBoolean(ConstantConfig.CONFIG_REDIS_SWITCH, false)){
			return JedisUtil.get(key);
		}else{
			return get(key);
		}
	}
	/**
	 * 设置缓存 只在ehcache中使用
	 * @param key
	 * @param value
	 */
	public static void set(Object key, Object value) {
		CacheKit.put(CACHE_NAME, key, value);
	}
	/**
	 * 设置缓存 ehcache 专用
	 * @param key
	 * @param value
	 * @param second
	 * @author ducongcong
	 * @updateDate
	 */
	public static void set(Object key, Object value,Integer second) {
		if(null == second  ){
			CacheKit.put(CACHE_NAME_SNAP_TIME_1800, key, value);
		}else if(SNAP_TIME_30 == second){
			CacheKit.put(CACHE_NAME_SNAP_TIME_30, key, value);
		}else if(SNAP_TIME_60 == second){
			CacheKit.put(CACHE_NAME_SNAP_TIME_60, key, value);
		}else if(SNAP_TIME_120 == second){
			CacheKit.put(CACHE_NAME_SNAP_TIME_120, key, value);
		}else if(SNAP_TIME_180 == second){
			CacheKit.put(CACHE_NAME_SNAP_TIME_180, key, value);
		}else if(SNAP_TIME_300 == second){
			CacheKit.put(CACHE_NAME_SNAP_TIME_300, key, value);
		}else if(SNAP_TIME_600 == second){
			CacheKit.put(CACHE_NAME_SNAP_TIME_600, key, value);
		}else if(SNAP_TIME_1800 == second){
			CacheKit.put(CACHE_NAME_SNAP_TIME_1800, key, value);
		}else if(SNAP_TIME_129600 == second){
			CacheKit.put(CACHE_NAME_SNAP_TIME_129600, key, value);
		}else{
			CacheKit.put(CACHE_NAME_SNAP_TIME_30, key, value);
		}
	}
	/**
	 * 设置缓存，兼容redis
	 * @param key  设置缓存key
	 * @param value 设置缓存值
	 * @param second 设置过期时间 
	 *     例如 CacheUtil.SNAP_TIME_60
	 */
	public static void setStr(String key, String value,Integer second) {
		setStr(null, key, value, second);
	}
	/**
	 * 设置缓存 ，兼容redis
	 * @param module 表名或者功能名
	 * @param key  设置缓存key
	 * @param value 设置缓存值（json字符串）  使用FastJsonUtils.toJson()
	 * @param second 设置过期时间
	 */
	public static void setStr(String module,String key, String value,Integer second) {
		key = getKey(module,key);
		//redis开关开启后，将缓存切换到redis上
		if(PropKit.getBoolean(ConstantConfig.CONFIG_REDIS_SWITCH, false)){
			if(null == second){
				JedisUtil.set(key, value);
			}else{
				JedisUtil.setex(key, second, value);
			}
		}else{
			set(key, value, second);
		}
	}
	/**
	 * 只在ehcache中使用
	 * @author ducongcong
	 * @createDate 2016年7月21日
	 * @updateDate 
	 * @param key
	 * @return
	 */
	public static <T> T getSession(Object key) {
		return CacheKit.get(CACHE_NAME_SNAP_TIME_1800, key);
	}
	
	
	/**
	 * 用户session保存
	 * @author ducongcong
	 * @createDate 2016年6月23日
	 * @updateDate 
	 * @param key
	 * @param value
	 */
	public static void setSession(Object key, Object value) {
		CacheKit.put(CACHE_NAME_SNAP_TIME_1800, key, value);
	}
	/**
	 * 删除缓存
	 * @param key
	 */
	public static void remove(Object key){
		CacheKit.remove(CACHE_NAME, key);
	}
	public static void remove(Object key,Integer second){
		if(null == second  ){
			CacheKit.remove(CACHE_NAME, key);
		}else if(SNAP_TIME_30 == second){
			CacheKit.remove(CACHE_NAME_SNAP_TIME_30, key);
		}else if(SNAP_TIME_60 == second){
			CacheKit.remove(CACHE_NAME_SNAP_TIME_60, key);
		}else if(SNAP_TIME_120 == second){
			CacheKit.remove(CACHE_NAME_SNAP_TIME_120, key);
		}else if(SNAP_TIME_180 == second){
			CacheKit.remove(CACHE_NAME_SNAP_TIME_180, key);
		}else if(SNAP_TIME_300 == second){
			CacheKit.remove(CACHE_NAME_SNAP_TIME_300, key);
		}else if(SNAP_TIME_600 == second){
			CacheKit.remove(CACHE_NAME_SNAP_TIME_600, key);
		}else if(SNAP_TIME_1800 == second){
			CacheKit.remove(CACHE_NAME_SNAP_TIME_1800, key);
		}else if(SNAP_TIME_129600 == second){
			CacheKit.remove(CACHE_NAME_SNAP_TIME_129600, key);
		}
	}
	/**
	 * 删除缓存，兼容redis
	 * @author ducongcong
	 * @createDate 2016年7月21日
	 * @updateDate 
	 * @param key
	 */
	public static void removeStr(String key){
		removeStr(null,key);
	}
	/**
	 * 删除缓存，兼容redis
	 * @author ducongcong
	 * @createDate 2016年7月21日
	 * @updateDate 
	 * @param module 表名或者功能名
	 * @param key 
	 */
	public static void removeStr(String module,String key){
		key = getKey(module,key);
		//redis开关开启后，将缓存切换到redis上
		if(PropKit.getBoolean(ConstantConfig.CONFIG_REDIS_SWITCH, false)){
			JedisUtil.del(key);
		}else{
			CacheKit.remove(CACHE_NAME, key);
			CacheKit.remove(CACHE_NAME_SNAP_TIME_30, key);
			CacheKit.remove(CACHE_NAME_SNAP_TIME_60, key);
			CacheKit.remove(CACHE_NAME_SNAP_TIME_120, key);
			CacheKit.remove(CACHE_NAME_SNAP_TIME_180, key);
			CacheKit.remove(CACHE_NAME_SNAP_TIME_300, key);
			CacheKit.remove(CACHE_NAME_SNAP_TIME_600, key);
			CacheKit.remove(CACHE_NAME_SNAP_TIME_1800, key);
			CacheKit.remove(CACHE_NAME_SNAP_TIME_129600, key);
		}
	}
	/**
	 * 清除session缓存
	 * @author ducongcong
	 * @createDate 2016年6月24日
	 * @updateDate 
	 * @param key
	 */
	public static void removeSession(Object key){
		CacheKit.remove(CACHE_NAME_SNAP_TIME_180, key);
	}
	/**
	 * 删除缓存
	 * @author ducongcong
	 * @createDate 2016年6月23日
	 * @updateDate 
	 * @param cacheName
	 * @param key
	 */
	public static void remove(String cacheName,Object key){
		CacheKit.remove(cacheName, key);
	}
	
}
