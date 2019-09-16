package harunabot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONReader {

    /** toStringBuffer
     *
     * @param in InputStream
     * @return StringBuffer
     * @throws IOException
     */
    public static StringBuffer toStringBuffer(InputStream in) throws IOException{

        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }
        br.close();
        return response;

    }

    /** connect
     *
     * @param link URL
     * @param key API key
     * @return HttpURLConnection
     * @throws IOException
     */
    public static HttpURLConnection connect(String link, String key) throws IOException{

        URL url = new URL(link);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", key);
        conn.setRequestProperty("Accept", "application/vnd.api+json");
        return conn;

    }

}
