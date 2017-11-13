package tk.adilkh.codingchallenge.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import tk.adilkh.codingchallenge.R;
import tk.adilkh.codingchallenge.models.Photo;

public class PhotoActivity extends AppCompatActivity {

    ImageView fullScreenImageView ;
    private String photoId;
    private String photoUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        // Initialize all Views
        initViews();

        // API call to get a specific photo
        getPhoto(photoId);

    }

    // Initialize all Views
    private void initViews() {
        // get intent Data (Album Id )
        Intent intent = getIntent();
        photoId = intent.getStringExtra("id");
        // init teh Image View
        fullScreenImageView = (ImageView) findViewById(R.id.fullScreenImageView);
    }


    // get a specific photo
    public void getPhoto (String PhotoId){
        final Photo p = new Photo();
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+PhotoId+"/?fields=images",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            JSONObject data = response.getJSONObject();
                            JSONObject j = (JSONObject) data.getJSONArray("images").get(0);
                            photoUrl = j.get("source").toString();
                            Picasso.with(PhotoActivity.this)
                                    .load(j.get("source").toString())
                                    .placeholder(R.drawable.image_placeholder)
                                    .error(R.drawable.image_placeholder)
                                    .into(fullScreenImageView);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.main, menu);
        return (super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.uploadToFireBase) {
            Toast.makeText(this, "Upload : " + photoUrl, Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
