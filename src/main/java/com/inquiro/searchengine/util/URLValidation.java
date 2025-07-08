package com.inquiro.searchengine.util;

import java.net.HttpURLConnection;
import java.net.URL;

public class URLValidation {
    public static boolean isURLReachable(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setRequestMethod("HEAD");
            int responseCode = conn.getResponseCode();
            return (200 <= responseCode && responseCode < 400);
        } catch (Exception e) {
            return false;
        }
    }
}
