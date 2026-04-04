package com.chuwa.hw7.demo;

import com.chuwa.hw7.annotation.Autowire;
import com.chuwa.hw7.annotation.Component;

/**
 * Singleton bean with field injection.
 */
@Component
public class ReportService {

    @Autowire
    private UserRepository userRepository;

    public String greetingForUser(int id) {
        return "Hello, " + userRepository.findName(id);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
}
