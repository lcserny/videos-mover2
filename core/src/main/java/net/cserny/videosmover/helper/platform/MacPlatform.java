package net.cserny.videosmover.helper.platform;

public class MacPlatform extends Platform {

    MacPlatform() { }

    @Override
    public String getPathPrefix() {
        return "";
    }

    @Override
    public String getName() {
        return Platform.MAC;
    }
}
