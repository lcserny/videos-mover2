package net.cserny.videosmover;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import net.cserny.videosmover.controller.MainController;
import net.cserny.videosmover.provider.InWindowMessageDisplayProvider;
import net.cserny.videosmover.provider.MainStageProvider;
import net.cserny.videosmover.service.*;

import javax.inject.Singleton;

@Module
public class UiModule {

    @Provides
    public MessageDisplayProvider messageDisplayProvider(InWindowMessageDisplayProvider inWindowMessageDisplayProvider) {
        return inWindowMessageDisplayProvider;
    }
}
