package ours.team20.com.groupay;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import ours.team20.com.groupay.models.User;
import ours.team20.com.groupay.singletons.UserSingleton;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private EditText nameText;
    private Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameText = (EditText) findViewById(R.id.name);
        loginButton = (Button) findViewById(R.id.login);

        loginButton.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login: {
                //Authenticate through facebook
                new LoginRest().execute();
            }
        }
    }

    //To make rest request for logging into the app
    private class LoginRest extends AsyncTask<Void, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(Void... params) {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPostReq = new HttpPost("http://10.189.175.158:3000/user/login");
            User user = new User(1, "Sumit", "svalecha91@gmail.com");
            JSONObject jsonObject = null;
            try {
                StringEntity se = new StringEntity(user.toJSONObject().toString());
                se.setContentType("application/json;charset=UTF-8");
                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
                httpPostReq.setEntity(se);
                HttpResponse httpResponse = httpClient.execute(httpPostReq);
                String responseText = null;
                responseText = EntityUtils.toString(httpResponse.getEntity());
                jsonObject = new JSONObject(responseText);


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonObject;
        }

        @Override
        public void onPostExecute(JSONObject userObj){
            User user = null;
            if(userObj != null)
                try {
                    user = new User(userObj.getJSONObject("data").getInt("userid"),
                            userObj.getJSONObject("data").getString("name"),
                            userObj.getJSONObject("data").getString("email"));
                    UserSingleton userSingleton = new UserSingleton(user);
                    Intent goToLoggedinIntent = new Intent(MainActivity.this, LoggedinActivity.class);
                    startActivity(goToLoggedinIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

        }
    }

}
