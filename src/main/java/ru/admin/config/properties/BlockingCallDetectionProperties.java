package ru.admin.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "blocking-call-detection")
public class BlockingCallDetectionProperties {
    private boolean enable;
}
