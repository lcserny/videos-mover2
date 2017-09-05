package net.cserny.videosMover2.configure;

import com.google.inject.AbstractModule;
import net.cserny.videosMover2.service.*;

public class ServiceInjector extends AbstractModule
{
    @Override
    protected void configure() {
        bind(ScanService.class).to(ScanServiceImpl.class);
        bind(VideoMover.class).to(VideoMoverImpl.class);
        bind(SubtitlesFinder.class).to(SubtitlesFinderImpl.class);
        bind(VideoChecker.class).to(VideoCheckerImpl.class);
        bind(OutputNameResolver.class).to(OutputNameResolverImpl.class);
    }
}
