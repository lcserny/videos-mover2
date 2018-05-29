package net.cserny.videosmover;

import net.cserny.videosmover.controller.MainController;
import net.cserny.videosmover.provider.InWindowMessageDisplayProvider;
import net.cserny.videosmover.provider.MainStageProvider;
import net.cserny.videosmover.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(CoreConfiguration.class)
public class UiConfiguration {

    @Bean
    public MainStageProvider mainStageProvider() {
        return new MainStageProvider();
    }

    @Bean
    public InWindowMessageDisplayProvider inWindowMessageDisplayProvider(SimpleMessageRegistry messageRegistry,
                                                                         MainStageProvider stageProvider) {
        return new InWindowMessageDisplayProvider(messageRegistry, stageProvider);
    }

    @Bean
    public MainController mainController(ScanService scanService, VideoMover videoMover,
                                         VideoCleaner videoCleaner, SimpleMessageRegistry messageRegistry,
                                         MainStageProvider stageProvider, OutputResolver outputResolver,
                                         CachedTmdbService metadataService, PathsInitializer pathsInitializer) {
        return new MainController(scanService, videoMover, videoCleaner, messageRegistry,
                stageProvider, outputResolver, metadataService, pathsInitializer);
    }

}
