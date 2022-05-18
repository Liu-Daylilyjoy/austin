package org.mura.austin.service.deduplication.build;

import org.mura.austin.service.deduplication.DeduplicationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Akutagawa Murasame
 * @date 2022/5/18 13:00
 *
 * 工厂模式决定去重参数
 */
@Service
public class BuilderFactory {
    private Map<String, Builder> builderFactory = new HashMap<>(4);

    private Builder contentDeduplicationBuilder;

    private Builder frequencyDeduplicationBuilder;

    @Autowired
    public void setContentDeduplicationBuilder(Builder contentDeduplicationBuilder) {
        this.contentDeduplicationBuilder = contentDeduplicationBuilder;
    }

    @Autowired
    public void setFrequencyDeduplicationBuilder(Builder frequencyDeduplicationBuilder) {
        this.frequencyDeduplicationBuilder = frequencyDeduplicationBuilder;
    }

    @PostConstruct
    public void init() {
        builderFactory.put(DeduplicationConstants.CONTENT_DEDUPLICATION, contentDeduplicationBuilder);
        builderFactory.put(DeduplicationConstants.FREQUENCY_DEDUPLICATION, frequencyDeduplicationBuilder);
    }

    public Builder select(String key) {
        return builderFactory.get(key);
    }
}
