package com.example.mockprojectcampuslink.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mockprojectcampuslink.R;
import com.example.mockprojectcampuslink.activity.MainActivity;
import com.example.mockprojectcampuslink.listener.OnBackPressFragment;
import com.example.mockprojectcampuslink.listener.OnListenerControl;
import com.example.mockprojectcampuslink.model.Music;
import com.example.mockprojectcampuslink.service.MusicControl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FullMusicControlFragment extends Fragment implements OnBackPressFragment {

    private static final String TAG = "FullMusicControl";
    private ImageButton mImageButtonPlay, mImageButtonPause, mImageButtonBack, mImageButtonNext, mImageButtonPrevious;;
    private OnListenerControl mOnControl;
    private ObjectAnimator mObjectAnimatorCircle, mObjectAnimatorLinear;
    private int mDurationSong, mCurrentPosition = 0;
    private SeekBar mSeekBar;
    private final Handler mHandler = new Handler();
    private Runnable mRunnable;
    private CircleImageView mImageView;
    private TextView mTxtRunTime, mTxtFullTime, mTxtSong;
    private boolean mIsState = false;
    private SimpleDateFormat mSimpleDateFormat;
    private LinearLayout linearLayout;
    private int position = 0;
    private ArrayList<Music> arrayList;
    private MusicControlFragment musicControlFragment;
    private Music mMusic;


    public View.OnClickListener onClickBtnPlay = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mIsState) {
                mImageButtonPlay.setVisibility(View.INVISIBLE);
                mImageButtonPause.setVisibility(View.VISIBLE);
                mObjectAnimatorCircle.resume();
                mIsState = false;
                musicControlFragment.setImageButtonPlay();
            }
            if (!mIsState) {
                mHandler.removeCallbacks(mRunnable);
                updateSeekBar();
            }
            mOnControl.onClickedBtnPlay();
        }
    };

    public View.OnClickListener onClickBtnPause = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mIsState == false) {
                mImageButtonPlay.setVisibility(View.VISIBLE);
                mImageButtonPause.setVisibility(View.INVISIBLE);
                mObjectAnimatorCircle.pause();
                mIsState = true;
                musicControlFragment.setImageButtonPause();
            }
            mOnControl.onClickedBtnPause();
        }
    };

    public View.OnClickListener onClickBtnNext = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (arrayList.size() > 0) {
                if (position < arrayList.size()) {
                    position++;
                    if (position > (arrayList.size() - 1)) {
                        position = 0;
                    }
                }
                mMusic = arrayList.get(position);
                ((MainActivity) getActivity()).data(mMusic, arrayList, position);
                mOnControl.onClickedBtnNextAndPrevious();
            }
        }
    };

    public View.OnClickListener onClickBtnPrevious = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (arrayList.size() > 0) {
                if (position < arrayList.size()) {
                    position--;
                    if (position < 0) {
                        position = arrayList.size() - 1;
                    }
                }
                mMusic = arrayList.get(position);
                ((MainActivity) getActivity()).data(mMusic, arrayList, position);
                mOnControl.onClickedBtnNextAndPrevious();
            }
        }
    };

    public void setMaxSeekBar(int duration) {
        if (duration > 0) this.mDurationSong = duration;
        else this.mDurationSong = 0;
    }

    public void setProgressSeekBar(int currentPosition) {
        this.mCurrentPosition = currentPosition;
    }

    public void setOnControl(OnListenerControl onControl) {
        this.mOnControl = onControl;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.full_music_control, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initView(view);
        initData();
        initAction();
    }

    public void updateData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            Music music = bundle.getParcelable("img");
            position = bundle.getInt("i");
            arrayList = bundle.getParcelableArrayList("fragment_list_song_of_author");
            mImageView.setImageBitmap(music.getBitmap());
            mTxtSong.setText(music.getSong());
        }
    }

    public void animationLayout() {
        mObjectAnimatorLinear = ObjectAnimator.ofFloat(linearLayout, "translationY", linearLayout.getHeight(), 0);
        mObjectAnimatorLinear.start();
    }

    public void setImageButtonPause() {
        if (!mIsState) {
            mImageButtonPlay.setVisibility(View.VISIBLE);
            mImageButtonPause.setVisibility(View.INVISIBLE);
            mObjectAnimatorCircle.pause();
            mIsState = true;
        }
        mOnControl.onClickedBtnPause();
    }

    public void setmImageButtonPlay() {
        if (mIsState) {
            mImageButtonPlay.setVisibility(View.INVISIBLE);
            mImageButtonPause.setVisibility(View.VISIBLE);
            mObjectAnimatorCircle.resume();
            mIsState = false;
        }
        if (!mIsState) {
            mHandler.removeCallbacks(mRunnable);
            updateSeekBar();
        }
        mOnControl.onClickedBtnPlay();
    }

    public void updateSeekBar() {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (!mIsState) {
                    if (mCurrentPosition < mDurationSong) {
                        mCurrentPosition += 1000;
                        mSimpleDateFormat = new SimpleDateFormat("m:ss");
                        mTxtRunTime.setText(mSimpleDateFormat.format(mCurrentPosition));
                        mSeekBar.setProgress(mCurrentPosition);
                        mHandler.postDelayed(this, 1000);
                    } else {
                        if (arrayList.size() > 0) {
                            if (position < arrayList.size()) {
                                position++;
                                if (position > (arrayList.size() - 1)) {
                                    position = 0;
                                }
                            }
                            mMusic = arrayList.get(position);
                            ((MainActivity) getActivity()).data(mMusic, arrayList, position);
                            mOnControl.onClickedBtnNextAndPrevious();
                        }
                    }
                }
            }
        };

        mHandler.post(mRunnable);
    }

    private void initView(View view) {
        mImageButtonPlay = (ImageButton) view.findViewById(R.id.imageButtonPlay);
        mImageButtonPause = (ImageButton) view.findViewById(R.id.imageButtonPause);
        mImageButtonBack = (ImageButton) view.findViewById(R.id.btnBack);
        mImageButtonNext = (ImageButton) view.findViewById(R.id.imageButtonForward);
        mImageButtonPrevious = (ImageButton) view.findViewById(R.id.imageButtonRewind);
        mTxtRunTime = (TextView) view.findViewById(R.id.txtRunTime);
        mTxtFullTime = (TextView) view.findViewById(R.id.txtFullTime);
        mSeekBar = (SeekBar) view.findViewById(R.id.seekBar);
        mTxtSong = (TextView) view.findViewById(R.id.txtSongInMC);
        mImageView = (CircleImageView) view.findViewById(R.id.imgSong);
        linearLayout = (LinearLayout) view.findViewById(R.id.layoutMusicControl);
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            Music music = bundle.getParcelable("img");
            position = bundle.getInt("i");
            arrayList = bundle.getParcelableArrayList("fragment_list_song_of_author");

            mImageView.setImageBitmap(music.getBitmap());
            mTxtSong.setText(music.getSong());
            mObjectAnimatorCircle = ObjectAnimator.ofFloat(mImageView, "rotation", 0f, 360f);
            mObjectAnimatorCircle.setDuration(10000);
            mObjectAnimatorCircle.setRepeatCount(ValueAnimator.INFINITE);
            mObjectAnimatorCircle.setRepeatMode(ValueAnimator.RESTART);
            mObjectAnimatorCircle.setInterpolator(new LinearInterpolator());
            mObjectAnimatorCircle.start();
        }
        mSimpleDateFormat = new SimpleDateFormat("m:ss");
        mTxtFullTime.setText(mSimpleDateFormat.format(mDurationSong));
    }

    private void initAction() {
        musicControlFragment = (MusicControlFragment) getFragmentManager().findFragmentByTag("musiccontrol");
        if (musicControlFragment != null) {
            mImageButtonPlay.setOnClickListener(onClickBtnPlay);
            mImageButtonPause.setOnClickListener(onClickBtnPause);
            mImageButtonNext.setOnClickListener(onClickBtnNext);
            mImageButtonPrevious.setOnClickListener(onClickBtnPrevious);
        }

        mImageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullMusicControlFragment fullMusicControlFragment = (FullMusicControlFragment) getFragmentManager().findFragmentByTag("fullmusiccontrol");
                if (fullMusicControlFragment != null) {
                    mObjectAnimatorLinear = ObjectAnimator.ofFloat(linearLayout, "translationY", 0, linearLayout.getHeight());
                    mObjectAnimatorLinear.start();
                }
            }
        });

        mSeekBar.setMax(mDurationSong);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mCurrentPosition = seekBar.getProgress();
                mOnControl.onClickedSeekBar(mCurrentPosition);
            }
        });
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public void onBackPress() {
        Toast.makeText(getContext(), "OK", Toast.LENGTH_SHORT).show();
//        test();
    }

    public void test() {
        mObjectAnimatorLinear = ObjectAnimator.ofFloat(linearLayout, "translationY", 0, linearLayout.getHeight());
        mObjectAnimatorLinear.start();
    }
}
