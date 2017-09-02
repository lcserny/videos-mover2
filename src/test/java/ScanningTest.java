import net.cserny.videosMover2.dto.Video;
import net.cserny.videosMover2.service.ScanService;
import net.cserny.videosMover2.service.ScanServiceImpl;
import net.cserny.videosMover2.service.VideoParserImpl;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Created by leonardo on 02.09.2017.
 */
public class ScanningTest
{
    @Test
    public void whenScanningReturnVideosFromLocation() throws Exception {
        ScanService scanService = new ScanServiceImpl(new VideoParserImpl());
        String location = "/mnt/Data/Downloads/";
        List<Video> videos = scanService.scan(location);

        assertNotNull(videos);
        assertFalse(videos.isEmpty());
    }
}
