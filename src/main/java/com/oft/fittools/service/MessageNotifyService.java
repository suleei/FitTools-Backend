package com.oft.fittools.service;

import java.util.Set;

public interface MessageNotifyService {

    void notify(String user_call_sign, String guest_call_sign);

    void confirm(String targetCallSign);

    Set<String> getGuestSet();

    Long getNotify();
}
