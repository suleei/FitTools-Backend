package com.oft.fittools.security;

import com.oft.fittools.po.UserBasicInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
@Data
@AllArgsConstructor
public class BasicUserDetails implements UserDetails {
    private UserBasicInfo userBasicInfo;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return userBasicInfo.getPassword();
    }

    @Override
    public String getUsername() {
        return userBasicInfo.getUsername();
    }
}
