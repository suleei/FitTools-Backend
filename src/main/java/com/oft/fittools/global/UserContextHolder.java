package com.oft.fittools.global;

import com.oft.fittools.po.User;

public class UserContextHolder {
    private static final ThreadLocal<User> user = new ThreadLocal<>();

    public static void setUser(User user) {
        UserContextHolder.user.set(user);
    }

    public static User getUser() {
        return user.get();
    }

    public static void clearUser() {
        user.remove();
    }
}
