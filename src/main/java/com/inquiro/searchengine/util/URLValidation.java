package com.inquiro.searchengine.util;

import java.net.HttpURLConnection;
import java.net.URL;

// Used to check if a URL is reachable. Responds to an HTTP HEAD request
public class URLValidation {

    // Check URL is reachable
    public static boolean isURLReachable(String urlStr) {
        try {
            URL url = new URL(urlStr); //Convert string to a URL object
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // Open a connection to the URL and cast it to HttpURLConnection
           
            // Timeout settings for the connection (ms)
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setRequestMethod("HEAD"); //HTTP HEAD checks availability. 
            
            // HTTP response code. True if 200–399.
            int responseCode = conn.getResponseCode();
            return (200 <= responseCode && responseCode < 400);
        } catch (Exception e) {
            return false;
        }
    }
}
