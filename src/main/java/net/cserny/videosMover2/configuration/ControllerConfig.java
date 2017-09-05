package net.cserny.videosMover2.configuration;

import net.cserny.videosMover2.controller.MainController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerConfig
{
    @Bean
    public MainController mainController() {
        return new MainController();
    }
}
