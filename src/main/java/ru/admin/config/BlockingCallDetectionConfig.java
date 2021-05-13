package ru.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.blockhound.BlockHound;
import ru.admin.config.properties.BlockingCallDetectionProperties;

import javax.annotation.PostConstruct;

@Configuration
public class BlockingCallDetectionConfig {
    @Bean
    public BlockingCallDetectionProperties blockingCallDetectionProperties () {
        return new BlockingCallDetectionProperties();
    }

    @SuppressWarnings("unused")
    @PostConstruct
    public void installBlockHound() { // устанавливает инструмент для поиска "блокирующих" участков кода
        if (blockingCallDetectionProperties().isEnable()) {
            // отключаем проверки для Swagger (Swagger в production лучше отключить)
            BlockHound.builder().allowBlockingCallsInside("org.springframework.core.io.buffer.DataBufferUtils$ReadableByteChannelGenerator", "accept").install();
        }
    }
}
