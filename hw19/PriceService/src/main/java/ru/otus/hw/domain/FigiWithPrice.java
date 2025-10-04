package ru.otus.hw.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.math.BigDecimal;


@AllArgsConstructor
@RedisHash(value = "Stock")
@Data
public class FigiWithPrice {
    @Id
    private String figi;
    private BigDecimal price;
    private String source;
}
