package photoes.k15.photodata.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import photoes.k15.photodata.R;


public class LoginActivity extends AppCompatActivity {
    private AutoCompleteTextView phone_number_view;
    private EditText password_view;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*Intent gpsOptionsIntent = new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(gpsOptionsIntent);*/

        phone_number_view = (AutoCompleteTextView) findViewById(R.id.number);
        password_view = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.login_progress);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.INVISIBLE);

        final Button sign_in_button = (Button) findViewById(R.id.sign_in_button);

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,LandingActivity.class);
                startActivity(intent);
                finish();
                /*startRequest(view);
                InputMethodManager iMmanager=(InputMethodManager)getSystemService((getBaseContext().INPUT_METHOD_SERVICE));
                iMmanager.hideSoftInputFromWindow(progressBar.getWindowToken(),0);*/
            }
        });

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }
    /*public void startRequest(View view) {

        final String email=phone_number_view.getText().toString();
        final String password=password_view.getText().toString();


        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email", phone_number_view.getText());
        params.put("password", password_view.getText().toString().trim());

        client.setConnectTimeout(15000);
        client.setTimeout(15000);

        client.post(getBaseContext(), CommonInformation.POST_MAN_LOGIN, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                if (responseString!=null) {


                    if (Integer.parseInt(responseString)!=0) {

                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        intent.putExtra(PreferenceManager.USER_ID,responseString);
                        UserPreference userPreference=new UserPreference();
                        userPreference.setId(Integer.parseInt(responseString));
                        userPreference.setPassword(password);
                        userPreference.setEmail(email);
                        PreferenceManager.storeUser(userPreference,getBaseContext());
                        startActivity(intent);
                        finish();

                    }else{
                        progressBar.setVisibility(View.INVISIBLE);
                        phone_number_view.setText("");
                        password_view.setText("");
                        Toast.makeText(getBaseContext(),"Wrong username or password",Toast.LENGTH_LONG).show();
                    }

                }else{
                    progressBar.setVisibility(View.INVISIBLE);
                    phone_number_view.setText("");
                    password_view.setText("");
                    Toast.makeText(getBaseContext(),"Wrong username or password",Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                progressBar.setVisibility(View.INVISIBLE);
                Log.e("cause",responseString);
                Toast.makeText(getBaseContext(),"Please try again later", Toast.LENGTH_LONG).show();

            }

        });

    }*/

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
}