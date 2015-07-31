package dk.aau.mppss.friendfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import dk.aau.mppss.friendfinder.controller.facebook.FacebookController;
import dk.aau.mppss.friendfinder.view.fragments.FacebookFragment;


public class MainActivity extends ActionBarActivity {
    private FacebookController facebookController;
    private FacebookFragment facebookFragment;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_main);

        this.facebookFragment = new FacebookFragment();
        if(this.facebookFragment != null) {
            this.attachFragment(this.facebookFragment);
            this.initializeFacebook();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(this.callbackManager != null)
            this.callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void initializeFacebook() {
        LoginManager.getInstance().logOut();

        this.facebookController = new FacebookController();
        this.callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(
                this.callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(
                                "FB Login", "Login success" + loginResult.getRecentlyDeniedPermissions() + "-" + loginResult
                                        .getRecentlyGrantedPermissions()
                        );
                        if(facebookController != null)
                            facebookController.getResultRequest();
                        Intent switchMapActivity = new Intent(MainActivity.this, MapsActivity.class);
                        MainActivity.this.startActivity(switchMapActivity);
                    }

                    @Override
                    public void onCancel() {
                        //Log.d("FB Login", "Login canceled");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        //Log.e("FB Login", "Login error");
                    }
                }
        );

        return;
    }

    //Attach (set) a fragment to the activity view:
    private void attachFragment(Fragment fragment) {
        if(isFinishing() == false) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_fb_container, fragment)
                    .commitAllowingStateLoss();
        }

        return;
    }
}
