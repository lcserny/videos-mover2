package net.cserny.videosmover;

import dagger.Module;
import dagger.Provides;
import net.cserny.videosmover.service.CachedTmdbService;
import org.mockito.Mockito;

public class CoreTestModule extends CoreModule {

    private CachedTmdbService cachedTmdbService;

    public CoreTestModule() {
        cachedTmdbService = Mockito.mock(CachedTmdbService.class);
    }

    @Override
    public CachedTmdbService cachedTmdbService() {
        return cachedTmdbService;
    }
}
