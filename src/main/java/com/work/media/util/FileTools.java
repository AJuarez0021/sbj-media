package com.work.media.util;

/**
 *
 * @author linux
 */
public final class FileTools {

    private FileTools() {

    }

    public static String getExtension(String in) {
        if (in == null) {
            return "unnamed.mp3";
        }
        int index = in.lastIndexOf(".");
        if (index < 0) {
            return in + ".mp3";
        }
        return in.substring(0, index) + ".mp3";
    }
    
    public static String getExtension(String in, String ext) {
        if (in == null) {
            return "unnamed." + ext;
        }
        int index = in.lastIndexOf(".");
        if (index < 0) {
            return in + "." + ext;
        }
        return in.substring(0, index) + "." + ext;
    }
}
