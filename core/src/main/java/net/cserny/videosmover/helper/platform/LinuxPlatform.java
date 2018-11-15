package net.cserny.videosmover.helper.platform;

public class LinuxPlatform extends Platform {

    LinuxPlatform() { }

    @Override
    public String getRootPathPrefix() {
        return "/";
    }

    @Override
    public String getName() {
        return Platform.LINUX;
    }
}
