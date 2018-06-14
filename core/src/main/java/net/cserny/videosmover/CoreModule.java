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
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Module
public class CoreModule {

    @Provides
    public Set<VideoNameParser> videoNameParsers(VideoNameTrimmer trimmer,
                                                 CachedVideoRetriever retriever,
                                                 VideoExistenceChecker checker) {
        Set<VideoNameParser> parsers = new LinkedHashSet<>();
        parsers.add(trimmer);
        parsers.add(retriever);
        parsers.add(checker);
        return parsers;
    }

    @Provides
    public Set<VideoValidator> videoValidators(VideoPathValidator pathValidator,
                                               VideoTypeValidator typeValidator,
                                               VideoSizeValidator sizeValidator) {
        Set<VideoValidator> validators = new LinkedHashSet<>();
        validators.add(pathValidator);
        validators.add(typeValidator);
        validators.add(sizeValidator);
        return validators;
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
