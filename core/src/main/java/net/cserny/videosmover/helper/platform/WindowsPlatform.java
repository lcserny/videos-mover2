package net.cserny.videosmover.helper.platform;

import java.util.regex.Pattern;

class WindowsPlatform implements Platform {

    static final String SPECIAL_CHARS = "[\\\\/:*?\"<>|]";
    static final int MAX_PATH_SIZE = 256;

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
        String[] split = path.split(Pattern.quote(getSeparator()));
        int remainingChars = 0;
        for (int i = 0; i < split.length - 1; i++) {
            remainingChars += split[i].length();
        }

        String lastPart = split[split.length - 1];

        String extension = lastPart.charAt(lastPart.length() - 4) == '.'
                ? lastPart.substring(lastPart.length() - 4)
                : "";
        remainingChars += extension.length();

        // TODO: if remaining chars > lastPart.length() then trim from previous part also and so on OR throw?
        String trimmed = lastPart.substring(0, (MAX_PATH_SIZE - remainingChars));

        split[split.length - 1] = trimmed + extension;
        return String.join(getSeparator(), split);
    }

    private String clearSpecialChars(String part) {
        String[] split = part.split(Pattern.quote(getSeparator()));
        for (int i = 0; i < split.length; i++) {
            String splitPart = split[i];
            if (i == 0 && splitPart.charAt(1) == ':' && splitPart.length() == 2) {
                continue;
            }
            split[i] = splitPart.replaceAll(SPECIAL_CHARS, "");
        }
        return String.join(getSeparator(), split);
    }
}
