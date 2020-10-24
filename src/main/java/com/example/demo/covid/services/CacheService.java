package com.example.demo.covid.services;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class CacheService {
    // TODO use Redis
    private final Map<String, Object> CACHE = new HashMap<>();

    public Object getFromCache(String key) {
        return CACHE.get(key);
    }

    public void storeInCache(String key, Object value) {
        CACHE.putIfAbsent(key, value);
    }

    public String calculateKey(Collection<String> list) {
        StringBuilder builder = new StringBuilder();
        list.forEach(builder::append);
        return String.valueOf(builder.toString().hashCode());
    }
}
