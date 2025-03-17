package com.effectivemobile.authservice.service.security;

import com.effectivemobile.authservice.exceptions.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service(value = "myUserDetailService")
@Slf4j
public class MyUserDetailService implements UserDetailsService {

    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    public MyUserDetailService(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        log.info("Метод loadUserByUsername() " + email);
        Object object = redisTemplate.opsForValue().get(email);
        if (object != null) {
            return User.builder()
                    .username(email)
                    .password("")
                    .roles("USER")
                    .build();
        } else {
            throw new UserNotFoundException("Пользователь не найден: " + email);
        }
    }
}
