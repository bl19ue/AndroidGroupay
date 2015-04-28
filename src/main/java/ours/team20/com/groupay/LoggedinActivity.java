package ours.team20.com.groupay;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.facebook.AccessToken;

import java.io.IOException;

import ours.team20.com.groupay.imagedownloader.DownloadImage;


public class LoggedinActivity extends ActionBarActivity {
    private Fragment fragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggedin);

        displayView(0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_loggedin, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case R.id.action_logout:
                logout();
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout(){
        AccessToken.setCurrentAccessToken(null);
        Intent intent = new Intent(LoggedinActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void setProfileImage(String src) throws IOException {
        ImageView iv = (ImageView)findViewById(R.id.image_profile);
        new DownloadImage(iv).execute(src);
    }

    private void displayView(int position){

        fragment = null;
        switch (position){
            case 0: {

                fragment = new MyGroupFragment2();
                break;
            }
//            case 1: {
//                fragment = new ShowCircleFragment();
//                break;
//            }
            default:{
                fragment = new MyGroupFragment2();
                break;
            }
        }

        attachFragment(position);

    }


    private void attachFragment(int position){

        if(fragment != null){


            FragmentManager fragmentManager = getFragmentManager();


            fragmentManager.beginTransaction().replace(R.id.loggedin_fragments, fragment).commit();


//            drawerList.setItemChecked(position, true);
//            drawerList.setSelection(position);
//            setTitle(menuTitles[position]);
//            drawerLayout.closeDrawer(drawerList);
        }
        else{

            Log.e("EnteredActivity", "Error in creating fragment");
        }
    }


}
