package net.cserny.videosmover;

import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {UiModule.class, CoreModule.class})
public interface MainComponent {
    void inject(MainApplication application);
}
