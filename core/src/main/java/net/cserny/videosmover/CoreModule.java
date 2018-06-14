package net.cserny.videosmover;

import com.google.common.collect.Sets;
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

    @Provides
    public Set<VideoNameParser> videoNameParsers(VideoNameTrimmer trimmer,
                                                 CachedVideoRetriever retriever,
                                                 VideoExistenceChecker checker) {
        return Sets.newHashSet(trimmer, retriever, checker);
    }

    @Provides @IntoSet
    public VideoValidator videoPathValidator(VideoPathValidator pathValidator) {
        return pathValidator;
    }

    @Provides @IntoSet
    public VideoValidator videoTypeValidator(VideoTypeValidator typeValidator) {
        return typeValidator;
    }

    @Provides @IntoSet
    public VideoValidator videoSizeValidator(VideoSizeValidator sizeValidator) {
        return sizeValidator;
    }

    @Provides @IntoSet
    public RemovalRestriction mainPathsRestriction(MainPathsRestriction mainPathsRestriction) {
        return mainPathsRestriction;
    }

    @Provides @IntoSet
    public RemovalRestriction customPathsRestriction(CustomPathsRestriction customPathsRestriction) {
        return customPathsRestriction;
    }

    @Provides @IntoSet
    public OutputVideoNameChecker outputVideoNameChecker(TvShowOutputVideoNameChecker tvShowOutputVideoNameChecker) {
        return tvShowOutputVideoNameChecker;
    }

    @Provides
    public OutputVideoNameService outputVideoNameService(DefaultOutputVideoNameService outputVideoNameService) {
        return outputVideoNameService;
    }
}
