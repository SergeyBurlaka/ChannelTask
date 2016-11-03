package com.example.kostya.channeltask.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.kostya.channeltask.R;
import com.example.kostya.channeltask.service.LoadAllChannelNameService;

/**
 * Created by kostya on 03.11.16.
 */

public class ActionChooserFragment extends Fragment {
    private OnChooserItemClickedListener mOnChooserItemClickedListener;
    private Button mChannelSelectedButton;

    public interface OnChooserItemClickedListener {
        void onChannelProgramClick();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initServiceDownload();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_action_chooser, container, false);
        mChannelSelectedButton = (Button) view.findViewById(R.id.enter_channel_program_button);

        mChannelSelectedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                channelProgramClick();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChooserItemClickedListener) {
            mOnChooserItemClickedListener = (OnChooserItemClickedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChooserItemClickedListener");
        }
    }


    private void initServiceDownload() {
        Intent intent = new Intent(LoadAllChannelNameService.ACTION_START_LOAD);
        intent.setClass(getContext(), LoadAllChannelNameService.class);
        getActivity().startService(intent);
    }

    private void onItemSelected() {
        if (mOnChooserItemClickedListener != null) {
            mOnChooserItemClickedListener.onChannelProgramClick();
        }
    }

    private void channelProgramClick() {
        onItemSelected();
        mChannelSelectedButton.setVisibility(View.GONE);
    }
}
