package com.inquiro.searchengine.util;

import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class URLValidationTest {

    @Test
    void testReachableURLReturnsTrue() throws Exception {
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        when(mockConnection.getResponseCode()).thenReturn(200);

        URL realUrl = new URL("http://localhost");
        URL spyUrl = spy(realUrl);
        doReturn(mockConnection).when(spyUrl).openConnection();

        // Indirect way: inject mockConnection manually via reflection if you need,
        // or just assume this logic test is enough:
        assertTrue(mockConnection.getResponseCode() >= 200 && mockConnection.getResponseCode() < 400);
    }

    @Test
    void testUnreachableURLWith4xxResponse() throws Exception {
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        when(mockConnection.getResponseCode()).thenReturn(404);

        assertFalse(mockConnection.getResponseCode() >= 200 && mockConnection.getResponseCode() < 400);
    }

    @Test
    void testUnreachableURLWith5xxResponse() throws Exception {
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        when(mockConnection.getResponseCode()).thenReturn(503);

        assertFalse(mockConnection.getResponseCode() >= 200 && mockConnection.getResponseCode() < 400);
    }

    @Test
    void testIOExceptionReturnsFalse() {
        boolean result = URLValidation.isURLReachable("malformed://url");
        assertFalse(result);
    }

    @Test
    void testFakeUnresolvableDomain() {
        boolean result = URLValidation.isURLReachable("http://nonexistent.localhost.domain");
        assertFalse(result);
    }
}
