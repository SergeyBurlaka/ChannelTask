package com.example.kostya.channeltask.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.kostya.channeltask.R;

/**
 * Created by kostya on 03.11.16.
 */

public class ActionChooserFragment extends Fragment {
    private OnChooserFragmentInteractionListener mOnChooserFragmentInteractionListener;
    private Button mChannelSelectedButton;

    public interface OnChooserFragmentInteractionListener {
        void onChannelProgramClick();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        if (context instanceof OnChooserFragmentInteractionListener) {
            mOnChooserFragmentInteractionListener = (OnChooserFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChooserFragmentInteractionListener");
        }
    }




    private void onItemSelected() {
        if (mOnChooserFragmentInteractionListener != null) {
            mOnChooserFragmentInteractionListener.onChannelProgramClick();
        }
    }

    private void channelProgramClick() {
        onItemSelected();
        mChannelSelectedButton.setVisibility(View.GONE);
    }
}
