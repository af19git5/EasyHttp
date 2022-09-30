package com.jimmyworks.easyhttp.utils;

import lombok.NonNull;
import okhttp3.MediaType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 檔案處理共用
 *
 * @author Jimmy Kang
 */
public class FileUtils {

    public static boolean mkdir(File file) {
        if (file.exists()) {
            return false;
        } else {
            return file.mkdir();
        }
    }

    public static MediaType fileToMediaType(@NonNull File file) {
        MediaType mediaType;
        try {
            Path path = file.toPath();
            String mimeType = Files.probeContentType(path);
            mediaType = MediaType.parse(mimeType);
            if (null == mediaType) {
                mediaType = MediaType.get("application/octet-stream");
            }
        } catch (IOException e) {
            mediaType = MediaType.get("application/octet-stream");
        }
        return mediaType;
    }

    public static MediaType fileNameToMediaType(@NonNull String fileName) {
        return fileToMediaType(new File(fileName));
    }
}
