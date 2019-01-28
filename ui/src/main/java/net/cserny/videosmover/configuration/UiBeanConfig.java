package net.cserny.videosmover.configuration;

import net.cserny.videosmover.provider.InWindowMessageDisplayProvider;
import net.cserny.videosmover.provider.MainStageProvider;
import net.cserny.videosmover.service.MessageDisplayProvider;
import net.cserny.videosmover.service.SimpleMessageRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UiBeanConfig {

    @Bean
    public MessageDisplayProvider messageDisplayProvider(SimpleMessageRegistry messageRegistry, MainStageProvider stageProvider) {
        return new InWindowMessageDisplayProvider(messageRegistry, stageProvider);
    }
}
