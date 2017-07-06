package photoes.k15.photodata.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import photoes.k15.photodata.CommonInformation.CommonInformation;
import photoes.k15.photodata.R;
import photoes.k15.photodata.dataStore.SharedPreferenceHelper;
import photoes.k15.photodata.pojo.User;
import photoes.k15.photodata.tools.TransferableContent;


public class LoginActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private String username=null;
    private String password=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        User user=SharedPreferenceHelper.getUserFromPreferences(getBaseContext());
        if(user!=null) {
            Intent intent=new Intent(getBaseContext(),LandingActivity.class);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(intent);
            finish();
        }

        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.INVISIBLE);

        final Button sign_in_button = (Button) findViewById(R.id.sign_in_button);

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username=usernameEditText.getText().toString().trim();
                password=passwordEditText.getText().toString().trim();
                login(getBaseContext()
                        ,username,
                        password,
                        CommonInformation.LOGIN_URL);
                InputMethodManager iMmanager=(InputMethodManager)getSystemService((getBaseContext().INPUT_METHOD_SERVICE));
                iMmanager.hideSoftInputFromWindow(progressBar.getWindowToken(),0);
            }
        });

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            finish();
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void login(final Context context, String username, String password, String url){

        AsyncHttpClient httpClient=new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("username",username);
        params.put("password",password);

        httpClient.post(context,url, params,new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(usernameEditText.getContext(),throwable.toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                Log.e("response",responseString);
                if(responseString.length()<8){
                    progressBar.setVisibility(View.INVISIBLE);
                    if(responseString.equalsIgnoreCase("none")){
                        Snackbar.make(usernameEditText,"Invalid information",Snackbar.LENGTH_LONG).show();
                        usernameEditText.setText("");
                        passwordEditText.setText("");
                    }

                }else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent intent=new Intent(getBaseContext(),LandingActivity.class);
                    User user=TransferableContent.fromJsonToUser(responseString);
                    if(user!=null){
                        SharedPreferenceHelper.storeUserInfo(getBaseContext(), TransferableContent.toJsonObject(user));
                        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                        startActivity(intent);
                        finish();
                    }else{
                        Snackbar.make(usernameEditText,"Invalid information",Snackbar.LENGTH_LONG).show();
                    }

                }


            }
        });

    }


}