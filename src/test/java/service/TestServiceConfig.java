package service;

import net.cserny.videosMover2.service.VideoMover;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestServiceConfig
{
    @Bean
    @Qualifier("testVideoMover")
    public VideoMover videoMover() {
        return new TestVideoMover();
    }
}
