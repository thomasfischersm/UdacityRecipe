package com.playposse.udacityrecipe.activity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.playposse.udacityrecipe.R;
import com.playposse.udacityrecipe.data.RecipeContentContract.IngredientTable;
import com.playposse.udacityrecipe.data.RecipeContentContract.RecipeTable;
import com.playposse.udacityrecipe.data.RecipeContentContract.StepTable;
import com.playposse.udacityrecipe.data.RecipePhotoLibrary;
import com.playposse.udacityrecipe.util.FontUtil;
import com.playposse.udacityrecipe.util.RecyclerViewCursorAdapter;
import com.playposse.udacityrecipe.util.SmartCursor;
import com.playposse.udacityrecipe.util.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A {@link Fragment} that shows the recipe and an overview of steps.
 */
public class RecipeFragment extends Fragment {

    private static final int RECIPE_LOADER_ID = 2;
    private static final int INGREDIENT_LOADER_ID = 3;
    private static final int STEP_LOADER_ID = 4;
    private static final String RECIPE_ID_PARAM = "recipeId";
    private static final int GRID_SPAN = 1;

    @BindView(R.id.recipe_background_image_view) ImageView recipeBackgrondImageView;
    @BindView(R.id.recipe_name_text_view) TextView recipeNameTextView;
    @BindView(R.id.ingredient_recycler_view) RecyclerView ingredientRecyclerView;
    @BindView(R.id.step_recycler_view) RecyclerView stepRecyclerView;

    private Long recipeId;
    private String recipeName;

    private RecipeFragmentOwner owner;
    private IngredientAdapter ingredientAdapter;
    private StepAdapter stepAdapter;

//    public static RecipeFragment newInstance(long recipeId) {
//        RecipeFragment fragment = new RecipeFragment();
//        Bundle args = new Bundle();
//        args.putLong(RECIPE_ID_PARAM, recipeId);
//        fragment.setArguments(args);
//        return fragment;
//    }

    void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
        initLoad();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            recipeId = getArguments().getLong(RECIPE_ID_PARAM);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

        ButterKnife.bind(this, rootView);

        // Set up ingredients.
        ingredientRecyclerView.setHasFixedSize(true);
        GridLayoutManager ingredientLayoutManager =
                new GridLayoutManager(getActivity(), GRID_SPAN, GridLayoutManager.VERTICAL, false);
        ingredientRecyclerView.setLayoutManager(ingredientLayoutManager);
        ingredientAdapter = new IngredientAdapter();
        ingredientRecyclerView.setAdapter(ingredientAdapter);

        // Set up cooking steps.
        stepRecyclerView.setHasFixedSize(true);
        GridLayoutManager stepLayoutManager =
                new GridLayoutManager(getActivity(), GRID_SPAN, GridLayoutManager.VERTICAL, false);
        stepRecyclerView.setLayoutManager(stepLayoutManager);
        stepAdapter = new StepAdapter();
        stepRecyclerView.setAdapter(stepAdapter);

