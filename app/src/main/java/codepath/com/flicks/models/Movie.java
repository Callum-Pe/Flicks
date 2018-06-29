package codepath.com.flicks.models;


import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import cz.msebera.android.httpclient.Header;

import static codepath.com.flicks.MovieListActiviity.API_BASE_URL;
import static codepath.com.flicks.MovieListActiviity.API_KEY_PARAM;


@Parcel
public class Movie {
    String title;
    String overview;
    String image_url;
    String backdropPath;
    Double voteAverage;
    String video_id;
    int id;

    Boolean done = false;

    public String getVideo_id() {
        while(!done)
        {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return video_id;
    }


    public int getId() {
        return id;
    }

    public Movie() {}
    public Movie(JSONObject o) throws JSONException {
        title = o.getString("title");
        overview = o.getString("overview");
        image_url = o.getString("poster_path");
        backdropPath = o.getString("backdrop_path");
        voteAverage = o.getDouble("vote_average");
        id = o.getInt("id");
        AsyncHttpClient client = new AsyncHttpClient();


        String url = API_BASE_URL + String.format("/movie/%s/videos", id);

        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, "9dc9bd5ee21084cd2011b1f31e80e12f");
        client.get(url, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray array = response.getJSONArray("results");
                    String videoid = null;

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(0);
                        if (o.getString("site").equals("YouTube")) {
                            videoid = o.getString("key");
                            break;
                        }
                    }
                    video_id = videoid;
                    done = true;
                    Log.i(title,""+videoid);
                } catch (JSONException e) {
                }

            }

        });
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
