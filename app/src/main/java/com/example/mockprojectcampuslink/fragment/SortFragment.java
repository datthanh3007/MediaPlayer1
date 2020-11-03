package com.example.mockprojectcampuslink.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mockprojectcampuslink.R;
import com.example.mockprojectcampuslink.activity.MainActivity;

public class SortFragment extends BottomSheetDialogFragment {

    private RadioGroup mRadioGroup;
    private RadioButton mRadioButtonDefault, mRadioButtonAZ, mRadioButtonZA;
    SharedPreferences preferences;
    public static final String MY_SHARED_PREFERENCES = "MySharedPrefs";
    SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sort_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preferences = getActivity().getSharedPreferences(MY_SHARED_PREFERENCES, getContext().MODE_PRIVATE);
        editor = preferences.edit();

        initView(view);
        initAction();
    }

    private void initAction() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioButtonDefault:
                        ((MainActivity) getActivity()).dataChange("update");
                        break;
                    case R.id.radioButtonAZ:
                        ((MainActivity) getActivity()).dataChange("sortSongAZ");
                        break;
                    case R.id.radioButtonZA:
                        ((MainActivity) getActivity()).dataChange("sortSongZA");
                        break;
                }
                SortFragment sortFragment = (SortFragment) getFragmentManager().findFragmentByTag("sort");
                if (sortFragment != null)
                    sortFragment.dismiss();
                editor.putInt("checkID", mRadioGroup.getCheckedRadioButtonId());
                editor.commit();
            }
        });
    }

    private void initView(View view) {
        mRadioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        mRadioButtonDefault = (RadioButton) view.findViewById(R.id.radioButtonDefault);
        mRadioButtonAZ = (RadioButton) view.findViewById(R.id.radioButtonAZ);
        mRadioButtonZA = (RadioButton) view.findViewById(R.id.radioButtonZA);
        mRadioGroup.check(preferences.getInt("checkID", R.id.radioButtonDefault));
    }
}
