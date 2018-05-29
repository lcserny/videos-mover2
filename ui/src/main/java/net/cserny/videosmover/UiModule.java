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

    @Provides @Singleton
    public MessageDisplayProvider inWindowMessageDisplayProvider(SimpleMessageRegistry messageRegistry,
                                                                 MainStageProvider stageProvider) {
        return new InWindowMessageDisplayProvider(messageRegistry, stageProvider);
    }
}
