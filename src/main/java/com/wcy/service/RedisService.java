package com.wcy.service;

import org.springframework.stereotype.Service;

@Service
public interface RedisService {
    /**
     * 存储缓存
     * @param key
     * @param value
     * @param second   有效期限
     */
    void set(String key, Object value,long second);

    /**
     * 获取缓存
     * @param key
     * @return
     */
    Object get(String key);

    /**
     * 是否存在
     * @param key
     * @return
     */
    boolean exists(String key);

    /**
     * 删除缓存
     * @param key
     * @return
     */
    boolean del(String key);

    void increment(String countkey);
}
