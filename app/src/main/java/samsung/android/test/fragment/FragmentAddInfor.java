package samsung.android.test.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.zxing.client.android.R;

import java.util.ArrayList;

import samsung.android.test.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAddInfor extends Fragment {
    private ListView lvInfo;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrInfo;

    public FragmentAddInfor() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_info, container, false);
        ((MainActivity) getActivity()).setActionBarTitle("Add Information");
        ((MainActivity) getActivity()).setActionBarSubtitle(null);
        lvInfo = (ListView) v.findViewById(R.id.lv_info);
        arrInfo = new ArrayList<>();
        arrInfo.add("Add Subject");
        arrInfo.add("Add Class");
        arrInfo.add("Add Students");

        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,arrInfo);
        lvInfo.setAdapter(adapter);

        lvInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String grade = adapter.getItem(position);
                switch(grade)
                {
                    case "Add Subject":


                    {
                        FragmentAddSub fragmentAddSub = new FragmentAddSub();
                        FragmentTransaction frag = getFragmentManager().beginTransaction();
                        frag.replace(R.id.frame, fragmentAddSub,"Fragment_addSub");
                        frag.addToBackStack("Fragment_blank");
                        frag.commit();
                        break;
                    }
                    case "Add Class":
                    {
                        FragmentAddClass fragmentAddClass = new FragmentAddClass();
                        FragmentTransaction frag = getFragmentManager().beginTransaction();
                        frag.replace(R.id.frame,fragmentAddClass,"Fragment_addClass");
                        frag.addToBackStack("Fragment_blank");
                        frag.commit();
                        break;
                    }
                    case "Add Students":
                    {
                        FragmentAddStudents fragmentAddStudents = new FragmentAddStudents();
                        FragmentTransaction frag = getFragmentManager().beginTransaction();
                        frag.replace(R.id.frame,fragmentAddStudents,"Fragment_addStudents");
                        frag.addToBackStack("Fragment_blank");
                        frag.commit();
                        break;
                    }
                }
            }
        });
        return v;
    }

}
