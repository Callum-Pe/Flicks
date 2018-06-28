package codepath.com.flicks.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Movie {
    String title;
    String overview;
    String image_url;
    String backdropPath;
    Double voteAverage;
    public Movie() {}
    public Movie(JSONObject o) throws JSONException
    {
        title = o.getString("title");
        overview = o.getString("overview");
        image_url = o.getString("poster_path");
        backdropPath = o.getString("backdrop_path");
        voteAverage = o.getDouble("vote_average");

    }

    public String getTitle() {
        return title;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getBackdropPath() { return backdropPath; }

    public String getOverview() {
        return overview;
    }

    public String getImage_url() {
        return image_url;
    }
}
