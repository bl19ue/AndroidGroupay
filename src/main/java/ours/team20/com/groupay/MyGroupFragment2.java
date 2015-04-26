package ours.team20.com.groupay;


import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import ours.team20.com.groupay.RESTApi.NodejsCall;
import ours.team20.com.groupay.listadapter.Item;
import ours.team20.com.groupay.listadapter.ItemAdapter;
import ours.team20.com.groupay.models.User;
import ours.team20.com.groupay.singletons.UserSingleton;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyGroupFragment2 extends Fragment implements View.OnClickListener{
    private User user;
    private ArrayList<Integer> groupids;
    private ArrayList<Item> items;
    private ListView myGroupList;
    private Button createGroupButton;

    public MyGroupFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_group2, container, false);
        createGroupButton = (Button) v.findViewById(R.id.create_group);
        createGroupButton.setOnClickListener(this);
        myGroupList = (ListView) v.findViewById(R.id.group_list);
        new GetMyGroupsTask().execute();
        return v;
    }

    private class GetMyGroupsTask extends AsyncTask<Void, Void, JSONArray>{

        @Override
        protected JSONArray doInBackground(Void... params) {
            user = UserSingleton.getCurrentUser();
            String url = "/user/"+ user.getId() +"/groups";
            JSONArray jsonArray = NodejsCall.getArray(url);

            return jsonArray;
        }

        @Override
        public void onPostExecute(JSONArray groups){
            groupids = new ArrayList<>();
            items = new ArrayList<Item>();
            try {
                for (int i = 0; i < groups.length(); i++) {
                    int groupid = groups.getJSONObject(i).getInt("groupid");

                    items.add(new Item(groups.getJSONObject(i).getString("name"),
                            "Money pool : " + Long.toString(groups.getJSONObject(i).getLong("moneypool"))));

                    if(!user.getGroups().contains(groupid)) {
                        groupids.add(groupid);
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }

            user.setGroups(groupids);
            if(!items.isEmpty()){
                ItemAdapter adapter = new ItemAdapter(getActivity(), items);
                myGroupList.setAdapter(adapter);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.create_group: {
                createDialog(v);
                break;
            }
        }
    }

    public void createDialog(View v){
        final Dialog createGroupDialog = new Dialog(getActivity());
        createGroupDialog.setContentView(R.layout.fragment_create_group_dialog);
        createGroupDialog.setTitle("Create a group..");

        Button createButton = (Button) v.findViewById(R.id.confirm);
        Button cancelButton = (Button) v.findViewById(R.id.cancel);
        final EditText group_name = (EditText) v.findViewById(R.id.group_name);
        final EditText frequency_amount = (EditText) v.findViewById(R.id.frequency_amount);
        final Spinner frequency_spinner = (Spinner) v.findViewById(R.id.frequency_spinner);
        final Spinner frequency_type_spinner = (Spinner) v.findViewById(R.id.frequency_type_spinner);

        ArrayAdapter<CharSequence> frequencyAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.frequency, R.layout.support_simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> frequencyTypeAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.frequency_type, R.layout.support_simple_spinner_dropdown_item);

        frequencyAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        frequencyTypeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        frequency_spinner.setAdapter(frequencyAdapter);
        frequency_type_spinner.setAdapter(frequencyTypeAdapter);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(group_name.getText().equals("") || frequency_amount.getText().equals("")){
                    Toast.makeText(getActivity(),
                            "Please enter the name and/or amount", Toast.LENGTH_SHORT).show();
                }

                else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("name", group_name.getText());
                        jsonObject.put("frequency_amount", Long.parseLong(frequency_amount.getText().toString()));
                        jsonObject.put("frequency", Long.parseLong(frequency_spinner.getSelectedItem().toString()));
                        jsonObject.put("frequency_type", frequency_type_spinner.getSelectedItem().toString());
                        new CreateGroup().execute(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGroupDialog.dismiss();
            }
        });
    }

    private class CreateGroup extends AsyncTask<JSONObject, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(JSONObject... params) {
            JSONObject jsonObject = params[0];
            try {
                jsonObject.put("created_at", new Date().toString());
                jsonObject.put("admin", user.getId());

                String url = "/user/" + user.getId() + "/groups";

                JSONObject resJsonObject = NodejsCall.post(url, jsonObject);

                return resJsonObject;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(JSONObject jsonObject){
            if(jsonObject == null){
                Toast.makeText(getActivity(),
                        "There is some problem connecting to the server", Toast.LENGTH_LONG).show();
            }
            else {
                try {
                    user.getGroups().add(Integer.parseInt(jsonObject.getJSONObject("data").getString("groupid")));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
