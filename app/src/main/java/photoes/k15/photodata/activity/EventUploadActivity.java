package photoes.k15.photodata.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

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
                .setMaxCount(30)
                .setMinCount(1)
                .setPickerSpanCount(6)
                .setActionBarColor(getResources().getColor(R.color.colorPrimary),getResources().getColor(R.color.colorPrimaryDark),false)
                .setActionBarTitleColor(getResources().getColor(R.color.textLightPrimary))
                .setArrayPaths(paths)
                .setAlbumSpanCount(1, 4)
                .setButtonInAlbumActivity(false)
                .setCamera(true)
                .setReachLimitAutomaticClose(false)
                .setHomeAsUpIndicatorDrawable(ContextCompat.getDrawable(getBaseContext(),R.drawable.ic_close))
                .setOkButtonDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.ic_action_tick))
                .setAllViewTitle("All images")
                .setActionBarTitle("Select photos ")
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
                    if(paths.size()<1){
                        finish();
                    }
                    for(Uri path:paths){
                        ResourceItem item=new ResourceItem();
                        try {
                            item.setPath(PathUtil.getPath(getBaseContext(),path));
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
                }else{
                    finish();
                }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.upload){
            uploadMultipart();
        }

        return true;
    }

    public void uploadMultipart() {

        try {
            String uploadId = UUID.randomUUID().toString();

            UploadNotificationConfig uploadNotificationConfig=new UploadNotificationConfig();
            uploadNotificationConfig.setTitle("Uploading");
            uploadNotificationConfig.setCompletedIconColor(android.R.color.holo_red_dark);
            uploadNotificationConfig.setCompletedMessage("Uploading process completed");
            //Creating a multi part request
            MultipartUploadRequest request=new MultipartUploadRequest(this, uploadId,CommonInformation.UPLOAD_URL);
            for(int i=0;i<resourceItems.size();i++){
                        request.addFileToUpload(resourceItems.get(i).getPath(),"PHOTO[]");
            }
             request.addParameter("DESCRIPTION",description.getText().toString().trim())
            .addParameter("LATITUDE",Double.toString(latLongObject.getLatitude()))
            .addParameter("LONGITUDE",Double.toString(latLongObject.getLongitude()))
                     .addParameter("USER_ID",Integer.toString(1))
                     .addParameter("DATE","SOME DATE")
            .setNotificationConfig(uploadNotificationConfig)
            .setMaxRetries(2);

            request.startUpload();

            LayoutInflater inflater=getLayoutInflater();
            View customToastroot =inflater.inflate(R.layout.haikarose_toast_view, null);
            TextView textView=(TextView)customToastroot.findViewById(R.id.toast_message);
            textView.setText("You will be notified once upload complete!");
            Toast customtoast=new Toast(getBaseContext());
            customtoast.setView(customToastroot);
            customtoast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM,0, 12);
            customtoast.setDuration(Toast.LENGTH_LONG);
            customtoast.show();

            finish();

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


}
