package com.example.mockprojectcampuslink.fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mockprojectcampuslink.R;
import com.example.mockprojectcampuslink.activity.MainActivity;
import com.example.mockprojectcampuslink.listener.OnListenerControl;
import com.example.mockprojectcampuslink.model.Music;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MusicControlFragment extends Fragment {

    private TextView mTxtSongName, mTxtAuthorName;
    private ImageButton mImageButtonPlay, mImageButtonPause, mImageButtonNext, mImageButtonPrevious;
    private OnListenerControl mOnControl;
    private CircleImageView mImageView;
    private RelativeLayout mRelativeLayout;
    private Music music;
    private ObjectAnimator mObjectAnimator;
    FullMusicControlFragment fullMusicControlFragment;
    private int position = 0;
    private ArrayList<Music> arrayList;


    public View.OnClickListener onClickBtnPlay = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            fullMusicControlFragment = (FullMusicControlFragment) getFragmentManager().findFragmentByTag("fullmusiccontrol");
            if (fullMusicControlFragment != null) {
                mImageButtonPlay.setVisibility(View.INVISIBLE);
                mImageButtonPause.setVisibility(View.VISIBLE);
                mObjectAnimator.resume();
                fullMusicControlFragment.setmImageButtonPlay();

                mOnControl.onClickedBtnPlay();
            }

        }
    };

    public View.OnClickListener onClickBtnPause = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            fullMusicControlFragment = (FullMusicControlFragment) getFragmentManager().findFragmentByTag("fullmusiccontrol");
            if (fullMusicControlFragment != null) {
                mImageButtonPlay.setVisibility(View.VISIBLE);
                mImageButtonPause.setVisibility(View.INVISIBLE);
                mObjectAnimator.pause();
                fullMusicControlFragment.setImageButtonPause();

                mOnControl.onClickedBtnPause();
            }

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
                Music music = arrayList.get(position);
                ((MainActivity) getActivity()).data(music, arrayList, position);
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
                Music music = arrayList.get(position);
                ((MainActivity) getActivity()).data(music, arrayList, position);
                mOnControl.onClickedBtnNextAndPrevious();
            }
        }
    };

    public void setOnControl(OnListenerControl onControl) {
        this.mOnControl = onControl;
    }

    public static MusicControlFragment newInstance(Music music, ArrayList<Music> arrayList, int position) {

        Bundle args = new Bundle();
        args.putParcelable("img", music);
        args.putInt("i", position);
        args.putParcelableArrayList("fragment_list_song_of_author", arrayList);
        MusicControlFragment fragment = new MusicControlFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_control, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initAction();
    }

    public void setImageButtonPause() {
            mImageButtonPlay.setVisibility(View.VISIBLE);
            mImageButtonPause.setVisibility(View.INVISIBLE);
            mObjectAnimator.pause();

            mOnControl.onClickedBtnPause();
    }

    public void setImageButtonPlay() {
            mImageButtonPlay.setVisibility(View.INVISIBLE);
            mImageButtonPause.setVisibility(View.VISIBLE);
            mObjectAnimator.resume();

            mOnControl.onClickedBtnPlay();
    }

    private void initAction() {
        mImageButtonPlay.setOnClickListener(onClickBtnPlay);
        mImageButtonPause.setOnClickListener(onClickBtnPause);
        mImageButtonNext.setOnClickListener(onClickBtnNext);
        mImageButtonPrevious.setOnClickListener(onClickBtnPrevious);

        mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullMusicControlFragment fullMusicControlFragment = (FullMusicControlFragment) getFragmentManager().findFragmentByTag("fullmusiccontrol");
                if (fullMusicControlFragment != null) {
                    fullMusicControlFragment.animationLayout();
                }
            }
        });
    }

    private void initView(View view) {
        mTxtSongName = (TextView) view.findViewById(R.id.txtSongName);
        mTxtAuthorName = (TextView) view.findViewById(R.id.txtAuthorName);
        mImageButtonPlay = (ImageButton) view.findViewById(R.id.imageButtonPlay);
        mImageButtonPause = (ImageButton) view.findViewById(R.id.imageButtonPause);
        mImageButtonNext = (ImageButton) view.findViewById(R.id.imageButtonForward);
        mImageButtonPrevious = (ImageButton) view.findViewById(R.id.imageButtonRewind);
        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.rlFull);
        mImageView = (CircleImageView) view.findViewById(R.id.imgSong);
        Bundle bundle = getArguments();
        if (bundle != null) {
            music = bundle.getParcelable("img");
            position = bundle.getInt("i");
            arrayList = bundle.getParcelableArrayList("fragment_list_song_of_author");

            mImageView.setImageBitmap(music.getBitmap());
            mTxtSongName.setText(music.getSong());
            mTxtAuthorName.setText(music.getAuthor());
            mObjectAnimator = ObjectAnimator.ofFloat(mImageView, "rotation", 0f, 360f);
            mObjectAnimator.setDuration(10000);
            mObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mObjectAnimator.setRepeatMode(ValueAnimator.RESTART);
            mObjectAnimator.setInterpolator(new LinearInterpolator()); //Xoay deu
            mObjectAnimator.start();
        }
    }
}
