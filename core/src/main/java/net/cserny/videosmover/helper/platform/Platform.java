package net.cserny.videosmover.helper.platform;

public abstract class Platform {

    static final String WINDOWS = "windows";
    static final String MAC = "mac";
    static final String LINUX = "linux";

    private static Platform instance;

    public static Platform initPlatform() {
        if (instance == null) {
            String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                instance = new WindowsPlatform();
            } else if (os.contains("OS X")) {
                instance = new MacPlatform();
            } else {
                instance = new LinuxPlatform();
            }
        }
        return instance;
    }

    public abstract String getPathPrefix();

    public abstract String getName();
}
