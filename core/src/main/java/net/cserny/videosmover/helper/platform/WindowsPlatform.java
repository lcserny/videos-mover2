package net.cserny.videosmover.helper.platform;

class WindowsPlatform implements Platform {

    private static final String SPECIAL_CHARS = "[\\\\/:*?\"<>|]";
    private static final int MAX_PATH_SIZE = 256;

    @Override
    public String getName() {
        return PlatformService.WINDOWS;
    }

    @Override
    public PlatformTrimPathData trimPath(PlatformTrimPathData pathData) {
        pathData.setPath(clearSpecialChars(pathData.getPath()));

        if (pathData.getPath().length() > MAX_PATH_SIZE) {
            pathData.setPath(renameToSaveSpace(pathData.getPath()));
        }

        if (pathData.hasParts()) {
            String[] parts = pathData.getParts();
            int totalLength = pathData.getPath().length();
            for (int i = 0; i < parts.length; i++) {
                parts[i] = clearSpecialChars(parts[i]);
                totalLength += parts[i].length();
            }
            if (totalLength > MAX_PATH_SIZE) {
                parts[parts.length - 1] = renameToSaveSpace(parts[parts.length - 1]);
            }
            pathData.setParts(parts);
        }

        return pathData;
    }

    private String renameToSaveSpace(String path) {
        // TODO
        return path;
    }

    private String clearSpecialChars(String part) {
        // TODO: for each individual part of the path do this, not the whole things
        return part.replaceAll(SPECIAL_CHARS, "");
    }
}
