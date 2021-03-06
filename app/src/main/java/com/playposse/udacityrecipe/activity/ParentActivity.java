package com.playposse.udacityrecipe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.playposse.udacityrecipe.R;
import com.playposse.udacityrecipe.util.FontUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A base {@link Activity} that implements common functionality across all activities.
 */
public abstract class ParentActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.toolbar_title_text_view) TextView toolbarTitleTextView;

    protected abstract int getContentLayoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getContentLayoutId());

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayShowTitleEnabled(false);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        }

        toolbarTitleTextView.setText(R.string.app_name);
        FontUtil.apply(toolbarTitleTextView, FontUtil.CORMORANT_BOLD_FONT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        toolbarTitleTextView.setText(title);
    }

    protected void hideToolbar() {
        toolbar.setVisibility(View.GONE);
    }
}
