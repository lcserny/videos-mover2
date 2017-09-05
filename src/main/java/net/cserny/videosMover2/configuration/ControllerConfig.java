package net.cserny.videosMover2.configuration;

import net.cserny.videosMover2.controller.MainController;
import net.cserny.videosMover2.service.OutputNameResolver;
import net.cserny.videosMover2.service.ScanService;
import net.cserny.videosMover2.service.VideoMover;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ServiceConfig.class)
public class ControllerConfig
{
    @Autowired
    private ScanService scanService;
    @Autowired
    private VideoMover videoMover;
    @Autowired
    private OutputNameResolver nameResolver;

    @Bean
    public MainController mainController() {
        return new MainController(scanService, videoMover, nameResolver);
    }
}
