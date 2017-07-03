package photoes.k15.photodata.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.List;

import photoes.k15.photodata.R;
import photoes.k15.photodata.activity.EventDetailActivity;
import photoes.k15.photodata.pojo.EventDetailItem;
import photoes.k15.photodata.tools.TransferableContent;

/**
 * Created by root on 6/2/17.
 */

public class UploadedDataAdapter extends RecyclerView.Adapter<UploadedDataAdapter.UploadViewHolder> {

    private List<EventDetailItem> uploadedItemPojoList;
    private Context context;

    public UploadedDataAdapter(Context context, List<EventDetailItem> itemPojos){
        this.context=context;
        this.uploadedItemPojoList=itemPojos;
    }

    @Override
    public UploadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item_view,parent,false);
        UploadViewHolder viewHolder=new UploadViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UploadViewHolder holder, int position) {
        EventDetailItem uploadedItemPojo=uploadedItemPojoList.get(position);
        holder.setData(uploadedItemPojo);
    }

    @Override
    public int getItemCount() {
        return uploadedItemPojoList.size();
    }

    public class UploadViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private EventDetailItem uploadedItemPojo;
        private TextView date;
        private TextView count;
        private ImageView image;
        private TextView preview;
        private EventDetailItem item;


        public UploadViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            image=(ImageView) view.findViewById(R.id.image);
            preview=(TextView)view.findViewById(R.id.description);
            count=(TextView)view.findViewById(R.id.count);
            date=(TextView)view.findViewById(R.id.date);

        }
        public void setData(EventDetailItem item){
            this.item=item;
            this.uploadedItemPojo=item;
            this.date.setText(item.getDate().toUpperCase());
            this.count.setText(Integer.toString(item.getResources().size())+"  PHOTOS");
            Glide.with(context).load(item.getResources().get(0))
                    .placeholder(R.drawable.demo)
                    .into(image);
            this.preview.setText(item.getDescription());
        }

        @Override
        public void onClick(View v) {
            Intent intent=new Intent(context, EventDetailActivity.class);
            intent.putExtra(TransferableContent.TRANSFEREVENTDETAIL,TransferableContent.toJsonObject(item));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
