package com.oft.fittools.service;

public interface LogConfirmNotifyService {

    public void notify(String call_sign);

    String getConfirmLog();


    void deleteConfirmLog();
}
