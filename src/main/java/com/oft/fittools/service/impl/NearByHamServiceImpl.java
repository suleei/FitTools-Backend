package com.oft.fittools.service.impl;

import com.oft.fittools.service.NearByHamService;
import org.springframework.stereotype.Service;

@Service
public class NearByHamServiceImpl implements NearByHamService {
    @Override
    public boolean getActiveStatus() {
        return false;
    }
}
