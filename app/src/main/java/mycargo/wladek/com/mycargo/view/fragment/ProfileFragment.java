package mycargo.wladek.com.mycargo.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mycargo.wladek.com.mycargo.R;

/**
 * Created by wladek on 5/25/17.
 */

public class ProfileFragment extends Fragment {
    View myFragmentView;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static ProfileFragment newInstance(int sectionNumber) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        myFragmentView = inflater.inflate(R.layout.profile_fragment_layout, container, false);
        return myFragmentView;
    }
}
