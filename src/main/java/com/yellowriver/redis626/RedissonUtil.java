package com.yellowriver.redis626;


import com.yellowriver.redis626.form.RHashRequest;
import lombok.NonNull;
import org.redisson.api.*;
import org.redisson.client.codec.Codec;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author huanghe
 * @create 2022/2/17 2:42 PM
 */
@DependsOn("redissonClient")
@Component
public class RedissonUtil implements DisposableBean {

    private static final Codec STRING_CODEC = new StringCodec("UTF-8");

    @Autowired
    RedissonClient redissonClient;

    public List<String> listKeys() {
        RKeys rKeys = this.redissonClient.getKeys();
        Iterable<String> keys = rKeys.getKeys();
        List<String> keysList = new ArrayList<>();
        for (String key : keys) {
            keysList.add(key);
        }
        return keysList;
    }

    public List<String> listKeysByPattern(String prefix) {
        Iterable<String> keysByPattern = this.redissonClient.getKeys().getKeysByPattern(prefix);
        List<String> keys = new ArrayList<>();
        for (String key : keysByPattern) {
            keys.add(key);
        }
        return keys;
    }

    public long deleteKeys(String... key) {
        RKeys keys = this.redissonClient.getKeys();
        return keys.delete(key);
    }

    /**
     * k v
     *
     * @param key     key
     * @param value   value
     * @param seconds timeToLive
     * @return .
     */
    public boolean set(String key, Object value, int seconds) {
        RBucket<Object> bucket = this.redissonClient.getBucket(key);
        if (seconds > 0) {
            return bucket.trySet(value, seconds, TimeUnit.SECONDS);
        } else {
            return bucket.trySet(value);
        }
    }


    public void set(String key, Object value) {
        set(key, value, -1);
    }

    public Object get(String key) {
        RBucket<Object> bucket = this.redissonClient.getBucket(key);
        return bucket.get();
    }


    public boolean hashSet(RHashRequest request) {
        String mapName = request.getName();
        String key = request.getKey();
        Object body = request.getBody();
        int expiry = request.getExpiry();
        RMap<Object, Object> rMap = redissonClient.getMap(mapName);
        if (expiry > 0) {
            rMap.fastPut(key, body);
            return rMap.expire(expiry, TimeUnit.SECONDS);
        } else {
            return rMap.fastPut(key, body);
        }
    }


    /**
     * @param typeKey 集合名称
     * @param itemKey 集合元素名称
     * @return
     */
    public Object hashGet(String typeKey, String itemKey) {
        RMap<Object, Object> rMap = redissonClient.getMap(typeKey);
        return rMap.get(itemKey);
    }

    public long hashDelete(String typeKey, String... itemKeys) {
        RMap<Object, Object> rMap = redissonClient.getMap(typeKey);
        return rMap.fastRemove(itemKeys);
    }

    public String getString(String key) {
        RBucket<String> bucket = this.redissonClient.getBucket(key, new StringCodec("UTF-8"));
        return bucket.get();
    }

    /**
     * member相当于 相机点位的cameraId
     *
     * @param typeKey  位置集名称 如萧山 余杭
     * @param geoEntry 保存 longitude 经度 latitude 纬度 以及成员Key member
     * @return success
     */
    public boolean geoAdd(String typeKey, GeoEntry geoEntry) {
        RGeo<Object> rGeo = redissonClient.getGeo(typeKey);
        return rGeo.add(geoEntry.getLongitude(), geoEntry.getLatitude(), geoEntry.getMember().toString()) > 0;
    }

    /**
     * 返回两个给定位置之间的距离
     *
     * @param typeKey      集合名称
     * @param firstMember  第一个成员
     * @param secondMember 第二个成员
     * @param geoUnit      返回距离单位 米、公里等
     * @return
     */
    public Double geoDist(String typeKey, String firstMember, String secondMember, GeoUnit geoUnit) {
        RGeo<Object> geo = redissonClient.getGeo(typeKey);

        return geo.dist(firstMember, secondMember, geoUnit);
    }

    /**
     * 从key里返回所有给定位置元素的位置（经度和纬度）
     *
     * @param typeKey 集合名称
     * @param members 成员
     * @return 成员名作为Key 的map
     */
    public Map<Object, GeoPosition> geoGet(String typeKey, String... members) {
        RGeo<Object> geo = redissonClient.getGeo(typeKey);
        return geo.pos(members);
    }

    /**
     * 以给定的经纬度为中心， 找出某一半径内的元素
     *
     * @param typeKey   集合名称
     * @param longitude 经度
     * @param latitude  纬度
     * @param radius    半径
     * @param geoUnit   单位
     * @return
     */
    public List<Object> geoRadius(
            String typeKey, double longitude, double latitude, double radius, GeoUnit geoUnit) {
        RGeo<Object> geo = redissonClient.getGeo(typeKey);
        return geo.radius(longitude, latitude, radius, geoUnit);
    }

    public Long atomicGet(String atomicKey) {
        RAtomicLong atomicLong = redissonClient.getAtomicLong(atomicKey);
        return atomicLong.get();
    }

    public Long atomicGetAndAdd(String atomicKey, long value) {
        RAtomicLong atomicLong = redissonClient.getAtomicLong(atomicKey);
        return atomicLong.getAndAdd(value);
    }

    public Long atomicAddAndGet(String atomicKey, long value) {
        RAtomicLong atomicLong = redissonClient.getAtomicLong(atomicKey);
        return atomicLong.addAndGet(value);
    }

    /**
     * lock(), 拿不到lock就不罢休，不然线程就一直block
     */
    public RLock lock(@NonNull String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    /**
     * @param bloomTypeKey 根据业务场景命名
     * @param dataKey      需要计算是否存在的key
     * @return 数据是否存在
     */
    public boolean bloomFilterContains(String bloomTypeKey, String dataKey) {
        return redissonClient.getBloomFilter(bloomTypeKey).contains(dataKey);
    }

    public boolean bloomFilterAdd(String bloomTypeKey, String dataKey) {
        ArrayList arrayList = new ArrayList<Object>();
        return redissonClient.getBloomFilter(bloomTypeKey).add(dataKey);
    }

    /**
     * 环形缓存区,队列
     *
     * @param key
     * @param capacity
     */
    public void createRingBuffer(String key, int capacity) {
        RRingBuffer<Object> ringBuffer = redissonClient.getRingBuffer(key);
        ringBuffer.setCapacity(capacity);
    }

    /**
     * 环形缓存区,队列
     *
     * @param key
     * @return null or element
     */
    public <E> E ringBufferGet(String key) {
        RRingBuffer<E> ringBuffer = redissonClient.getRingBuffer(key);
        return ringBuffer.poll();
    }

    /**
     * 环形缓存区,队列
     *
     * @param key
     */
    public <E> boolean ringBufferSet(String key, E e) {
        RRingBuffer<E> ringBuffer = redissonClient.getRingBuffer(key);
        return ringBuffer.offer(e);
    }

    @Override
    public void destroy() throws Exception {
        redissonClient.shutdown();
    }


}
