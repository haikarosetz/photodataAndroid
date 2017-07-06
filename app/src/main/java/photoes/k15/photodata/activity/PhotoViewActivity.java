package photoes.k15.photodata.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import photoes.k15.photodata.R;
import photoes.k15.photodata.tools.TransferableContent;

public class PhotoViewActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Photo Explorer");


        ImageView promo_image = (ImageView) findViewById(R.id.imageView2);
        Context context = getBaseContext();
        String PATH=getIntent().getStringExtra(TransferableContent.TRANSFERIMAGEPATH);
        Glide.with(context).load(PATH).
                placeholder(android.R.drawable.editbox_dropdown_light_frame).into(promo_image);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

    }
}
