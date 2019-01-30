package net.cserny.videosmover.javafx.configuration;

import net.cserny.videosmover.javafx.provider.InWindowMessageDisplayProvider;
import net.cserny.videosmover.javafx.provider.MainStageProvider;
import net.cserny.videosmover.core.service.MessageDisplayProvider;
import net.cserny.videosmover.core.service.SimpleMessageRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UiBeanConfig {

    @Bean
    public MessageDisplayProvider messageDisplayProvider(SimpleMessageRegistry messageRegistry, MainStageProvider stageProvider) {
        return new InWindowMessageDisplayProvider(messageRegistry, stageProvider);
    }
}
