package net.cserny.videosmover;

import dagger.Component;
import javafx.application.Application;

import javax.inject.Singleton;

@Singleton
@Component(modules = {UiModule.class, CoreModule.class})
public interface ApplicationComponent {
    void inject(MainApplication application);

    void inject(MainApplicationKt applicationKt);
}
