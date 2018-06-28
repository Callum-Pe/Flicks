package codepath.com.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Config {
    String imageBaseUrl;
    String posterSize;
    String backdropSize;
    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }

    public Config() {
    }

    public Config(JSONObject response) throws JSONException
    {
        JSONObject images = response.getJSONObject("images");
        imageBaseUrl = images.getString("secure_base_url");
        JSONArray arr = images.getJSONArray("poster_sizes");
        posterSize = arr.optString(3,"w324");
        JSONArray array = images.getJSONArray("backdrop_sizes");
        backdropSize = array.optString(1,"w780");
    }

    public String getImageUrl(String size, String path)
    {
        return String.format("%s%s%s", imageBaseUrl, size, path);

    }

    public String getBackdropSize() {
        return backdropSize;
    }
}
