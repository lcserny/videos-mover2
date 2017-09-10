package net.cserny.videosMover2.configuration;

import net.cserny.videosMover2.service.*;
import net.cserny.videosMover2.service.parser.VideoNameTrimmer;
import net.cserny.videosMover2.service.parser.VideoNameParser;
import net.cserny.videosMover2.service.parser.VideoExistenceChecker;
import net.cserny.videosMover2.service.validator.VideoPathValidator;
import net.cserny.videosMover2.service.validator.VideoTypeValidator;
import net.cserny.videosMover2.service.validator.VideoValidator;
import net.cserny.videosMover2.service.validator.VideodSizeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class ServiceConfig
{
    @Bean
    public ScanService scanService() {
        return new ScanServiceImpl(videoChecker(), subtitlesFinder());
    }

    @Bean
    public VideoMover videoMover() {
        return new VideoMoverImpl();
    }

    @Bean
    public SubtitlesFinder subtitlesFinder() {
        return new SubtitlesFinderImpl();
    }

    @Bean
    public List<VideoValidator> videoValidators() {
        return Arrays.asList(
                new VideoPathValidator(),
                new VideoTypeValidator(),
                new VideodSizeValidator());
    }

    @Bean
    public VideoChecker videoChecker() {
        return new VideoCheckerImpl(videoValidators());
    }

    @Bean
    public List<VideoNameParser> videoNameParsers() {
        return Arrays.asList(
                new VideoNameTrimmer(),
                new VideoExistenceChecker());
    }

    @Bean
    public OutputNameResolver outputNameResolver() {
        return new OutputNameResolverImpl(videoNameParsers());
    }
}
