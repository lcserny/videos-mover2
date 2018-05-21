package net.cserny.videosmover;

import dagger.Module;
import dagger.Provides;
import net.cserny.videosmover.error.GlobalExceptionCatcher;
import net.cserny.videosmover.service.*;
import net.cserny.videosmover.service.parser.CachedVideoRetriever;
import net.cserny.videosmover.service.parser.VideoExistenceChecker;
import net.cserny.videosmover.service.parser.VideoNameParser;
import net.cserny.videosmover.service.parser.VideoNameTrimmer;
import net.cserny.videosmover.service.validator.*;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;

@Module
public class CoreModule {

    @Provides
    @Singleton
    public GlobalExceptionCatcher provideGlobalExceptionCatcher(SimpleMessageRegistry messageRegistry) {
        return new GlobalExceptionCatcher(messageRegistry);
    }

    @Provides
    @Singleton
    public SimpleMessageRegistry provideSimpleMessageRegistry() {
        return new SimpleMessageRegistry();
    }

    @Provides
    @Singleton
    public CachedTmdbService provideCachedTmdbService() {
        return new CachedTmdbService();
    }

    @Provides
    @Singleton
    public OutputResolver provideOutputResolver(List<VideoNameParser> nameParsers) {
        return new OutputResolver(nameParsers);
    }

    @Provides
    @Singleton
    public List<VideoNameParser> provideNameParsers(VideoNameTrimmer trimmer, CachedVideoRetriever videoRetriever,
                                                    VideoExistenceChecker existenceChecker) {
        return Arrays.asList(trimmer, videoRetriever, existenceChecker);
    }

    @Provides
    @Singleton
    public VideoNameTrimmer provideNameTrimmer() {
        return new VideoNameTrimmer();
    }

    @Provides
    @Singleton
    public CachedVideoRetriever provideVideoRetriever(CachedTmdbService tmdbService) {
        return new CachedVideoRetriever(tmdbService);
    }

    @Provides
    @Singleton
    public VideoExistenceChecker provideExistenceChecker() {
        return new VideoExistenceChecker();
    }

    @Provides
    @Singleton
    public PathsInitializer providePathsInitializer() {
        return new PathsInitializer();
    }

    @Provides
    @Singleton
    public ScanService provideScanService(VideoChecker videoChecker, SubtitlesFinder subtitlesFinder) {
        return new ScanService(videoChecker, subtitlesFinder);
    }

    @Provides
    @Singleton
    public VideoChecker provideVideoChecker(List<VideoValidator> videoValidators) {
        return new VideoChecker(videoValidators);
    }

    @Provides
    @Singleton
    public SubtitlesFinder provideSubtitlesFinder() {
        return new SubtitlesFinder();
    }

    @Provides
    @Singleton
    public List<VideoValidator> provideVideoValidators(VideoPathValidator pathValidator, VideoTypeValidator typeValidator,
                                                       VideoSizeValidator sizeValidator) {
        return Arrays.asList(pathValidator, typeValidator, sizeValidator);
    }

    @Provides
    @Singleton
    public VideoPathValidator providePathValidator() {
        return new VideoPathValidator();
    }

    @Provides
    @Singleton
    public VideoTypeValidator provideTypeValidator() {
        return new VideoTypeValidator();
    }

    @Provides
    @Singleton
    public VideoSizeValidator provideSizeValidator() {
        return new VideoSizeValidator();
    }

//    @Provides
//    @Singleton
//    public StaticPathsProvider provideStaticPathsProvider(PathsInitializer pathsInitializer) {
//        return new StaticPathsProvider(pathsInitializer);
//    }

    @Provides
    @Singleton
    public VideoCleaner provideVideoCleaner(List<RemovalRestriction> removalRestrictions, SimpleMessageRegistry messageRegistry) {
        return new VideoCleaner(removalRestrictions, messageRegistry);
    }

    @Provides
    @Singleton
    public List<RemovalRestriction> provideRemovalRestrictions(MainPathsRestriction mainPathsRestriction,
                                                               CustomPathsRestriction customPathsRestriction) {
        return Arrays.asList(mainPathsRestriction, customPathsRestriction);
    }

    @Provides
    @Singleton
    public MainPathsRestriction provideMainPathsRestriction() {
        return new MainPathsRestriction();
    }

    @Provides
    @Singleton
    public CustomPathsRestriction provideCustomPathsRestriction() {
        return new CustomPathsRestriction();
    }

    @Provides
    @Singleton
    public VideoMover provideVideoMover() {
        return new VideoMover();
    }
}
