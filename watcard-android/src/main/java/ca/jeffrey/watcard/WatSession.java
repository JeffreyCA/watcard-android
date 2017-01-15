package ca.jeffrey.watcard;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class WatSession {

    private HashMap<HttpUrl, List<Cookie>> cookieStore;
    CookieManager cookieManager;

    private String verificationToken;

    public WatSession() {
        initializeSession();
    }

    private void initializeSession() {
        final String LOGIN_URL = "https://watcard.uwaterloo.ca/OneWeb/Account/LogOn";

        cookieStore = new HashMap<>();
        cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .build();

        try {
            Request request = new Request.Builder().url(LOGIN_URL).build();
            Response response = client.newCall(request).execute();
            String htmlResponse = response.body().string();

            Document doc = Jsoup.parse(htmlResponse);

            String requestVerificationToken = doc.select("input[name=__RequestVerificationToken]").get(0).val();
            setVerificationToken(requestVerificationToken);
        }
        catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verification_token) {
        this.verificationToken = verification_token;
    }

    public CookieManager getCookieManager() {
        return cookieManager;
    }
}
