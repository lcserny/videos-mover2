package net.cserny.videosmover;

import com.google.inject.AbstractModule;
import net.cserny.videosmover.provider.InWindowMessageDisplayProvider;
import net.cserny.videosmover.service.MessageDisplayProvider;

public class UiModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MessageDisplayProvider.class).to(InWindowMessageDisplayProvider.class);
    }
}
