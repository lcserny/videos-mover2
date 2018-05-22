package net.cserny.videosmover;

import net.cserny.videosmover.error.GlobalExceptionCatcher;
import net.cserny.videosmover.service.*;
import net.cserny.videosmover.service.parser.CachedVideoRetriever;
import net.cserny.videosmover.service.parser.VideoExistenceChecker;
import net.cserny.videosmover.service.parser.VideoNameParser;
import net.cserny.videosmover.service.parser.VideoNameTrimmer;
import net.cserny.videosmover.service.validator.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class CoreModule {

    @Bean
    public GlobalExceptionCatcher globalExceptionCatcher(SimpleMessageRegistry messageRegistry) {
        return new GlobalExceptionCatcher(messageRegistry);
    }

    @Bean
    public SimpleMessageRegistry simpleMessageRegistry() {
        return new SimpleMessageRegistry();
    }

    @Bean
    public CachedTmdbService cachedTmdbService() {
        return new CachedTmdbService();
    }

    @Bean
    public OutputResolver outputResolver(Set<VideoNameParser> nameParsers) {
        return new OutputResolver(nameParsers);
    }

    @Bean
    public VideoNameParser videoNameTrimmer() {
        return new VideoNameTrimmer();
    }

    @Bean
    public VideoNameParser cachedVideoRetriever(CachedTmdbService tmdbService) {
        return new CachedVideoRetriever(tmdbService);
    }

    @Bean
    public VideoNameParser videoExistenceChecker() {
        return new VideoExistenceChecker();
    }

    @Bean
    public PathsInitializer pathsInitializer() {
        return new PathsInitializer();
    }

    @Bean
    public ScanService scanService(VideoChecker videoChecker, SubtitlesFinder subtitlesFinder) {
        return new ScanService(videoChecker, subtitlesFinder);
    }

    @Bean
    public VideoChecker videoChecker(Set<VideoValidator> videoValidators) {
        return new VideoChecker(videoValidators);
    }

    @Bean
    public SubtitlesFinder subtitlesFinder() {
        return new SubtitlesFinder();
    }

    @Bean
    public VideoValidator videoPathValidator() {
        return new VideoPathValidator();
    }

    @Bean
    public VideoValidator videoTypeValidator() {
        return new VideoTypeValidator();
    }

    @Bean
    public VideoValidator videoSizeValidator() {
        return new VideoSizeValidator();
    }

    @Bean
    public VideoCleaner videoCleaner(Set<RemovalRestriction> removalRestrictions, SimpleMessageRegistry messageRegistry) {
        return new VideoCleaner(removalRestrictions, messageRegistry);
    }

    @Bean
    public RemovalRestriction mainPathsRestriction() {
        return new MainPathsRestriction();
    }

    @Bean
    public RemovalRestriction customPathsRestriction() {
        return new CustomPathsRestriction();
    }

    @Bean
    public VideoMover videoMover() {
        return new VideoMover();
    }
}
