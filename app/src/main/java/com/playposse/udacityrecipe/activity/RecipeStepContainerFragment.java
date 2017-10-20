package com.playposse.udacityrecipe.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.playposse.udacityrecipe.R;
import com.playposse.udacityrecipe.data.RecipeContentContract.StepTable;
import com.playposse.udacityrecipe.util.SmartCursor;
import com.playposse.udacityrecipe.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A {@link Fragment} that shows a recipe step. It actually uses a view that allows swiping from
 * one step to the next.
 */
public class RecipeStepContainerFragment
        extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = RecipeStepContainerFragment.class.getSimpleName();

    private static final String RECIPE_ID_PARAM = "recipeId";
    private static final String STEP_INDEX_PARAM = "stepIndex";
    private static final int LOADER_ID = 5;

    @BindView(R.id.recipe_step_pager) ViewPager recipeStepPager;

    private Long recipeId;
    private Integer stepIndex;
    private RecipeStepPagerAdapter recipeStepAdapter;
    private Map<Integer, Integer> stepIndexToPositionMap = new HashMap<>();

    public static RecipeStepContainerFragment newInstance() {
        return new RecipeStepContainerFragment();
    }

    void setRecipeStep(long recipeId, int stepIndex) {
        this.recipeId = recipeId;
        this.stepIndex = stepIndex;
        Log.i(LOG_TAG, "setRecipeStep: Activity has set step index " + stepIndex);

        initLoader();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            recipeId = savedInstanceState.getLong(RECIPE_ID_PARAM);
            stepIndex = savedInstanceState.getInt(STEP_INDEX_PARAM);
            Log.i(LOG_TAG, "onCreate: Loaded step index " + stepIndex);
        }

        if (recipeId == null) {
            recipeId = ActivityNavigator.getRecipeId(getActivity().getIntent());
        }

        if (stepIndex == null) {
            stepIndex = ActivityNavigator.getRecipeStepIndex(getActivity().getIntent());
            Log.i(LOG_TAG, "onCreate: Loaded step index from intent " + stepIndex);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(RECIPE_ID_PARAM, recipeId);
        outState.putInt(STEP_INDEX_PARAM, stepIndex);
        Log.i(LOG_TAG, "onSaveInstanceState: Saved stepIndex " + stepIndex);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_step_container, container, false);

        ButterKnife.bind(this, rootView);

        recipeStepAdapter = new RecipeStepPagerAdapter(getFragmentManager());
        recipeStepPager.setAdapter(recipeStepAdapter);
        recipeStepPager.addOnPageChangeListener(new PageChangeListener());

        initLoader();

        return rootView;
    }

    private void initLoader() {
        if ((recipeId != null) && (stepIndex != null)) {
            getLoaderManager().initLoader(LOADER_ID, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                StepTable.CONTENT_URI,
                StepTable.COLUMN_NAMES,
                StepTable.RECIPE_ID_COLUMN + "=?",
                new String[]{Long.toString(recipeId)},
                StepTable.STEP_INDEX_COLUMN);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        recipeStepAdapter.setCursor(cursor);

        // The data is sometimes missing steps.
        buildStepIndexToPositionMap(cursor);

        if ((stepIndex != null) && (stepIndex != -1)) {
            int position = stepIndexToPositionMap.get(stepIndex);
            recipeStepPager.setCurrentItem(position);
        }

        String recipeName = ActivityNavigator.getRecipeName(getActivity().getIntent());
        if (!StringUtil.isEmpty(recipeName)) {
            getActivity().setTitle(recipeName);
        }
    }

    private void buildStepIndexToPositionMap(Cursor cursor) {
        stepIndexToPositionMap.clear();

        SmartCursor smartCursor = new SmartCursor(cursor, StepTable.COLUMN_NAMES);

        if (cursor.moveToFirst()) {
            do {
                int stepIndex = smartCursor.getInt(StepTable.STEP_INDEX_COLUMN);
                stepIndexToPositionMap.put(stepIndex, cursor.getPosition());
            } while (cursor.moveToNext());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        recipeStepAdapter.setCursor(null);
    }

    /**
     * A {@link PagerAdapter} that manages one child fragment per recipe step.
     */
    private class RecipeStepPagerAdapter extends FragmentStatePagerAdapter {

        private Cursor cursor;

        private RecipeStepPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        private void setCursor(Cursor cursor) {
            this.cursor = cursor;

            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            if (cursor == null) {
                return null;
            }

            if (position >= cursor.getCount()) {
                Log.e(LOG_TAG, "getItem: The cursor does not have a step for position " + position);
                return null;
            }

            if (cursor.moveToPosition(position)) {
                SmartCursor smartCursor = new SmartCursor(cursor, StepTable.COLUMN_NAMES);

                long recipeId = smartCursor.getLong(StepTable.RECIPE_ID_COLUMN);
                int stepIndex = smartCursor.getInt(StepTable.STEP_INDEX_COLUMN);
                String shortDescription = smartCursor.getString(StepTable.SHORT_DESCRIPTION_COLUMN);
                String description = smartCursor.getString(StepTable.DESCRIPTION_COLUMN);
                String videoUrl = smartCursor.getString(StepTable.VIDEO_URL_COLUMN);
                String thumbnailUrl = smartCursor.getString(StepTable.THUMBNAIL_COLUMN);

                return RecipeStepIndividualFragment.newInstance(
                        recipeId,
                        stepIndex,
                        shortDescription,
                        description,
                        videoUrl,
                        thumbnailUrl);
            } else {
                Log.e(LOG_TAG, "getItem: Failed to move to the position: " + position);
                return null;
            }
        }

        @Override
        public int getCount() {
            if (cursor == null) {
                return 0;
            } else {
                return cursor.getCount();
            }
        }

        /**
         * Ensures that the {@link PagerAdapter} will actually refresh when the data is invalidated.
         */
        @Override
        public int getItemPosition(Object object){
            return PagerAdapter.POSITION_NONE;
        }

        private Cursor getCursor() {
            return cursor;
        }
    }

    /**
     * A {@link ViewPager.OnPageChangeListener} that updates the current stepIndex, so that
     * rotation to landscape won't forget the current position.
     */
    private class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // Ignore.
        }

        @Override
        public void onPageSelected(int position) {
            Cursor cursor = recipeStepAdapter.getCursor();
            if ((cursor != null) && (cursor.moveToPosition(position))) {
                SmartCursor smartCursor = new SmartCursor(cursor, StepTable.COLUMN_NAMES);
                stepIndex = smartCursor.getInt(StepTable.STEP_INDEX_COLUMN);
                Log.i(LOG_TAG, "onPageSelected: Set stepIndex from cursor: " + stepIndex);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // Ignore.
        }
    }
}
