package ru.symdeveloper.SeGALLERY;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.etsy.android.grid.StaggeredGridView;


public class MainActivity extends ActionBarActivity {
    private static final String LOG_TAG = "MainActivity";

    private SegalEntitiesStorage mEntitiesStorage;
    private SegalAdapter mAdapter;
    private StaggeredGridView mGridView;
    private LinearLayout mResetButtonPanel;
    private Button mResetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEntitiesStorage = new SegalEntitiesStorage(App.instance().getDiskCache());

        mGridView = (StaggeredGridView) findViewById(R.id.grid_view);
        mAdapter = new SegalAdapter(this, mEntitiesStorage.getEntitiesList());
        mGridView.setAdapter(mAdapter);

        mResetButtonPanel = (LinearLayout) findViewById(R.id.layout_button);
        mResetButton = (Button) findViewById(R.id.reset_button);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tryToUpdateImages()) {
                    showMessage(getString(R.string.no_internet_connection));
                }
            }
        });

        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
//                    ImageLoader.getInstance().pause();
//                } else {
//                    ImageLoader.getInstance().resume();
//                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount > 0) {
                    //scroll progress in percents
                    int progress = (100 * (firstVisibleItem + visibleItemCount)) / totalItemCount;
                    if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                        progress = 100;
                    } else {
                        if (firstVisibleItem == 0) {
                            progress = 0;
                        }
                    }
                    float alpha = 1 - ((float) progress / 100);
                    //mResetButtonPanel.setAlpha(alpha);
                    setAlphaForView(mResetButtonPanel, alpha);
                }
            }
        });
        tryToUpdateImages();
    }

    private void setAlphaForView(View v, float alpha) {
        AlphaAnimation animation = new AlphaAnimation(alpha, alpha);
        animation.setDuration(0);
        animation.setFillAfter(true);
        v.startAnimation(animation);
    }

    private boolean tryToUpdateImages() {
        if (App.instance().isNetworkAvailable()) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(displayMetrics);
            int screenWidth = displayMetrics.widthPixels;
            int screenHeight = displayMetrics.heightPixels;
            mEntitiesStorage.reset(screenWidth, screenHeight);

            mAdapter.setItems(mEntitiesStorage.getEntitiesList());
            mAdapter.notifyDataSetChanged();
            return true;
        }
        return false;
    }

    private void showMessage(String text) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(text)
                .setCancelable(false)
                .setPositiveButton(R.string.text_ok, null)
                .create();
        dialog.show();
    }
}
