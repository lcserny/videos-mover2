package net.cserny.videosmover.helper.platform;

public class PlatformService {

    static final String WINDOWS = "windows";
    static final String MAC = "mac";
    static final String LINUX = "linux";

    private static final Platform platform = initPlatform();

    private static Platform initPlatform() {
        String os = System.getProperty("os.name");
        if (os.contains("Windows")) {
            return new WindowsPlatform();
        } else if (os.contains("OS X")) {
            return new MacPlatform();
        } else {
            return new LinuxPlatform();
        }
    }

    public static String getDefaultRoot() {
        return platform.getDefaultRoot();
    }

    public static String getName() {
        return platform.getName();
    }

    public static String getSeparator() {
        return platform.getSeparator();
    }

    public static PlatformTrimPathData trimPath(PlatformTrimPathData pathData) {
        return platform.trimPath(pathData);
    }
}
