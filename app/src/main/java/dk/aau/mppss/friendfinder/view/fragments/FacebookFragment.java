package dk.aau.mppss.friendfinder.view.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import dk.aau.mppss.friendfinder.MainActivity;
import dk.aau.mppss.friendfinder.R;

/**
 * Created by adibayoub on 30/07/2015.
 */
public class FacebookFragment extends Fragment {
    private LoginButton loginButton;

    public FacebookFragment() {
        this.loginButton = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //false to let android attach automatically fragment to the root view (i.e. parent view):
        View parentView = inflater.inflate(R.layout.fragment_facebook, container, false);

        this.onLoginClickListener(parentView);

        return parentView;
    }

    public void onLoginClickListener(View fragmentView) {
        this.loginButton = (LoginButton) fragmentView.findViewById(R.id.fragment_facebook_login_button);
        if(this.loginButton != null) {
            //Set information access FB profile permission on login button
            this.loginButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            LoginManager.getInstance().logInWithReadPermissions(
                                    (MainActivity) getActivity(),
                                    Arrays.asList("public_profile", "email", "user_birthday", "user_friends", "user_education_history", "user_work_history")
                            );
                        }
                    }
            );
        }
    }
}
