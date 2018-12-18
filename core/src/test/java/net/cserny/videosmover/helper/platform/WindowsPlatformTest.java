package net.cserny.videosmover.helper.platform;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

// TODO: not finished yet, neet to test also specialCharsStrip
public class WindowsPlatformTest {

    private WindowsPlatform platform = new WindowsPlatform();

    @Test
    public void testLengthTrim() {
        String longPath = "C:\\SomePath with a lot of charcters in it\\SomePath with a lot of charcters in it\\"
                + "SomePath with a lot of charcters in it\\SomePath with a lot of charcters in it\\"
                + "SomePath with a lot of charcters in it\\SomePath with a lot of charcters in it\\"
                + "SomePath with a lot of charcters in it\\SomePath with a lot of charcters in it\\"
                + "SomePath with a lot of charcters in it\\SomePath with a lot of charcters in it\\"
                + "SomePath with a lot of charcters in it\\SomeVideo name with a lot of characters.mp4";
        String expected = "C:\\SomePath with a lot of charcters in it\\SomePath with a lot of charcters in it\\"
                + "SomePath with a lot of charcters in it\\SomePath with a lot of charcters in it\\"
                + "SomePath with a lot of charcters in it\\SomePath with a lot of charcters in it\\SomePath with a.mp4";

        PlatformTrimPathData pathData = platform.trimPath(new PlatformTrimPathData(longPath));

        assertEquals(pathData.getPath(), expected);
    }
}
