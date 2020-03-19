package listener;

import configuration.AppConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONHelper {

    /**
     * isNSFW
     * @param tags String
     * @return boolean
     */
    static boolean isNSFW(String tags) {
        // put all of these tags in a txt file and load with a File API by storing in a list
        for(String tagFromList : AppConfig.tagList) {
            if(tags.contains(tagFromList)) {
                return true;
            }
        }
        return false;
    }

    /**
     * toStringBuffer
     * @param in InputStream
     * @return StringBuffer
     * @throws IOException
     */
    static StringBuffer toStringBuffer(InputStream in) throws IOException{

        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }
        br.close();
        return response;

    }

    /**
     * connect - No Auth
     * @param link
     * @return
     * @throws IOException
     */
    static StringBuffer connect(String link) throws IOException {
        URL url = new URL(link);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        InputStream inputStream = conn.getInputStream();
        StringBuffer stringBuffer = toStringBuffer(inputStream);
        conn.disconnect();
        return stringBuffer;
    }

    /**
     * connect - API Key (key/value)
     * @param link String
     * @param key String
     * @param value String
     * @return HttpURLConnection
     * @throws IOException
     */
    static StringBuffer connect(String link, String key, String value) throws IOException {

        URL url = new URL(link);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty(key, value);
        conn.setRequestProperty("Accept", "application/json");
        InputStream inputStream = conn.getInputStream();
        StringBuffer stringBuffer = toStringBuffer(inputStream);
        conn.disconnect();
        return stringBuffer;

    }

}
