package net.cserny.videosmover.core.helper.platform;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WindowsPlatformTest {

    private WindowsPlatform platform = new WindowsPlatform();

    @Test
    public void testSpecialCharsTrim() {
        String path = String.format("%s%s%s%s",
                platform.getDefaultRoot(),
                "The Lord of the Rings: The Fellowship of <>the Ring (2001)",
                platform.getSeparator(),
                "The_Lord_of_the_Rings:_The_Fellowship_of_<>the_Ring.mp4");

        String expected = String.format("%s%s%s%s",
                platform.getDefaultRoot(),
                "The Lord of the Rings The Fellowship of the Ring (2001)",
                platform.getSeparator(),
                "The_Lord_of_the_Rings_The_Fellowship_of_the_Ring.mp4");

        PlatformTrimPathData pathData = platform.trimPath(new PlatformTrimPathData(path));

        assertEquals(pathData.getPath(), expected);
    }
}
