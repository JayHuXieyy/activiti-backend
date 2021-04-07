package com.huafagroup.common.core.redis;

import com.alibaba.fastjson.JSONObject;
import com.huafagroup.common.constant.Constants;
import com.huafagroup.common.utils.DateUtils;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * spring redis 工具类
 *
 * @author huafagroup
 **/
@SuppressWarnings(value = {"unchecked", "rawtypes"})
@Component
public class RedisCache {
    @Autowired
    public RedisTemplate redisTemplate;

    @Autowired
    public RedissonClient redisson;

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    public boolean deleteObject(final String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return
     */
    public long deleteObject(final Collection collection) {
        return redisTemplate.delete(collection);
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> long setCacheList(final String key, final List<T> dataList) {
        Long count = redisTemplate.opsForList().rightPushAll(key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> long setCacheSet(final String key, final Set<T> dataSet) {
        Long count = redisTemplate.opsForSet().add(key, dataSet);
        return count == null ? 0 : count;
    }

    /**
     * 获得缓存的set
     *
     * @param key
     * @return
     */
    public <T> Set<T> getCacheSet(final String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap) {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(key, dataMap);
        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public <T> Map<String, T> getCacheMap(final String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    public <T> void setCacheMapValue(final String key, final String hKey, final T value) {
        redisTemplate.opsForHash().put(key, hKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T getCacheMapValue(final String key, final String hKey) {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(key, hKey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys) {
        return redisTemplate.opsForHash().multiGet(key, hKeys);
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(final String pattern) {
        return redisTemplate.keys(pattern);
    }


    /**
     * scan 实现
     *
     * @param pattern  表达式
     * @param consumer 对迭代到的key进行操作
     */
    public void scan(String pattern, Consumer<byte[]> consumer) {
        this.redisTemplate.execute((RedisConnection connection) -> {
            try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().count(Long.MAX_VALUE).match(pattern).build())) {
                cursor.forEachRemaining(consumer);
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 获取符合条件的key
     *
     * @param pattern 表达式
     * @return
     */
    public List<String> scanKeys(String pattern) {
        List<String> keys = new ArrayList<>();
        this.scan(pattern, item -> {
            //符合条件的key
            String key = new String(item, StandardCharsets.UTF_8);
            keys.add(key);
        });
        return keys;
    }

    /**
     * 生成号码
     */
    public String generateNumber(String key, String name) {
        key = Constants.CALL_QUEUE + key;
        // 设置递增因子
        Long count = redisTemplate.opsForValue().increment(key, 1);
        if (redisTemplate.opsForValue().getOperations().getExpire(key).equals(-1)) {
            //当天时间
            Date date = new Date();
            //当天零点
            date = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
            //第二天零点
            date = DateUtils.addDays(date, +1);
            redisTemplate.expireAt(key, date);
        }
        System.out.println("递增的值" + count);
        Format f1 = new DecimalFormat("000");
        //将1变为001
        return name + f1.format(count);
    }

    /**
     * 放入zset
     */
/*    public void putWaitNumber(String key,String number,double score){
        key = Constants.CALL_QUEUE +":"+ key;
        redisTemplate.opsForZSet().add(key,number,score);
        //当天时间
        Date date = new Date();
        //当天零点
        date = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
        //第二天零点
        date = DateUtils.addDays(date, +1);
        redisTemplate.expireAt(key,date);
    }*/

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
/*    public String getWaitNumber(String key) {
        key = Constants.CALL_QUEUE +":"+ key;
        //获取对象实例
        ZSetOperations<String, String> opsForZSet = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> reverseRangeWithScores = opsForZSet.rangeWithScores(key, 0, -1);

        List<ZSetOperations.TypedTuple<String>> list=new ArrayList(reverseRangeWithScores);
        opsForZSet.remove(key,list.get(0).getValue());
        return list.get(0).getValue();
    }*/
    public String getWaitNumber(String key) {
        key = Constants.CALL_QUEUE  + key;
        RScoredSortedSet<String> set = redisson.getScoredSortedSet(key);
        System.out.println("是我排队列表:"+set.first() + JSONObject.toJSONString(set.toArray()));
        //set.pollFirst();
        //set.pollLast();
        //int index = set.rank(new SomeObject(g, d)); // 获取元素在集合中的位置
        //Double score = set.getScore(new SomeObject(g, d)); // 获取元素的评分
        System.out.println("rank"+set.rank(key));
        return set.pollFirst();
    }

    /**
     * 放入zset
     */
    public void putWaitNumber(String key, String number, double score) {
        key = Constants.CALL_QUEUE + key;
        RScoredSortedSet<String> set = redisson.getScoredSortedSet(key);
        set.add(score, number);
    }

    /**
     * 获取zset长度
     */
    public List getWaitNumberSize(String key) {
        key = Constants.CALL_QUEUE + key;
        RScoredSortedSet<String> set = redisson.getScoredSortedSet(key);
        List list=Arrays.asList(set.stream().toArray());
//        List arrayList=new ArrayList(list);
        return list;
    }

    //插队 yType为“Y”+eventType
    public void jumpInLine(String key, String number,String yType) {
        key = Constants.CALL_QUEUE + key;
        RScoredSortedSet<String> set = redisson.getScoredSortedSet(key);
        System.out.println(set.first() + JSONObject.toJSONString(set.toArray()));
        //set.pollFirst();
        //set.pollLast();
        //int index = set.rank(new SomeObject(g, d)); // 获取元素在集合中的位置
        List list=Arrays.asList(set.stream().toArray());
        Double score=null;
        Double temp;
        if(list!=null && list.size()>2){
            //从第二个开始判断是否预约号码
            for(int i=1;i<list.size();i++){
                if(!list.get(i).toString().contains(yType)){
                    //i元素不是预约号码，开始插队
                    if(i>1){//轮询到第三个以后的号码,插到前面
                        score=set.getScore(list.get(i).toString())-1;
                        temp=set.getScore(list.get(i-1).toString());
                        if(Double.doubleToLongBits(score)==Double.doubleToLongBits(temp)){
                            //如果分数等于预约号码评分，则拿后一位号码评分
                            score=set.getScore(list.get(i).toString());
                        }
                    }else {
                        //i处于第二个，插在后排
                        score=set.getScore(list.get(i).toString())+1;
                    }
                    break;
                }
            }
        }else {
            score=Double.valueOf(System.currentTimeMillis());
        }
        set.add(score, number);

    }

    /**
     * 获取key的过期时间
     * */
    public Long getExpire(String key){
        return redisTemplate.opsForValue().getOperations().getExpire(key);//此方法返回单位为秒过期时长
    }

}
