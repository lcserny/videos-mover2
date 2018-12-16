package net.cserny.videosmover.helper.platform;

import java.util.Arrays;

public class PlatformTrimPathData {

    private String path;
    private String[] parts;

    public PlatformTrimPathData(String path, String[] parts) {
        this.path = path;
        this.parts = parts;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String[] getParts() {
        return parts;
    }

    public void setParts(String[] parts) {
        this.parts = parts;
    }

    public boolean hasParts() {
        return parts != null && parts.length > 0;
    }

    @Override
    public String toString() {
        return "PlatformTrimPathData{" +
                "path='" + path + '\'' +
                ", parts=" + Arrays.toString(parts) +
                '}';
    }
}
