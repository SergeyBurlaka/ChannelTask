package com.example.kostya.channeltask.fragment.accelerometer_fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kostya.channeltask.FirebaseHelper;
import com.example.kostya.channeltask.R;
import com.example.kostya.channeltask.holder.acc_holders.SessionHolder;
import com.example.kostya.channeltask.model.acc_model.Session;
import com.example.kostya.channeltask.prefs.PrefManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

public class AccelerometerSessionFragment extends Fragment {

    private static final String TODAYS_DATE = "04:11:2016";
    private static final String TAG = AccelerometerSessionFragment.class.getSimpleName();
    private OnSessionClickListener mOnSessionClickListener;
    
    public interface OnSessionClickListener {
        void onSessionClick(String sessionName);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSessionClickListener) {
            mOnSessionClickListener = (OnSessionClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSessionClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        initAccelerometerDataList(recyclerView);

        return view;
    }

    private void initAccelerometerDataList(RecyclerView recyclerView) {
        DatabaseReference reference = FirebaseHelper.getDataFromAllAccelerometerSessions();
        int sessionId = PrefManager.getPrefManager().getLastSessionNumber(getContext());

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Session, SessionHolder>(Session.class,
                R.layout.fragment_accelerometer_item, SessionHolder.class,
                reference.orderByChild("date").startAt("session0").endAt("session" + sessionId)) {
            @Override
            protected void populateViewHolder(SessionHolder sessionHolder, Session session, int position) {
                sessionHolder.setDate(session.getDate());
                sessionClick(sessionHolder, session);
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void sessionClick(final SessionHolder holder, final Session session) {
        holder.setOnSessionClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnSessionClickListener.onSessionClick(session.getDate());
                String date = session.getDate();
                Log.i(TAG, date);
            }
        });
    }
}
