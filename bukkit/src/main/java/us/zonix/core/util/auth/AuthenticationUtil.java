package us.zonix.core.util.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class AuthenticationUtil {

    public static String getTokenByKey(String key) {

        String url = "http://chart.googleapis.com/chart?chs=400x400&cht=qr&chl=200x200&chld=M|0&cht=qr&chl=otpauth://totp/Zonix-Staff-Token" + "?secret=" + key;
        try {
            return shortURL(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String shortURL(String url) throws IOException {

        String tinyUrl = "http://tinyurl.com/api-create.php?url=";
        String tinyUrlLookup = tinyUrl + URLEncoder.encode(url, "UTF-8");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(tinyUrlLookup).openStream()));
        tinyUrl = reader.readLine();
        return tinyUrl;
    }
}
