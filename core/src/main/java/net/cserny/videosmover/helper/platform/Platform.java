package net.cserny.videosmover.helper.platform;

import java.io.File;

interface Platform {

    default String getDefaultRoot() {
        return File.listRoots()[0].toString();
    }

    String getName();

    default String getSeparator() {
        return File.separator;
    };

    default PlatformTrimPathData trimPath(PlatformTrimPathData pathData) {
        return pathData;
    }
}
