package net.cserny.videosmover;

import dagger.Module;
import dagger.Provides;
import net.cserny.videosmover.controller.MainController;
import net.cserny.videosmover.provider.InWindowMessageDisplayProvider;
import net.cserny.videosmover.provider.MainStageProvider;
import net.cserny.videosmover.service.*;

import javax.inject.Singleton;

@Module
public class UiModule {

    @Provides
    @Singleton
    public MainController provideMainController(ScanService scanService, VideoMover videoMover, VideoCleaner videoCleaner,
                                                SimpleMessageRegistry messageRegistry, MainStageProvider stageProvider,
                                                OutputResolver outputResolver, CachedTmdbService metadataService,
                                                PathsInitializer pathsInitializer) {
        return new MainController(scanService, videoMover, videoCleaner, messageRegistry,
                stageProvider, outputResolver, metadataService, pathsInitializer);
    }

    @Provides
    @Singleton
    public InWindowMessageDisplayProvider provideInWindowMessageDisplayProvider(SimpleMessageRegistry messageRegistry,
                                                                                MainStageProvider stageProvider) {
        return new InWindowMessageDisplayProvider(messageRegistry, stageProvider);
    }

    @Provides
    @Singleton
    public MainStageProvider provideMainStageProvider() {
        return new MainStageProvider();
    }
}
