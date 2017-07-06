package photoes.k15.photodata.dataStore;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import photoes.k15.photodata.pojo.User;
import photoes.k15.photodata.tools.TransferableContent;

/**
 * Created by root on 7/4/17.
 */

public class SharedPreferenceHelper {

    public static void storeUserInfo(Context context, String userJson){
        SharedPreferences sharedPreferences=context.getSharedPreferences("USER",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(TransferableContent.TRANSFERUSER,userJson);
        editor.commit();
    }

    @Nullable
    public static User getUserFromPreferences(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences(TransferableContent.TRANSFERUSER,
                context.MODE_PRIVATE);
        String content=sharedPreferences.getString(TransferableContent.TRANSFERUSER," ");
        if(content.length()<5){
            return null;
        }
        return TransferableContent.fromJsonToUser(content);
    }
}
