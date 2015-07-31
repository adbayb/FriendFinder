package dk.aau.mppss.friendfinder.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dk.aau.mppss.friendfinder.R;

/**
 * Created by adibayoub on 31/07/2015.
 */
public class FacebookFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_facebook, container, false);

        return parentView;
    }
}
