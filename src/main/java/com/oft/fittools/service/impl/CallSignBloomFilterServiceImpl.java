package com.oft.fittools.service.impl;

import com.oft.fittools.service.CallSignBloomFilterService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CallSignBloomFilterServiceImpl implements CallSignBloomFilterService {
    private final RedissonClient redissonClient;
    private RBloomFilter<String> bloomFilter;

    @PostConstruct
    public void init() {
        bloomFilter = redissonClient.getBloomFilter("CallSignBloomFilter");
        bloomFilter.tryInit(190000, 0.01);
    }

    @Override
    public void add(String value) {
        bloomFilter.add(value);
    }

    @Override
    public boolean contains(String value) {
        return bloomFilter.contains(value);
    }
}
