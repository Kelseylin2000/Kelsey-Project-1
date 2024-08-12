package com.example.stylish.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Log4j2
@Component
public class RateLimiterFilter extends OncePerRequestFilter {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public RateLimiterFilter(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper){
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    private static final int LIMIT_COUNT = 10;
    private static final int LIMIT_SECOND = 1;
    private static final int EXPIRE_SECOND = 10;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String ipAddress = request.getRemoteAddr();
        String requestURI = request.getRequestURI();

        long currentTimestamp = Instant.now().toEpochMilli();
        String timestampKey = "timestamp:" + ipAddress + ":" + requestURI;
        String countKey = "count:" + ipAddress + ":" + requestURI;

        try{
            Long lastTimestamp = (Long) redisTemplate.opsForValue().get(timestampKey);
            Integer currentCount = (Integer) redisTemplate.opsForValue().get(countKey);

            if (lastTimestamp == null || currentCount == null || currentTimestamp - lastTimestamp >= LIMIT_SECOND * 1000) {
                redisTemplate.opsForValue().set(timestampKey, currentTimestamp, EXPIRE_SECOND, TimeUnit.SECONDS);
                redisTemplate.opsForValue().set(countKey, 1, EXPIRE_SECOND, TimeUnit.SECONDS);
            } else if (currentCount >= LIMIT_COUNT) {
                response.setStatus(429);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Too many requests");

                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                return;
            } else {
                redisTemplate.opsForValue().increment(countKey);
                redisTemplate.expire(countKey, EXPIRE_SECOND, TimeUnit.SECONDS);
            }
        }catch(Exception e){
            log.info("redis connection fail");
        }

        filterChain.doFilter(request, response);
    }
}