        initLoad();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof RecipeFragmentOwner) {
            owner = (RecipeFragmentOwner) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RecipeFragmentOwner");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        owner = null;
    }

    private void initLoad() {
        if (recipeId != null) {
            getLoaderManager().initLoader(RECIPE_LOADER_ID, null, new RecipeLoaderCallbacks());
            getLoaderManager().initLoader(INGREDIENT_LOADER_ID, null, new IngredientLoaderCallbacks());
            getLoaderManager().initLoader(STEP_LOADER_ID, null, new StepLoaderCallbacks());
        }
    }

    /**
     * A {@link LoaderCallbacks} that loads the recipe information.
     */
    private class RecipeLoaderCallbacks implements LoaderCallbacks<Cursor> {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(
                    getActivity(),
                    RecipeTable.CONTENT_URI,
                    RecipeTable.COLUMN_NAMES,
                    RecipeTable.ID_COLUMN + "=?",
                    new String[]{Long.toString(recipeId)},
                    null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            if (cursor.moveToFirst()) {
                SmartCursor smartCursor = new SmartCursor(cursor, RecipeTable.COLUMN_NAMES);

                recipeName = smartCursor.getString(RecipeTable.NAME_COLUMN);
                String imageUrl = smartCursor.getString(RecipeTable.IMAGE_COLUMN);

                recipeNameTextView.setText(recipeName);
                FontUtil.apply(recipeNameTextView, FontUtil.CORMORANT_REGULAR_FONT);

                RecipePhotoLibrary.loadPhoto(
                        getActivity(),
                        recipeBackgrondImageView,
                        imageUrl,
                        recipeName);

                getActivity().setTitle(recipeName);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            // Ignore resets.
        }
    }

    /**
     * A {@link LoaderCallbacks} that loads the ingredient information.
     */
    private class IngredientLoaderCallbacks implements LoaderCallbacks<Cursor> {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(
                    getActivity(),
                    IngredientTable.CONTENT_URI,
                    IngredientTable.COLUMN_NAMES,
                    IngredientTable.RECIPE_ID_COLUMN + "=?",
                    new String[]{Long.toString(recipeId)},
                    null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            ingredientAdapter.swapCursor(cursor);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            ingredientAdapter.swapCursor(null);
        }
    }

    /**
     * A {@link LoaderCallbacks} that loads the cooking step information.
     */
    private class StepLoaderCallbacks implements LoaderCallbacks<Cursor> {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(
                    getActivity(),
                    StepTable.CONTENT_URI,
                    StepTable.COLUMN_NAMES,
                    StepTable.RECIPE_ID_COLUMN + "=?",
                    new String[]{Long.toString(recipeId)},
                    StepTable.STEP_INDEX_COLUMN + " asc");
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            stepAdapter.swapCursor(cursor);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            stepAdapter.swapCursor(null);
        }
    }

    /**
     * An adapter that manages a list of ingredients.
     */
    private class IngredientAdapter extends RecyclerViewCursorAdapter<IngredientViewHolder> {

        @Override
        public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(
                    R.layout.ingredient_list_item,
                    parent,
                    false);
            return new IngredientViewHolder(view);
        }

        @Override
        protected void onBindViewHolder(IngredientViewHolder holder, int position, Cursor cursor) {
            SmartCursor smartCursor = new SmartCursor(cursor, IngredientTable.COLUMN_NAMES);

            String quantity = smartCursor.getString(IngredientTable.QUANTITY_COLUMN);
            String measure = smartCursor.getString(IngredientTable.MEASURE_COLUMN);
            String ingredient = smartCursor.getString(IngredientTable.INGREDIENT_COLUMN);

            String str = getString(R.string.whole_ingredient, quantity, measure, ingredient);

            holder.ingredientTextView.setText(str);
        }
    }

    /**
     * A {@link RecyclerView.ViewHolder} for an ingredient.
     */
    class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredient_text_view) TextView ingredientTextView;

        private IngredientViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * An adapter that manages a list of ingredients.
     */
    private class StepAdapter extends RecyclerViewCursorAdapter<StepViewHolder> {

        @Override
        public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(
                    R.layout.short_step_list_item,
                    parent,
                    false);
            return new StepViewHolder(view);
        }

        @Override
        protected void onBindViewHolder(StepViewHolder holder, int position, Cursor cursor) {
            SmartCursor smartCursor = new SmartCursor(cursor, StepTable.COLUMN_NAMES);

            final int stepIndex = smartCursor.getInt(StepTable.STEP_INDEX_COLUMN);
            String shortDescription = smartCursor.getString(StepTable.SHORT_DESCRIPTION_COLUMN);
            String imageUrl = smartCursor.getString(StepTable.THUMBNAIL_COLUMN);

            String str = getString(R.string.whole_short_step, stepIndex, shortDescription);

            if (stepIndex == 0) {
                holder.stepTextView.setText(shortDescription);
            } else {
                holder.stepTextView.setText(str);
            }

            if (!StringUtil.isEmpty(imageUrl)) {
                holder.stepImageView.setVisibility(View.VISIBLE);
                Glide.with(getActivity())
                        .load(imageUrl)
                        .apply(RequestOptions.overrideOf(Target.SIZE_ORIGINAL))
                        .apply(RequestOptions.circleCropTransform())
                        .into(holder.stepImageView);
            } else {
                holder.stepImageView.setVisibility(View.GONE);
            }

            holder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    owner.onRecipeStepSelected(recipeId, stepIndex, recipeName);
                }
            });
        }
    }

    /**
     * A {@link RecyclerView.ViewHolder} for an ingredient.
     */
    class StepViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_view) LinearLayout rootView;
        @BindView(R.id.step_image_view) ImageView stepImageView;
        @BindView(R.id.step_text_view) TextView stepTextView;

        private StepViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
