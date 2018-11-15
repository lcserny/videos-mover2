package net.cserny.videosmover.helper.platform;

public abstract class Platform {

    static final String WINDOWS = "windows";
    static final String MAC = "mac";
    static final String LINUX = "linux";

    private static Platform instance;

    static {
        initPlatform();
    }

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

    public static String getRootPathPrefix() {
        instance.get
    }

    public abstract String getName();
}
