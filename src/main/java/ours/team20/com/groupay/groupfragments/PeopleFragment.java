package ours.team20.com.groupay.groupfragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ours.team20.com.groupay.R;
import ours.team20.com.groupay.RESTApi.NodejsCall;
import ours.team20.com.groupay.listadapter.Item;
import ours.team20.com.groupay.listadapter.ItemAdapter;
import ours.team20.com.groupay.models.User;
import ours.team20.com.groupay.singletons.UserSingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleFragment extends Fragment {
    private ArrayList<String> groupids;
    private ListView userListView;
    private ArrayAdapter<String> listAdapter;
    private TextView groupName;

    public PeopleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_people, container, false);
        userListView = (ListView) v.findViewById(R.id.people);
        groupName = (TextView) v.findViewById(R.id.group_name);
        new GroupDetailFetcher().execute();
        return v;
    }

    private class GroupDetailFetcher extends AsyncTask<Void, Void, JSONArray>{

        @Override
        protected JSONArray doInBackground(Void... params) {
            User user = UserSingleton.getCurrentUser();
            String groupid = getActivity().getIntent().getStringExtra("groupid");
//            String url = "/user/" + user.getId()
//                    + "/group/" + groupid;

//            JSONObject groupObject = NodejsCall.get(url);
//            try {
//                groupName.setText(groupObject.getString("name"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            String userListUrl = "/group/" + groupid +"/users";

            //To show the user list
            JSONArray userList = NodejsCall.getArray(userListUrl);


            return userList;
        }

        @Override
        public void onPostExecute(JSONArray userList){
            ArrayList<String> userListString = new ArrayList<>();
            for(int i=0;i<userList.length();i++){
                try {
                    userListString.add(userList.getJSONObject(i).getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, userListString);
            userListView.setAdapter(listAdapter);
        }
    }

}
