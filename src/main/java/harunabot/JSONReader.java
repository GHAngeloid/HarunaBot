package harunabot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONReader {

    private static String readAll(BufferedReader rd) throws IOException {
        String result = "";
        String output = null;
        while ((result = rd.readLine()) != null) {
            result = result.substring(1, result.length() - 1);
            output = result;
        }
        return output;
    }

    public static JSONObject URLtoJSON(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            //System.out.println(jsonText);
            JSONObject json = new JSONObject(jsonText);
            return json;
        }finally{
            is.close();
        }
    }

    public static JSONArray URLtoJSONArray(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = rd.readLine();
            //System.out.println(jsonText);
            JSONArray json = new JSONArray(jsonText);
            return json;
        }finally{
            is.close();
        }
    }

    public static StringBuffer toStringBuffer(InputStream in) throws IOException{
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            return response;
        }catch(IOException e){
            return null;
        }
    }

}
