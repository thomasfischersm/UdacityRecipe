package com.playposse.udacityrecipe.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.playposse.udacityrecipe.R;
import com.playposse.udacityrecipe.util.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A {@link Fragment} that shows the invididual recipe step. It is expected to be contained inside
 * of {@link RecipeStepContainerFragment}, which has a {@link ViewPager} to navigate through all the
 * recipe steps.
 */
public class RecipeStepIndividualFragment extends Fragment {

    public static final String RECIPE_ID_PARAM = "recipeId";
    public static final String STEP_INDEX_PARAM = "stepIndex";
    public static final String SHORT_DESCRIPTION_PARAM = "shortDescription";
    public static final String DESCRIPTION_PARAM = "description";
    public static final String VIDEO_URL_PARAM = "videoUrl";
    public static final String THUMBNAIL_PARAM = "thumbnailUrl";

    @BindView(R.id.player_view) SimpleExoPlayerView playerView;
    @BindView(R.id.no_video_available_layout) FrameLayout noVideoAvailableLayout;
    @BindView(R.id.step_description_text_view) TextView stepDescriptionTextView;

    private long recipeId;
    private int stepIndex;
    private String shortDescription;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;

    private SimpleExoPlayer simpleExoPlayer;

    public static RecipeStepIndividualFragment newInstance(
            long recipeId,
            int stepIndex,
            String shortDescription,
            String description,
            String videoUrl,
            String thumbnailUrl) {

        RecipeStepIndividualFragment fragment = new RecipeStepIndividualFragment();
        Bundle args = new Bundle();
        args.putLong(RECIPE_ID_PARAM, recipeId);
        args.putInt(STEP_INDEX_PARAM, stepIndex);
        args.putString(SHORT_DESCRIPTION_PARAM, shortDescription);
        args.putString(DESCRIPTION_PARAM, description);
        args.putString(VIDEO_URL_PARAM, videoUrl);
        args.putString(THUMBNAIL_PARAM, thumbnailUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipeId = getArguments().getLong(RECIPE_ID_PARAM);
            stepIndex = getArguments().getInt(STEP_INDEX_PARAM);
            shortDescription = getArguments().getString(SHORT_DESCRIPTION_PARAM);
            description = getArguments().getString(DESCRIPTION_PARAM);
            videoUrl = getArguments().getString(VIDEO_URL_PARAM);
            thumbnailUrl = getArguments().getString(THUMBNAIL_PARAM);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View rootView =
                inflater.inflate(R.layout.fragment_recipe_step_individual, container, false);

        ButterKnife.bind(this, rootView);

        stepDescriptionTextView.setText(description);

        return rootView;
    }

    private SimpleExoPlayer createExoPlayer() {
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        return ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!StringUtil.isEmpty(videoUrl)) {
            playerView.setVisibility(View.VISIBLE);
            noVideoAvailableLayout.setVisibility(View.GONE);

            simpleExoPlayer = createExoPlayer();
            playerView.setPlayer(simpleExoPlayer);

            preparePlayer(videoUrl);
        } else {
            playerView.setVisibility(View.GONE);
            noVideoAvailableLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    private void preparePlayer(String videoUrl) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(
                        getActivity(),
                        Util.getUserAgent(getActivity(), "udacityRecipeApp-meaningless"),
                        null);
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource videoSource = new ExtractorMediaSource(
                Uri.parse(videoUrl),
                dataSourceFactory,
                extractorsFactory,
                null,
                null);
        simpleExoPlayer.prepare(videoSource);
    }
}
