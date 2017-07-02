package photoes.k15.photodata.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import java.util.ArrayList;
import java.util.List;

import photoes.k15.photodata.R;
import photoes.k15.photodata.adapter.UploadedDataAdapter;
import photoes.k15.photodata.pojo.EventDetailItem;
import photoes.k15.photodata.tools.GPSTracker;

public class LandingActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private UploadedDataAdapter adapter;
    private List<EventDetailItem> list;

    GPSTracker gps;




    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.MEDIA_CONTENT_CONTROL,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gps = new GPSTracker(LandingActivity.this);
                // check if GPS enabled
                if(gps.canGetLocation()){
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
            }
        });

        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        layoutManager=new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        list=getList();
        adapter=new UploadedDataAdapter(getBaseContext(),list);
        recyclerView.setAdapter(adapter);



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==android.R.id.home){
            finish();
        }
        return true;
    }

    public List<EventDetailItem> getList(){

        List<EventDetailItem> itemPojos=new ArrayList<>();
        List<String> resource=new ArrayList<>();
        resource.add("one");
        resource.add("four");
        resource.add("anotherresource");

        for(int i=0;i<15;i++){
            EventDetailItem item=new EventDetailItem();
            item.setDate("june 2 , 2017");
            item.setLatitude("123478.987");
            item.setLongitude(("3498289.8797"));
            item.setDescription(getResources().getString(R.string.large_text));
            item.setResources(resource);
            itemPojos.add(item);
        }
        return itemPojos;
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
