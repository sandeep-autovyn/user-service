package com.user.management.service;

import com.user.management.entity.RandomUserApiData;
import org.springframework.stereotype.Service;

@Service
public interface RandomUserGenerationService {

    RandomUserApiData generateRandomUsers(int count);
}

