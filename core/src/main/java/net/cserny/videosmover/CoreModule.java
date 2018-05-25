package net.cserny.videosmover;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import info.movito.themoviedbapi.TmdbApi;
import net.cserny.videosmover.error.GlobalExceptionCatcher;
import net.cserny.videosmover.service.*;
import net.cserny.videosmover.service.parser.CachedVideoRetriever;
import net.cserny.videosmover.service.parser.VideoExistenceChecker;
import net.cserny.videosmover.service.parser.VideoNameParser;
import net.cserny.videosmover.service.parser.VideoNameTrimmer;
import net.cserny.videosmover.service.validator.*;

import javax.inject.Singleton;
import java.util.Set;

@Module
public class CoreModule {

    @Provides @Singleton
    public GlobalExceptionCatcher globalExceptionCatcher(SimpleMessageRegistry messageRegistry) {
        return new GlobalExceptionCatcher(messageRegistry);
    }

    @Provides @Singleton
    public SimpleMessageRegistry simpleMessageRegistry() {
        return new SimpleMessageRegistry();
    }

    @Provides @Singleton
    public CachedTmdbService cachedTmdbService() {
        return new CachedTmdbService(new TmdbApi(PropertiesLoader.getTmdbApiKey()));
    }

    @Provides @Singleton
    public OutputResolver outputResolver(Set<VideoNameParser> nameParsers) {
        return new OutputResolver(nameParsers);
    }

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

    @Provides @Singleton
    public PathsInitializer pathsInitializer() {
        return new PathsInitializer();
    }

    @Provides @Singleton
    public ScanService scanService(VideoChecker videoChecker, SubtitlesFinder subtitlesFinder) {
        return new ScanService(videoChecker, subtitlesFinder);
    }

    @Provides @Singleton
    public VideoChecker videoChecker(Set<VideoValidator> videoValidators) {
        return new VideoChecker(videoValidators);
    }

    @Provides @Singleton
    public SubtitlesFinder subtitlesFinder() {
        return new SubtitlesFinder();
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

    @Provides @Singleton
    public VideoCleaner videoCleaner(Set<RemovalRestriction> removalRestrictions, SimpleMessageRegistry messageRegistry) {
        return new VideoCleaner(removalRestrictions, messageRegistry);
    }

    @Provides @IntoSet @Singleton
    public RemovalRestriction mainPathsRestriction() {
        return new MainPathsRestriction();
    }

    @Provides @IntoSet @Singleton
    public RemovalRestriction customPathsRestriction() {
        return new CustomPathsRestriction();
    }

    @Provides @Singleton
    public VideoMover videoMover() {
        return new VideoMover();
    }
}
