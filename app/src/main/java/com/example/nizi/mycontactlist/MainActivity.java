package com.example.nizi.mycontactlist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class  MainActivity extends AppCompatActivity {

    private ArrayList<Person> list = new ArrayList<Person>();
    private FragmentManager fragmentManager;
    private Fragment fragmentContatctDetail;
    private Fragment fragmentContactList;
    private FrameLayout fragment_protrait_landscape;
    private Fragment fragmentContatctProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragment_protrait_landscape= (FrameLayout) findViewById(R.id.fragment_landscape);
        list= loadList();
        if(list == null){
            list = new ArrayList<Person>();
            fakeData();
        }
        setListener();
        fragmentContactList=new FragmentListActivity();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_list,fragmentContactList,"contactList").commit();
        if(this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            fragment_protrait_landscape.setVisibility(View.VISIBLE);
        }
        fragmentContatctDetail= new FragmentDetailActivity();
        fragmentManager.beginTransaction().replace(R.id.fragment_landscape,fragmentContatctDetail,"Detail").commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void fakeData() {
        Person lala1=new Person("la", "11111", new ArrayList<Person>());
        ArrayList<Person> lala2_list=new ArrayList<>();
        lala2_list.add(lala1);
        Person lala2=new Person("la2", "22222", lala2_list);
        lala1.relationship.add(lala2);
        ArrayList<Person> lala3_list=new ArrayList<>();
        lala3_list.add(lala1);
        lala3_list.add(lala2);
        Person lala3=new Person("la3", "33333", lala3_list);
        lala1.relationship.add(lala3);
        lala2.relationship.add(lala3);
        list.add(lala1);
        list.add(lala2);
        list.add(lala3);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragment_protrait_landscape.setVisibility(View.VISIBLE);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            fragment_protrait_landscape.setVisibility(View.GONE);
        }
    }

    private void setListener() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        saveList(list);
    }

    public static ArrayList<Person> parseJson(String json) {
        ArrayList<Person> contactList = new ArrayList<Person>();
        try {
            JSONArray tJsonArray = new JSONArray(json);
            ArrayList<ArrayList<Integer>> posiontLists=new ArrayList<>();
            for (int i = 0; i < tJsonArray.length(); i++) {
                JSONObject object = tJsonArray.getJSONObject(i);
                Person contact = new Person(object.getString("name"), object.getString("phoneNum"),new ArrayList<Person>());
                contactList.add(contact);
                String relationshipStr=object.get("relationship").toString();
                JSONArray jsonArrayPosition=new JSONArray(relationshipStr);
                ArrayList<Integer> positions=new ArrayList<Integer>();
                for (int j = 0; j < jsonArrayPosition.length(); j++) {
                    int a = jsonArrayPosition.getInt(j);
                    positions.add(Integer.valueOf(a));
                }
                posiontLists.add(positions);
            }
            for(int i=0;i<posiontLists.size();i++){
                for(int j=0;j<posiontLists.get(i).size();j++){
                    contactList.get(i).relationship.add(contactList.get(posiontLists.get(i).get(j)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contactList;
    }

    public static String jsonEncoder(ArrayList<Person> ContactList) {

        JSONArray tJsonArray = new JSONArray();
        for (Person contact :
                ContactList) {
            JSONObject object = new JSONObject();
            try {
                object.put("name", contact.name);
                object.put("phoneNum", contact.Phone);
                JSONArray reJsonArray = new JSONArray();

                ArrayList<Person> relationship = contact.relationship;
//                ArrayList<Integer> relationshipPos=new ArrayList<Integer>();
                for (Person reContact:relationship) {
                    reJsonArray.put(ContactList.indexOf(reContact));
                }
                object.put("relationship",reJsonArray);

            } catch (Exception e) {
                e.printStackTrace();
            }
            tJsonArray.put(object);
        }
        return tJsonArray.toString();
    }

    public ArrayList<Person> loadList() {

        SharedPreferences settings = getSharedPreferences("ContactApp", MODE_PRIVATE);

        String json = settings.getString("contact_list", "");

        if (json.isEmpty()) {
            return null;
        } else {
            return parseJson(json);
        }


    }

    public void saveList(ArrayList<Person> contactList) {

        SharedPreferences settings = getSharedPreferences("ContactApp", MODE_PRIVATE);

        SharedPreferences.Editor editor = settings.edit();



        String json = jsonEncoder(contactList);

        System.out.println(json);

//        parseJson(json);
//
        editor.putString("contact_list", json);

        editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveList(list);
    }

    public ArrayList<Person> getList(){

        return this.list;
    }

    public void swipToProfileFragment(Person contact){
        fragmentManager.beginTransaction().detach(fragmentContatctDetail).commit();
        if (fragmentContatctProfile!=null){
            ((FragmentProfileActivity)fragmentContatctProfile).setContact(contact);
            ((FragmentProfileActivity)fragmentContatctProfile).updateWidgets();
            fragmentManager.beginTransaction().attach(fragmentContatctProfile).commit();
        }else {
            fragmentContatctProfile=new FragmentProfileActivity(contact);
            fragmentManager.beginTransaction().replace(R.id.fragment_landscape, fragmentContatctProfile,"Profile").commit();
        }

    }

    public void swipToDetailFragment(){

        fragmentContatctDetail = fragmentManager.findFragmentByTag("Detail");
        System.out.println(fragmentContatctDetail);
        if(fragmentContatctProfile!=null){
            fragmentManager.beginTransaction().detach(fragmentContatctProfile).commit();
        }

        fragmentManager.beginTransaction().attach(fragmentContatctDetail).commit();
    }

    public Fragment getFragmentContactList(){

        return fragmentManager.findFragmentByTag("contactList");
    }

    public Fragment getFragmentContatctProfile(){

        return fragmentManager.findFragmentByTag("Profile");
    }

    public Fragment getFragmentContatctDetail(){

        return fragmentManager.findFragmentByTag("Detail");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==-1){

//            list= (ArrayList<Person>) data.getSerializableExtra("contactList");
//            list.add(contact);
            ArrayList<Integer>RelationshipPos= data.getIntegerArrayListExtra("RelationshipPos");
            String name=data.getStringExtra("name");
            String phoneNum=data.getStringExtra("phoneNum");

            ArrayList<Person>input_relationship=new ArrayList<>();
            for (Integer pos:RelationshipPos) {
                input_relationship.add(list.get(pos));
            }



            Person input_contact=new Person(name,phoneNum,input_relationship);

            for (Integer pos:RelationshipPos) {
                list.get(pos).relationship.add(input_contact);
            }

            list.add(input_contact);

            ((FragmentListActivity)fragmentContactList).updateList();

        }else if(resultCode==-2){
            String name=data.getStringExtra("name");
            String phoneNum=data.getStringExtra("phoneNum");

            ((FragmentDetailActivity)getFragmentContatctDetail()).updateInput(name, phoneNum);
        }
    }
}
