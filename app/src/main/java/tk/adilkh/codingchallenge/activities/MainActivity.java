package tk.adilkh.codingchallenge.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tk.adilkh.codingchallenge.R;
import tk.adilkh.codingchallenge.adapters.List_Adapter;
import tk.adilkh.codingchallenge.models.Album;
import tk.adilkh.codingchallenge.models.Photo;

public class MainActivity extends AppCompatActivity {

    TextView userName ;
    ListView listview ;
    List_Adapter list_Adapter ;;
    private String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Test if the User is not logged in if not send it to the login activity
        if (AccessToken.getCurrentAccessToken() == null){
            goToLoginScreen();
        }

        // / Get the User Id of the User that logged in to be used later
        UserId = AccessToken.getCurrentAccessToken().getUserId();

        // Initilize the Views
        initViews();

        // make the API call
        getUserInfo(UserId);
        getAllAlbums(UserId);


        // Listen for a click on the list view
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                // get the clicked Album and pass it's ID to the Album Activity to display the photos of the album
                Album album = (Album) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this , AlbumActivity.class);
                intent.putExtra("id",album.getId());
            }
        });
    }

    private void initViews() {
        userName = (TextView)findViewById(R.id.userName);
        listview = (ListView) findViewById(R.id.Listview);
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

    // Get the user name and Email
    public  void getUserInfo (String UserID){
        // Make the API Call
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+UserID+"?fields=email,name",
                null,
                HttpMethod.GET ,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            // Get the response and parse the data into a JSONObject Object
                            JSONObject data = response.getJSONObject();
                            // Set the TextView Text attribute to the user name
                            userName.setText(data.get("name").toString() + "\n" + data.get("email").toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync(); // Execute the request asynchronously
    }

    // get all albums of the User
    public  void getAllAlbums (String UserID){
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+UserID+"/albums?fields=name,picture{url}",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            // Get the response and parse the data into a JSONObject Object
                            JSONObject data = response.getJSONObject();

                            // Create and Arraylist to hold the response
                            ArrayList <Album> albums = new ArrayList<Album>();

                            // iterate over the response that we got from the API
                            JSONArray jsonArray = (JSONArray) data.getJSONArray("data");
                            for (int i = 0, size = jsonArray.length(); i < size; i++)
                            {
                                JSONObject objectInArray = jsonArray.getJSONObject(i);
                                albums.add(new Album(
                                        objectInArray.get("name").toString(),
                                        objectInArray.getJSONObject("picture").getJSONObject("data").get("url").toString(),
                                        objectInArray.get("id").toString())
                                );
                            }

                            // Set the adapter with the Data that we Got from the API
                            list_Adapter = new List_Adapter(MainActivity.this,albums);

                            // Set the List view's adapter
                            listview.setAdapter(list_Adapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();  // Execute the request asynchronously
    }


}
