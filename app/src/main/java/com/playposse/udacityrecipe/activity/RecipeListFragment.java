package com.playposse.udacityrecipe.activity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.playposse.udacityrecipe.R;
import com.playposse.udacityrecipe.data.RecipeContentContract.RecipeTable;
import com.playposse.udacityrecipe.data.RecipePhotoLibrary;
import com.playposse.udacityrecipe.util.FontUtil;
import com.playposse.udacityrecipe.util.RecyclerViewCursorAdapter;
import com.playposse.udacityrecipe.util.ResponsiveGridLayoutManager;
import com.playposse.udacityrecipe.util.SmartCursor;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A {@link Fragment} that shows a list of recipes to the user.
 */
public class RecipeListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_MANAGER = 1;
    private static final String RECYCLER_STATE_KEY = "recyclerLayoutStateKey";

    @BindView(R.id.recipe_recycler_view) RecyclerView recipeRecyclerView;

    private RecipeListFragmentOwner owner;
    private RecipeAdapter recipeAdapter;
    private Parcelable recyclerState;

    public static RecipeListFragment newInstance() {
        return new RecipeListFragment();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        ButterKnife.bind(this, rootView);

        // Small performance improvement.
        recipeRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager =
                new ResponsiveGridLayoutManager(getActivity(), R.dimen.min_grid_item_width);
        recipeRecyclerView.setLayoutManager(layoutManager);
        recipeAdapter = new RecipeAdapter();
        recipeRecyclerView.setAdapter(recipeAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(LOADER_MANAGER, null, this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof RecipeListFragmentOwner) {
            owner = (RecipeListFragmentOwner) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RecipeListFragmentOwner");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        owner = null;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            recyclerState = savedInstanceState.getParcelable(RECYCLER_STATE_KEY);
            if (recyclerState != null) {
                recipeRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerState);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(
                RECYCLER_STATE_KEY,
                recipeRecyclerView.getLayoutManager().onSaveInstanceState());
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                RecipeTable.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor cursor) {
        recipeAdapter.swapCursor(cursor);

        if (recyclerState != null) {
            recipeRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerState);
            recyclerState = null;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        recipeAdapter.swapCursor(null);
    }

    /**
     * An adapter that manages a list of recipes.
     */
    private class RecipeAdapter extends RecyclerViewCursorAdapter<RecipeViewHolder> {

        @Override
        public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(
                    R.layout.recipe_list_item,
                    parent,
                    false);
            return new RecipeViewHolder(view);
        }

        @Override
        protected void onBindViewHolder(final RecipeViewHolder holder, int position, Cursor cursor) {
            SmartCursor smartCursor = new SmartCursor(cursor, RecipeTable.COLUMN_NAMES);
            final long recipeId = smartCursor.getLong(RecipeTable.ID_COLUMN);
            String recipeName = smartCursor.getString(RecipeTable.NAME_COLUMN);
            String imageUrl = smartCursor.getString(RecipeTable.IMAGE_COLUMN);
            int servings = smartCursor.getInt(RecipeTable.SERVINGS_COLUMN);
            String servingsStr = getString(R.string.servings, servings);

            holder.recipeNameTextView.setText(recipeName);
            holder.servingsTextView.setText(servingsStr);

            RecipePhotoLibrary.loadPhoto(
                    getActivity(),
                    holder.recipeImageView,
                    imageUrl,
                    recipeName);

            holder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (owner != null) {
                        owner.onRecipeSelected(recipeId);
                    }
                }
            });

            // Apply custom font.
            FontUtil.apply(holder.recipeNameTextView, FontUtil.CORMORANT_REGULAR_FONT);
            FontUtil.apply(holder.servingsTextView, FontUtil.CORMORANT_REGULAR_FONT);
        }
    }

    /**
     * A {@link RecyclerView.ViewHolder} for a recipe.
     */
    class RecipeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_view) ConstraintLayout rootView;
        @BindView(R.id.recipe_image_view) ImageView recipeImageView;
        @BindView(R.id.recipe_name_text_view) TextView recipeNameTextView;
        @BindView(R.id.servings_text_view) TextView servingsTextView;

        private RecipeViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
