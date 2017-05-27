package samsung.android.test.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import android.view.View.OnClickListener;
import com.example.ketromdeptrai.database.Database;
import com.google.zxing.client.android.R;

import samsung.android.test.MainActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAddSub extends Fragment {
    public FragmentAddSub() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_addsub, container, false);
        ((MainActivity) getActivity()).setActionBarTitle("Add Subject");
        ((MainActivity) getActivity()).setActionBarSubtitle(null);
        Button button = (Button) view.findViewById(R.id.btnAddSub);
        button.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String addSubID = (((EditText)view.findViewById(R.id.addsub_subID)).getText()+"").toUpperCase().trim();
                String addSubName = ((EditText)view.findViewById(R.id.addsub_subName)).getText()+"";
                Database db = new Database(getActivity());
                //  Toast.makeText(getActivity(), db.ERROR_REASON, Toast.LENGTH_LONG).show();
                String msg = db.insertSubject(addSubID, addSubName);
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                db.close();
            }
        });
        return view;
    }

   /* public void btnAddSubject(View v){
       *//*String addSubID = ((EditText)v.findViewById(R.id.addsub_subID)).getText()+"";
        String addSubName = ((EditText)v.findViewById(R.id.addsub_subName)).getText()+"";
        Database db = new Database(getActivity());
        String msg = db.insertSubject(addSubID, addSubName);*//*
       Toast.makeText(getActivity(), "vcl", Toast.LENGTH_LONG).show();
    }*/
}
