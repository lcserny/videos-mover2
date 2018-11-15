package net.cserny.videosmover.helper.platform;

public class WindowsPlatform extends Platform {

    WindowsPlatform() { }

    @Override
    public String getRootPathPrefix() {
        return "c:/";
    }

    @Override
    public String getName() {
        return Platform.WINDOWS;
    }
}
