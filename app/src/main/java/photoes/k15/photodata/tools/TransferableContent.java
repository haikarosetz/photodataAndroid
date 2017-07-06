package photoes.k15.photodata.tools;

import com.google.gson.Gson;
import photoes.k15.photodata.pojo.EventDetailItem;
import photoes.k15.photodata.pojo.LatLongObject;
import photoes.k15.photodata.pojo.ResourceItem;
import photoes.k15.photodata.pojo.User;

/**
 * Created by root on 7/3/17.
 */



public class TransferableContent {

    public static final String TRANSFERRESOURCEITEM="RESOURCEITEM";
    public static final String TRANSFEREVENTDETAIL="EVENTDETAIL";
    public static final String TRANSFERIMAGEPATH="IMAGEPATH";
    public static final String TRANSFERLATLONG="LATLONG";
    public static final String TRANSFERUSER="USER";

    public static final String USER_ID="USER_ID";
    public static final String PAGE="PAGE";


    public static String toJsonObject(EventDetailItem items){
        String stringContent=new Gson().toJson(items);
        return stringContent;
    }

    public static String toJsonObject(User user){
        String stringContent =new Gson().toJson(user);
        return stringContent;
    }


    public static String toJsonObject(ResourceItem items){
        String stringContent=new Gson().toJson(items);
        return stringContent;
    }

    public static String toJsonObject(LatLongObject item){
        String stringContent=new Gson().toJson(item);
        return stringContent;
    }

    public static LatLongObject fromJsonToLatiLangObject(String content){
        return new Gson().fromJson(content,LatLongObject.class);
    }

    public static User fromJsonToUser(String content){
        return new Gson().fromJson(content,User.class);
    }


    public static ResourceItem fromJsonToResourceItem(String content){
        return new Gson().fromJson(content,ResourceItem.class);
    }

    public static EventDetailItem fromJsonToEventDetailItem(String content){
        return new Gson().fromJson(content,EventDetailItem.class);
    }



}
