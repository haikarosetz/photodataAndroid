package photoes.k15.photodata.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import photoes.k15.photodata.R;
import photoes.k15.photodata.activity.PhotoViewActivity;
import photoes.k15.photodata.pojo.ResourceItem;
import photoes.k15.photodata.tools.TransferableContent;

/**
 * Created by root on 7/3/17.
 */

public class ResourceItemAdapter extends RecyclerView.Adapter<ResourceItemAdapter.ResourceViewHolder> {


    private List<ResourceItem> resourceItems;
    private Context context;

    public ResourceItemAdapter(Context context, List<ResourceItem> resources){
        this.context=context;
        this.resourceItems=resources;
    }

    @Override
    public ResourceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.resource_item,null);
        ResourceViewHolder holder=new ResourceViewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(ResourceViewHolder holder, int position) {
        ResourceItem item=resourceItems.get(position);
        holder.setData(item);
    }

    @Override
    public int getItemCount() {
        return resourceItems.size();
    }

    public class ResourceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Context context;
        private ResourceItem item;
        private ImageView resourceImage;
        private ImageView deleteView;

        public ResourceViewHolder(View view){
            super(view);
            this.context=view.getContext();
            resourceImage=(ImageView)view.findViewById(R.id.imageView);
            deleteView=(ImageView)view.findViewById(R.id.deleteIcon);
            resourceImage.setOnClickListener(this);
            deleteView.setOnClickListener(this);

        }

        public void setData(ResourceItem item){
            this.item=item;
            Glide.with(context).load(item.getUrl()).placeholder(R.drawable.login)
            .into(resourceImage);
        }


        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.imageView){
                Intent intent=new Intent(context, PhotoViewActivity.class);
                intent.putExtra(TransferableContent.TRANSFERIMAGEPATH,item.getUrl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }
}
