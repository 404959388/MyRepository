package com.yellowriver.redis626.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author huanghe
 * @create 2022/2/17 2:39 PM
 */
@Data
@Configuration
public class RedissonConfig {

    private final String REDIS_URL = "redis://";

    @Value("#{'${spring.redis.cluster.nodes}'.split(',')}")
    private List<String> nodes;

    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public RedissonClient initRedisClient() {
        if (CollectionUtils.isEmpty(nodes)) {
            throw new IllegalArgumentException("nodes must not empty");
        }
        String[] nodesAddress = new String[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            nodesAddress[i] = REDIS_URL + nodes.get(i);
        }
        Config config = new Config();
        config.useClusterServers()
                // 默认设置为SLAVE
                // .setReadMode(ReadMode.SLAVE)
                // 集群状态扫描间隔
                .setScanInterval(2000)
                .setClientName("yellow-river")
                .setPassword(password)
                .addNodeAddress(nodesAddress);
        return Redisson.create(config);
    }
}
