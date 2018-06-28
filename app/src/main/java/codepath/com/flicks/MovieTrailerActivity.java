package codepath.com.flicks;

import android.os.Bundle;
import android.util.Log;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import codepath.com.flicks.models.Movie;

public class MovieTrailerActivity extends YouTubeBaseActivity {
        Movie movie;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_movie_trailer);


            final String videoId = getIntent().getStringExtra("id");

            // resolve the player view from the layout
            YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);

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
