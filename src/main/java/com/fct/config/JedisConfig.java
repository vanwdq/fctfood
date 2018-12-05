/*
package com.fct.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class JedisConfig {

    @Value("${spring.redis.host}")
    private String jedisHost;
    @Value("${spring.redis.port}")
    private int jedisPort;
    @Value("${spring.redis.timeout}")
    private int jedisTimeout;
    @Value("${spring.redis.password}")
    private String redisPassword;

    @Value("${spring.redis.db}")
    private int database;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;
    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxTotal;
    @Value("${spring.redis.jedis.pool.max-wait}")
    private long maxWaitMillis;


    @Bean
    public JedisPoolConfig poolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxWaitMillis(maxWaitMillis);
        return poolConfig;
    }


    @Bean
    public JedisPool pool() {
        if (StringUtils.isEmpty(redisPassword)) {
            return new JedisPool(poolConfig(), jedisHost, jedisPort, jedisTimeout, null, database);
        } else {
            return new JedisPool(poolConfig(), jedisHost, jedisPort, jedisTimeout, redisPassword, database);
        }
    }

}
*/
