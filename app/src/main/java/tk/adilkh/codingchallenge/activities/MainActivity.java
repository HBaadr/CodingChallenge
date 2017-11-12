package tk.adilkh.codingchallenge.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;

import tk.adilkh.codingchallenge.R;
import tk.adilkh.codingchallenge.network.API;

public class MainActivity extends AppCompatActivity {

    private String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Test if the user is not logged in if not send it to the login activity
        if (AccessToken.getCurrentAccessToken() == null){
            goToLoginScreen();
        }

        // Get the user Id of the user that logged in to be used later
        UserId = AccessToken.getCurrentAccessToken().getUserId();

        // make the API call
        API.getAllAlbums(UserId);


    }



    // Go to the login Activity
    private void goToLoginScreen() {
        Intent intent = new Intent(this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(intent);
    }

    // Logout of the facebook session
    private void logout(){
        LoginManager.getInstance().logOut();
        goToLoginScreen();
    }


}
