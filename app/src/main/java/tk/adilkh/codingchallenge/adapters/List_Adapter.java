package tk.adilkh.codingchallenge.adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.GraphRequest;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import tk.adilkh.codingchallenge.R;
import tk.adilkh.codingchallenge.models.Album;

public class List_Adapter extends BaseAdapter {

    Context c ;
    ArrayList<Album> albums ;

    public List_Adapter( Context c, ArrayList<Album> albums) {
        this.albums = albums ;
        this.c = c;
    }

    @Override
    public int getCount() {
        return (albums == null) ? 0 : albums.size();
    }

    @Override
    public Object getItem(int position) {
        return albums.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Integer.parseInt(albums.get(position).getId()) ;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup viewGroup) {
        ViewHolder holder;
        if(convertview == null) {
            convertview = LayoutInflater.from(c).inflate(R.layout.list_item_layout,viewGroup,false);
            holder = new ViewHolder();
            holder._bg = (ImageView) convertview.findViewById(R.id.img_background);
            holder._grid_item_image = (ImageView) convertview.findViewById(R.id.grid_item_image);
            holder._grid_item_title = (TextView) convertview.findViewById(R.id.grid_item_title);
            holder._vw_blayer =  convertview.findViewById(R.id.vw_blacklayer);
            convertview.setTag(holder);
        } else {
            holder = (ViewHolder) convertview.getTag();
        }
        holder._grid_item_title.setText(albums.get(position).getName());
        Picasso.with(c)
                .load(albums.get(position).getUrl())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into( holder._bg);
        Picasso.with(c)
                .load(albums.get(position).getUrl())
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder)
                .into(holder._grid_item_image);

        ObjectAnimator fade = ObjectAnimator.ofFloat(holder._vw_blayer, View.ALPHA, 1f,.3f);
        fade.setDuration(500);
        fade.setInterpolator(new LinearInterpolator());
        fade.start();
        return convertview;
    }

}