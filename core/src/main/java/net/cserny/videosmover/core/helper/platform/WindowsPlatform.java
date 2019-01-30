package net.cserny.videosmover.core.helper.platform;

import net.cserny.videosmover.core.helper.StringHelper;

import java.util.regex.Pattern;

class WindowsPlatform implements Platform {

    private static final String SPECIAL_CHARS = "[\\\\/:*?\"<>|]";
    private static final int MAX_PATH_SIZE = 256;

    @Override
    public String getName() {
        return PlatformService.WINDOWS;
    }

    @Override
    public PlatformTrimPathData trimPath(PlatformTrimPathData pathData) {
        if (pathData.getPath().length() > MAX_PATH_SIZE) {
            throw new RuntimeException("Path too long, maximum allowed size is: " + MAX_PATH_SIZE);
        }

        pathData.setPath(clearSpecialChars(pathData.getPath()));
        if (pathData.hasParts()) {
            String[] parts = pathData.getParts();
            for (int i = 0; i < parts.length; i++) {
                if (parts[i].length() > MAX_PATH_SIZE) {
                    throw new RuntimeException("Path too long, maximum allowed size is: " + MAX_PATH_SIZE);
                }
                parts[i] = clearSpecialChars(parts[i]);
            }
            pathData.setParts(parts);
        }

        return pathData;
    }

    private String clearSpecialChars(String part) {
        String[] split = part.split(Pattern.quote(getSeparator()));
        for (int i = 0; i < split.length; i++) {
            String splitPart = split[i];
            if (StringHelper.isEmpty(splitPart) || (splitPart.charAt(1) == ':' && splitPart.length() == 2)) {
                continue;
            }
            split[i] = splitPart.replaceAll(SPECIAL_CHARS, "");
        }
        return String.join(getSeparator(), split);
    }
}
