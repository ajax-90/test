package com.engagepoint.acceptancetest.base.utils;

import java.io.File;
import java.io.InputStream;

public final class FileUtils {

    private FileUtils() {}

    public static File getFileFromResourcesByFilePath(String filePath) {
        return new File(FileUtils.class.getResource(filePath).getFile());
    }

    public static InputStream getStreamFromResourcesByFilePath(String filePath) {
        return FileUtils.class.getResourceAsStream(filePath);
    }

}
