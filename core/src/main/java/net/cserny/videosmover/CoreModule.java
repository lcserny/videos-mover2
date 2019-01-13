package net.cserny.videosmover;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import net.cserny.videosmover.helper.DefaultLoadingService;
import net.cserny.videosmover.helper.LoadingService;
import net.cserny.videosmover.service.CachedMetadataService;
import net.cserny.videosmover.service.MovitoCachedTmdbService;
import net.cserny.videosmover.service.DefaultOutputVideoNameService;
import net.cserny.videosmover.service.OutputVideoNameService;
import net.cserny.videosmover.service.parser.*;
import net.cserny.videosmover.service.validator.*;

public class CoreModule extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<VideoNameParser> videoNameParserMultibinder = Multibinder.newSetBinder(binder(), VideoNameParser.class);
        videoNameParserMultibinder.addBinding().to(VideoNameTrimmer.class);
        videoNameParserMultibinder.addBinding().to(CachedVideoRetriever.class);
        videoNameParserMultibinder.addBinding().to(VideoExistenceChecker.class);

        Multibinder<VideoValidator> videoValidatorMultibinder = Multibinder.newSetBinder(binder(), VideoValidator.class);
        videoValidatorMultibinder.addBinding().to(VideoPathValidator.class);
        videoValidatorMultibinder.addBinding().to(VideoTypeValidator.class);
        videoValidatorMultibinder.addBinding().to(VideoSizeValidator.class);

        Multibinder<RemovalRestriction> removalRestrictionMultibinder = Multibinder.newSetBinder(binder(), RemovalRestriction.class);
        removalRestrictionMultibinder.addBinding().to(MainPathsRestriction.class);
        removalRestrictionMultibinder.addBinding().to(CustomPathsRestriction.class);

        Multibinder<OutputVideoNameChecker> outputVideoNameCheckerMultibinder = Multibinder.newSetBinder(binder(), OutputVideoNameChecker.class);
        outputVideoNameCheckerMultibinder.addBinding().to(TvShowOutputVideoNameChecker.class);

        bind(OutputVideoNameService.class).to(DefaultOutputVideoNameService.class);

        bind(CachedMetadataService.class).to(MovitoCachedTmdbService.class);

        bind(LoadingService.class).to(DefaultLoadingService.class);
    }
}
