package ru.otus.hw.config;

import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.distribution.IFeatureAwareVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddedMongoConfig {
    @Bean
    public IFeatureAwareVersion embeddedMongoVersion() {
        return Version.Main.V5_0;
    }
}
