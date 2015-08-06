package dk.aau.mppss.friendfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;
import java.util.List;

import dk.aau.mppss.friendfinder.controller.facebook.FacebookController;


public class MainActivity extends AppCompatActivity {
    private FacebookController facebookController;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log.d("Ayoub MainActivity", "onCreate");
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        this.callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);

        //Fix Exception "Performing stop of activity that is not resumed" by placing
        //initializeFacebook inside onCreate instead running it each time on onResume (we only need to register callback one time):
        this.initializeFacebook();
    }

    public void initializeFacebook() {
        this.facebookController = new FacebookController();

        LoginManager.getInstance().logOut();
        LoginButton loginButton = (LoginButton) findViewById(R.id.activity_main_login_button);
        List<String> permissionNeeds = Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends", "user_education_history", "user_work_history"
        );
        loginButton.setReadPermissions(permissionNeeds);

        if(this.callbackManager != null) {
            loginButton.registerCallback(
                    this.callbackManager, new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            //Log.d("FB Login", "Login success");
                            if(facebookController != null) {
                                facebookController.getResultRequest(MainActivity.this);
                                //Log.e("Attention3", facebookController.getUser().toString());
                            }
                            //For switch activities cf getResultRequest function because We must
                            //switch activities only after completed FB Request:
                        }

                        @Override
                        public void onCancel() {
                            //Log.d("FB Login", "Login canceled");
                        }

                        @Override
                        public void onError(FacebookException e) {
                            //Log.e("FB Login", "Login error");
                        }
                    }
            );
        }

        return;
    }

    @Override
    protected void onStop() {
        //Log.d("Ayoub MainActivity", "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //Log.d("Ayoub MainActivity", "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        //Log.d("Ayoub MainActivity", "onPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        //Log.d("Ayoub MainActivity", "onResume");
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.d("Ayoub MainActivity", "onActivityResult");
        if(this.callbackManager != null)
            this.callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
