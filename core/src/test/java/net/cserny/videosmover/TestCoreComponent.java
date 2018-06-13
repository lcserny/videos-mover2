package net.cserny.videosmover;

import dagger.Component;
import net.cserny.videosmover.error.GlobalExceptionCatcherTest;
import net.cserny.videosmover.service.OutputResolverTest;
import net.cserny.videosmover.service.ScanServiceTest;

import javax.inject.Singleton;

@Singleton
@Component(modules = TestCoreModule.class)
public interface TestCoreComponent {
    void inject(GlobalExceptionCatcherTest test);

    void inject(OutputResolverTest test);

    void inject(ScanServiceTest test);
}
