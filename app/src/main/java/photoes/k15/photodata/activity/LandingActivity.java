package photoes.k15.photodata.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import photoes.k15.photodata.CommonInformation.CommonInformation;
import photoes.k15.photodata.R;
import photoes.k15.photodata.adapter.UploadedDataAdapter;
import photoes.k15.photodata.dataStore.SharedPreferenceHelper;
import photoes.k15.photodata.pojo.EventDetailItem;
import photoes.k15.photodata.pojo.LatLongObject;
import photoes.k15.photodata.pojo.ResourceItem;
import photoes.k15.photodata.tools.EndlessRecyclerViewScrollListener;
import photoes.k15.photodata.tools.GPSTracker;
import photoes.k15.photodata.tools.RetryObject;
import photoes.k15.photodata.tools.TransferableContent;

public class LandingActivity extends AppCompatActivity implements RetryObject.ReloadListener{

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private UploadedDataAdapter adapter;
    private List<Object> list=new ArrayList<>();
    private RetryObject retryObject;
    private double latitude ;
    private double longitude ;

    private int PAGE=1;

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
                Manifest.permission.CAMERA,
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
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }

                Intent intent=new Intent(getBaseContext(),EventUploadActivity.class);
                LatLongObject latLongObject=new LatLongObject();
                latLongObject.setLatitude(latitude);
                latLongObject.setLongitude(longitude);
                intent.putExtra(TransferableContent.TRANSFERLATLONG,TransferableContent.toJsonObject(latLongObject));
                startActivity(intent);
            }
        });

        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);
        layoutManager=new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        retryObject = RetryObject.getInstance(this);
        retryObject.setListener(this);

        adapter=new UploadedDataAdapter(getBaseContext(),list);
        recyclerView.setAdapter(adapter);
        doTask(CommonInformation.EVENT_DETAIL_URL,PAGE,list);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                PAGE=page+1;
                doTask(CommonInformation.EVENT_DETAIL_URL,PAGE,list);
                Log.e("the page",Integer.toString(page));

            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==android.R.id.home){
            finish();
        }
        return true;
    }

    public List<Object> getList(){

        List<Object> itemPojos=new ArrayList<>();
        List<ResourceItem> resource=new ArrayList<>();

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


    public void doTask(final String url, final int page,final List<Object> categories){

        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        //params.put(TransferableContent.USER_ID,SharedPreferenceHelper.getUserFromPreferences(getBaseContext()).getId());
        params.put(TransferableContent.PAGE,page);

        client.post(getBaseContext(), url, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                retryObject.hideMessage();
                retryObject.hideName();
                retryObject.showProgress();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                retryObject.hideMessage();
                retryObject.hideProgress();
                Toast.makeText(getBaseContext(),throwable.toString(),Toast.LENGTH_LONG).show();
                Log.e("the reason",throwable.toString());
                if(categories.size()<1){
                    retryObject.showName();
                    retryObject.showMessage();
                }

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                retryObject.hideProgress();
                retryObject.hideMessage();
                retryObject.hideName();
                Log.e("content to display",responseString);
                Type listType = new TypeToken<List<EventDetailItem>>() {}.getType();
                List<EventDetailItem> postList = new Gson().fromJson(responseString, listType);
                categories.addAll(postList);
                adapter.notifyDataSetChanged();

                if(responseString.length()<5 && categories.size()<1){
                    retryObject.hideMessage();
                    retryObject.hideProgress();
                    retryObject.hideName();
                }

            }
        });
    }

    @Override
    public void onReloaded(String message) {
        doTask(CommonInformation.EVENT_DETAIL_URL, PAGE,list);
    }


}
