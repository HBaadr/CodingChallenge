package tk.adilkh.codingchallenge.network;

import android.util.Log;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;


public class API {

    // get all albums of the user
    public static void getAllAlbums (String UserID){
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+UserID+"/albums",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d("response", "onCompleted: " + response);
                    }
                }
        ).executeAsync();
    }
}
