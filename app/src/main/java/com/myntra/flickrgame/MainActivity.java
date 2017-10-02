package com.myntra.flickrgame;

import android.animation.Animator;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.myntra.flickrgame.api.FlickrRequest;
import com.myntra.flickrgame.api.FlickrResponse;
import com.myntra.flickrgame.api.FlickrService;
import com.myntra.flickrgame.api.ImageData;
import com.myntra.flickrgame.base.ApiCallback;
import com.myntra.flickrgame.base.ApiException;
import com.myntra.flickrgame.util.SquaredImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;

public class MainActivity extends BaseActivity {

    private static final int GAME_DURATION = 15;
    private String mImageUrl;
    private int time;
    private Timer mTimer;
    private Handler mHandler = new Handler();
    private TextView mTextViewTime;
    private GridView gridView;
    private SquaredImageView imageViewRandom;
    private boolean canClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        time = GAME_DURATION;
        setContentView(R.layout.activity_main);
        gridView = (GridView) findViewById(R.id.grid_view);
        imageViewRandom = (SquaredImageView) findViewById(R.id.random_image);
        mTextViewTime = (TextView) findViewById(R.id.timer);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!canClick()) {
                    return;
                }
                String selectedImage = (String) gridView.getAdapter().getItem(position);
                SquaredImageView selectedImageView = (SquaredImageView) view;
                boolean isGameOver = mImageUrl.equals(selectedImage) || isGameOver();
                if (!selectedImageView.isShowing()) {
                    if (isGameOver) {
                        canClick = false;
                        selectedImageView.show(animatorListener);
                    } else {
                        selectedImageView.show();
                    }
                }
            }
        });
        requestData();
    }

    private boolean canClick() {
        return canClick;
    }

    private boolean isGameOver() {
        int childCount = gridView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            SquaredImageView view = (SquaredImageView) gridView.getChildAt(i);
            if (!view.isShowing()) {
                return false;
            }
        }
        return true;
    }

    private DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            time = GAME_DURATION;
            resetGame();
            startGame();
            startTimer();
        }
    };

    private void startGame() {
        FlickrGridViewAdapter adapter = (FlickrGridViewAdapter) gridView.getAdapter();
        adapter.suffleImages();
        mImageUrl = adapter.getRandomImageUrl();
        if (imageViewRandom.getTag() == null) {
            imageViewRandom.setTag(mImageUrl);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    private void startTimer() {
        if (time > 0) {
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }
            mTimer = new Timer();
            mTimer.schedule(getTimerTask(), 1000, 1000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (time > 0) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    private TimerTask getTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (time <= 1) {
                            mTimer.cancel();
                            onTimeComplete();
                        }
                        time -= 1;
                        mTextViewTime.setText(String.valueOf(time));
                    }
                });
            }
        };
    }

    private void onTimeComplete() {
        int childCount = gridView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            SquaredImageView view = (SquaredImageView) gridView.getChildAt(i);
            view.hide();
        }
        if (imageViewRandom != null) {
            imageViewRandom.show();
        }
        canClick = true;
    }

    private void resetGame() {
        canClick = false;
        int childCount = gridView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            SquaredImageView view = (SquaredImageView) gridView.getChildAt(i);
            view.show();
        }
        if (imageViewRandom != null) {
            imageViewRandom.hide(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    imageViewRandom.setTag(mImageUrl);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }

    private Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            animation.removeAllListeners();
            showAlertDialog("Your game is over !!!", listener);
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    private void requestData() {
        showLoadingDialog();
        FlickrRequest request = new FlickrRequest("json");
        FlickrService service = new FlickrService(getApplicationContext());
        service.execute(request, new ApiCallback<FlickrResponse>() {
            @Override
            public void onSuccess(Call<FlickrResponse> call, FlickrResponse response) {
                if (response.getImages().size() < 9) {
                    return;
                }
                ArrayList<String> images = new ArrayList<>();
                List<ImageData> imageDatas = response.getImages();
                int index = 0;
                while (images.size() < 9 && index < imageDatas.size()) {
                    String imageUrl = imageDatas.get(index).getImage();
                    if (!TextUtils.isEmpty(imageUrl)) {
                        images.add(imageUrl);
                    }
                    index += 1;
                }
                final FlickrGridViewAdapter adapter = new FlickrGridViewAdapter(MainActivity.this, images);
                gridView.setAdapter(adapter);
                startGame();
            }

            @Override
            public void onComplete() {
                hideLoadingDialog();
            }

            @Override
            public void onFailure(ApiException e) {
                e.printStackTrace();
            }
        });
    }
}
