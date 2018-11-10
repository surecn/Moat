package com.asyncaction.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by surecn on 15/7/1.
 */
public class IOUtils {

    public static String readString(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i= -1;
        try {
            while((i = is.read()) != -1){
                baos.write(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toString();
    }

    public static byte[] readAllBytes(InputStream is) throws Exception {
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int n;
        while ((n = bis.read(b)) != -1) {
            out.write(b, 0, n);
        }
        bis.close();
        return out.toByteArray();
    }
}
