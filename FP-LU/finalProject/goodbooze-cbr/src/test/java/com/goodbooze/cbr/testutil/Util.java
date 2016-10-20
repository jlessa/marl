package com.goodbooze.cbr.testutil;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

public class Util {
    /**
     * Simplifies file reading
     * 
     * @param loader
     *            Classloder
     * @param fileLocation
     * @return file content
     * @throws IOException
     */
    public static String fileToString(Class<?> loader, String fileLocation)
            throws IOException {
        return IOUtils.toString(
                loader.getClassLoader().getResource(fileLocation),
                Charset.defaultCharset());
    }
}
