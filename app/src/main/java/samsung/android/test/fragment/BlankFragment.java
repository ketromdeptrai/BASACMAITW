package samsung.android.test.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import com.google.zxing.client.android.R;

import samsung.android.test.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {


    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_blank, container, false);
        /*Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((ActionBarActivity)getActivity()).setSupportActionBar(toolbar);
        getActivity().setTitle("my title");*/
        //getActivity().setActionBarTitle("Your title");
        //getActivity().getActionBar().setTitle("YOUR TITLE");
        ((MainActivity) getActivity()).setActionBarTitle("Attendance Tracking");
        ((MainActivity) getActivity()).setActionBarSubtitle(null);
        return v;
    }

}

