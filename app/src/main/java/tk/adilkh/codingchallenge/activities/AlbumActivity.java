package tk.adilkh.codingchallenge.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

import tk.adilkh.codingchallenge.R;
import tk.adilkh.codingchallenge.models.Photo;

public class AlbumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
    }

    // get all photos of an album
    public static void getAllPhotos (String albumId){
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+albumId+"/photos?fields=picture{url}",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        Log.d("response", "onCompleted: " + response);
                    }
                }
        ).executeAsync();
    }

    // get a specific photo
    public static Photo getPhoto (String PhotoId){
        final Photo p = new Photo();
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+PhotoId+"/?fields=images",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        JSONObject data = response.getJSONObject();
                        try {
                            p.setHighResUrl(data.getJSONArray("images").get(0).toString());
                            p.setLowResUrl(data.getJSONArray("images").get(5).toString());
                            p.setId(data.get("id").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
        return p ;
    }

}
