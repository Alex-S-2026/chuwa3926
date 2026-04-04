package com.chuwa.hw7.demo;

import com.chuwa.hw7.annotation.Component;

/**
 * Singleton bean: shared data access stub.
 */
@Component
public class UserRepository {
    public String findName(int id) {
        return id == 1 ? "Alex" : "guest";
    }
}
