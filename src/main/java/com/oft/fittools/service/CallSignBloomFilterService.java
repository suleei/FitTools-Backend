package com.oft.fittools.service;

public interface CallSignBloomFilterService {

    public void add(String value);

    public boolean contains(String value);
}
