package ru.admin.config;

import org.springframework.context.annotation.Configuration;
import reactor.blockhound.BlockHound;
import reactor.tools.agent.ReactorDebugAgent;
import ru.admin.config.properties.BlockingCallDetectionProperties;

@Configuration
public class BlockingCallDetectionConfig {
    public BlockingCallDetectionConfig(BlockingCallDetectionProperties blockingCallDetectionProperties) {
        ReactorDebugAgent.init();
        if (blockingCallDetectionProperties.isEnable()) {
            // отключаем проверки для Swagger (Swagger в production лучше отключить)
            BlockHound.builder()
                    .allowBlockingCallsInside("org.springframework.core.io.buffer.DataBufferUtils", "readByteChannel")
                    .install();
        }
    }
}
