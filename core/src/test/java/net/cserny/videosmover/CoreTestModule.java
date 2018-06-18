package net.cserny.videosmover;

import net.cserny.videosmover.service.CachedMetadataService;
import net.cserny.videosmover.service.SimpleMessageRegistry;
import org.mockito.Mockito;

public class CoreTestModule extends CoreModule {

    private CachedMetadataService cachedTmdbService;

    public CoreTestModule() {
        cachedTmdbService = Mockito.mock(CachedMetadataService.class);
    }

    @Override
    public CachedMetadataService cachedMetadataService(SimpleMessageRegistry messageRegistry) {
        return cachedTmdbService;
    }
}
