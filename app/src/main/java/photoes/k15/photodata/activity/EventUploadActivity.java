package photoes.k15.photodata.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import photoes.k15.photodata.CommonInformation.CommonInformation;
import photoes.k15.photodata.R;
import photoes.k15.photodata.adapter.ResourceItemAdapter;
import photoes.k15.photodata.pojo.LatLongObject;
import photoes.k15.photodata.pojo.ResourceItem;
import photoes.k15.photodata.tools.PathUtil;
import photoes.k15.photodata.tools.TransferableContent;

public class EventUploadActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<ResourceItem> resourceItems=new ArrayList<>();
    private ResourceItemAdapter adapter;
    private LatLongObject latLongObject;
    private ArrayList<Uri> paths=new ArrayList<>();

    private EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_upload);

        latLongObject= TransferableContent.fromJsonToLatiLangObject(getIntent().
                getStringExtra(TransferableContent.TRANSFERLATLONG));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FishBun.with(EventUploadActivity.this)
                .setMaxCount(5)
                .setMinCount(1)
                .setPickerSpanCount(6)
                .setActionBarColor(Color.parseColor("#37474F"), Color.parseColor("#263238"), false)
                .setActionBarTitleColor(Color.parseColor("#ffffff"))
                .setArrayPaths(paths)
                .setAlbumSpanCount(2, 4)
                .setButtonInAlbumActivity(false)
                .setCamera(true)
                .setReachLimitAutomaticClose(false)
                .setHomeAsUpIndicatorDrawable(ContextCompat.getDrawable(getBaseContext(),R.drawable.ic_close))
                .setOkButtonDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_action_tick))
                .setAllViewTitle("All images")
                .setActionBarTitle("Select images ")
                .textOnImagesSelectionLimitReached("Limit Reached!")
                .textOnNothingSelected("Nothing Selected")
                .startAlbum();

        description=(EditText) findViewById(R.id.description);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upload_menu,menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    paths = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                    Toast.makeText(getBaseContext(),Integer.toString(paths.size()),Toast.LENGTH_SHORT).show();
                    for(Uri path:paths){
                        ResourceItem item=new ResourceItem();
                        try {
                            item.setUrl(PathUtil.getPath(getBaseContext(),path));
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        resourceItems.add(item);
                    }

                    adapter=new ResourceItemAdapter(getBaseContext(),resourceItems);
                    LinearLayoutManager layoutManager=new LinearLayoutManager(getBaseContext());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                    break;
                }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.menu.upload_menu){
            Toast.makeText(getBaseContext(),"hey buddy",Toast.LENGTH_LONG).show();
        }

        return true;
    }

    public void upload(){
        String uploadId = UUID.randomUUID().toString();

        UploadNotificationConfig uploadNotificationConfig=new UploadNotificationConfig();
        uploadNotificationConfig.setTitle("uploading image data");
        uploadNotificationConfig.setCompletedIconColor(android.R.color.holo_red_dark);
        uploadNotificationConfig.setCompletedMessage("The upload process completed");
        //Creating a multi part request
        try {

            MultipartUploadRequest uploadRequest=new MultipartUploadRequest(this, uploadId, CommonInformation.UPLOAD_URL)
                    .addParameter("user_name","")
                    .setNotificationConfig(uploadNotificationConfig)
                    .setMaxRetries(2);

            for(Uri uri:paths){
                uploadRequest.addFileToUpload(uri.getPath(), "data[]");
            }
            uploadRequest.startUpload();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


}
