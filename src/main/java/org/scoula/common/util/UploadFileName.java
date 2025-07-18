package org.scoula.common.util;

import java.util.UUID;

public class UploadFileName {
    public static String getUniqueName(String originalFileName) {
        return UUID.randomUUID().toString() + "_" + originalFileName;
    }
}
