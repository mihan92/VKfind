package com.example.vkfind.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NetworkUtils {
    private final static String VK_BASE_URL = "https://api.vk.com";
    private final static String VK_GET_USER = "/method/users.get";
    private final static String PARAM_USER_ID = "user_id";
    private final static String FIELDS = "fields";
    private final static String PARAM_VERSION = "v";
    private final static String ACCESS_TOKEN = "access_token";

    public static URL generateUrl(String user_id){
        Uri builtUri = Uri.parse(VK_BASE_URL + VK_GET_USER)
                .buildUpon()
                .appendQueryParameter(PARAM_USER_ID, user_id)
                .appendQueryParameter(FIELDS, "bdate")
                .appendQueryParameter(PARAM_VERSION, "5.131")
                .appendQueryParameter(ACCESS_TOKEN, AccessToken.token)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
    public static String getResponse(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream is = urlConnection.getInputStream();
            Scanner scanner = new Scanner(is);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else
                return null;
        } catch(UnknownHostException e) {
            return null;
        }
        finally {
            urlConnection.disconnect();
        }
    }
}
