package config;

import net.cserny.videosMover.controller.MainController;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by leonardo on 17.09.2017.
 */
@Configuration
public class TestConfiguration
{
    @Bean
    public MainController mainController() {
        return Mockito.mock(MainController.class);
    }
}
