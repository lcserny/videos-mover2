package net.cserny.videosmover;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import info.movito.themoviedbapi.TmdbApi;
import net.cserny.videosmover.error.GlobalExceptionCatcher;
import net.cserny.videosmover.service.*;
import net.cserny.videosmover.service.parser.*;
import net.cserny.videosmover.service.validator.*;

import javax.inject.Singleton;
import java.util.Set;

@Module
public class CoreModule {

    @Provides @IntoSet @Singleton
    public VideoNameParser videoNameTrimmer() {
        return new VideoNameTrimmer();
    }

    @Provides @IntoSet @Singleton
    public VideoNameParser cachedVideoRetriever(CachedTmdbService tmdbService) {
        return new CachedVideoRetriever(tmdbService);
    }

    @Provides @IntoSet @Singleton
    public VideoNameParser videoExistenceChecker() {
        return new VideoExistenceChecker();
    }

    @Provides @IntoSet @Singleton
    public VideoValidator videoPathValidator() {
        return new VideoPathValidator();
    }

    @Provides @IntoSet @Singleton
    public VideoValidator videoTypeValidator() {
        return new VideoTypeValidator();
    }

    @Provides @IntoSet @Singleton
    public VideoValidator videoSizeValidator() {
        return new VideoSizeValidator();
    }

    @Provides @IntoSet @Singleton
    public RemovalRestriction mainPathsRestriction() {
        return new MainPathsRestriction();
    }

    @Provides @IntoSet @Singleton
    public RemovalRestriction customPathsRestriction() {
        return new CustomPathsRestriction();
    }

    @Provides @IntoSet @Singleton
    public OutputVideoNameChecker outputVideoNameChecker(SimpleMessageRegistry messageRegistry) {
        return new TvShowOutputVideoNameChecker(messageRegistry);
    }

    @Provides @Singleton
    public OutputVideoNameService outputVideoNameService(Set<OutputVideoNameChecker> videoNameCheckers) {
        return new DefaultOutputVideoNameService(videoNameCheckers);
    }
}
