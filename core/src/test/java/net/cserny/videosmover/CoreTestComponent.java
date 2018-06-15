package net.cserny.videosmover;

import dagger.Component;
import net.cserny.videosmover.error.GlobalExceptionCatcherTest;
import net.cserny.videosmover.service.*;

import javax.inject.Singleton;

@Singleton
@Component(modules = CoreModule.class)
public interface CoreTestComponent {
    void inject(GlobalExceptionCatcherTest test);
    void inject(OutputResolverTest test);
    void inject(ScanServiceTest test);
    void inject(SubtitleFinderTest test);
    void inject(VideoCheckerTest test);
    void inject(VideoCleanerTest test);
    void inject(VideoMetadataServiceTest test);
    void inject(VideoMoverTest test);
}
