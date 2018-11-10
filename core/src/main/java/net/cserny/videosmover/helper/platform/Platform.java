package net.cserny.videosmover.helper.platform;

public abstract class Platform {

    static final String WINDOWS = "windows";
    static final String MAC = "mac";
    static final String LINUX = "linux";

    public static Platform initPlatform() {
        String os = System.getProperty("os.name");
        if (os.contains("Windows")) {
            return new WindowsPlatform();
        } else if (os.contains("OS X")) {
            return new MacPlatform();
        } else {
            return new LinuxPlatform();
        }
    }

    public abstract String getPathPrefix();

    public abstract String getName();
}
