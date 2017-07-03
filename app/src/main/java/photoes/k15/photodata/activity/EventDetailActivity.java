package photoes.k15.photodata.activity;

import android.app.usage.UsageEvents;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import photoes.k15.photodata.R;
import photoes.k15.photodata.adapter.ResourceItemAdapter;
import photoes.k15.photodata.pojo.EventDetailItem;
import photoes.k15.photodata.pojo.ResourceItem;
import photoes.k15.photodata.tools.TransferableContent;

public class EventDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<ResourceItem> resourceItems=new ArrayList<>();
    private ResourceItemAdapter adapter;
    private EventDetailItem item;
    private TextView eventDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);


        item= TransferableContent.fromJsonToEventDetailItem(getIntent().
                getStringExtra(TransferableContent.TRANSFEREVENTDETAIL));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        for(String resource:item.getResources()){
            ResourceItem resource1=new ResourceItem();
            resource1.setUrl(resource);
            resourceItems.add(resource1);
        }

        eventDetail=(TextView)findViewById(R.id.textView2);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);

        eventDetail.setText(item.getDescription());

        adapter=new ResourceItemAdapter(getBaseContext(),resourceItems);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }


}
