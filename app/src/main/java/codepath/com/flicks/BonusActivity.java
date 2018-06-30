package codepath.com.flicks;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;

import org.parceler.Parcels;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import codepath.com.flicks.models.Movie;

public class BonusActivity extends YouTubeBaseActivity {
    Movie movie;
    AsyncHttpClient client;

    @Nullable @BindView(R.id.tvTitle)
    TextView tvTitle;
    @Nullable @BindView(R.id.tvOverview)TextView tvOverview;
    @Nullable @BindView(R.id.rbVoteAverage)RatingBar rbVoteAverage;

    ImageView imageView;
    @BindString(R.string.api_key)    String API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus);
        ButterKnife.bind(this);

        boolean portrait = this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT;

        movie =  Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));

        final String videoId = movie.getVideo_id();//"tKodtNFpzBA";//;

        // resolve the player view from the layout
        YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);


        Log.d("BonusActivity", String.format("Showing details for '%s'", movie.getTitle()));
        Log.d("Videoid", String.format("id for '%s'", movie.getVideo_id()));

        //Config config = Parcels.unwrap(getIntent().getParcelableExtra(Config.class.getSimpleName()));
        if(portrait)
        {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());

            float voteAverage = movie.getVoteAverage().floatValue();
            rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

        }

        // initialize with API key stored in secrets.xml
        playerView.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {
                // do any work here to cue video, play video, etc.
                youTubePlayer.cueVideo(videoId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                // log the error
                Log.e("MovieTrailerActivity", "Error initializing YouTube player");
            }
        });
    }
}
