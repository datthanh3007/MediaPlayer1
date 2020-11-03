package com.example.mockprojectcampuslink.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mockprojectcampuslink.R;
import com.example.mockprojectcampuslink.model.Music;

public class SettingsFragment extends BottomSheetDialogFragment {

    private TextView mTxtSong, mTxtAuthor;
    private LinearLayout mLinearLayout;
    private RelativeLayout mRelativeLayoutPlayList;
    private ImageView mImageView;
    private ImageButton mImageBtnPlaylist, mImageBtnDelete;
    private Button mBtnCreate, mBtnCancle;
    private Music mMusic;

    public static SettingsFragment newInstance(Music music) {

        Bundle args = new Bundle();
        args.putParcelable("name", music);
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            mMusic = savedInstanceState.getParcelable("name");
        }

        initView(view);
        initData();
        initAction();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("name", mMusic);
    }

    private void initAction() {
        mRelativeLayoutPlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPlaylist();
                mLinearLayout.setVisibility(View.GONE);
            }
        });
    }

    private void dialogPlaylist(){
        final Dialog dialog = new Dialog(getContext());
        if (dialog.getWindow() != null){
            ColorDrawable colorDrawable = new ColorDrawable(Color.TRANSPARENT);
            dialog.getWindow().setBackgroundDrawable(colorDrawable);
        }

        dialog.setContentView(R.layout.dialog_playlist);
        dialog.setCancelable(true);
        dialog.show();

        mBtnCreate = (Button) dialog.findViewById(R.id.btnCreate);
        mBtnCancle = (Button) dialog.findViewById(R.id.btnCancle);

        mBtnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mBtnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void initView(View view) {
        mTxtAuthor = (TextView) view.findViewById(R.id.txtViewAuthor);
        mTxtSong = (TextView) view.findViewById(R.id.txtViewSong);
        mImageView = (ImageView) view.findViewById(R.id.imageSetting);
        mLinearLayout = (LinearLayout) view.findViewById(R.id.test);
        mImageBtnPlaylist = (ImageButton) view.findViewById(R.id.imageBtnPlaylist);
        mImageBtnDelete = (ImageButton) view.findViewById(R.id.imageBtnDelete);
        mRelativeLayoutPlayList = (RelativeLayout) view.findViewById(R.id.relativeLayoutPlayList);
    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null){
            mLinearLayout.setVisibility(View.VISIBLE);
            mMusic = bundle.getParcelable("name");
            mImageView.setImageBitmap(mMusic.getBitmap());
            mTxtAuthor.setText(mMusic.getAuthor());
            mTxtSong.setText(mMusic.getSong());
        }
    }
}
