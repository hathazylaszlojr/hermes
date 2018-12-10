package com.hermes.core.service;

import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TimeService {

    public Long getCurrentUtcEpochMilli() {
        return Instant.now().toEpochMilli();
    }
}
