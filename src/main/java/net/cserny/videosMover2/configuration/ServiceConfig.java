package net.cserny.videosMover2.configuration;

import net.cserny.videosMover2.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public VideoChecker videoChecker() {
        return new VideoCheckerImpl();
    }

    @Bean
    public OutputNameResolver outputNameResolver() {
        return new OutputNameResolverImpl();
    }
}
