package com.example.nizi.mycontactlist;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FragmentProfileActivity extends Fragment {
    private View rootView;
    private ListView profile_list;
    private MyAdapter relationshipAdapter;
    private ArrayList<Person> contactList;
    private Person p;
    private TextView profile_name;
    private TextView profile_phone;
    private ImageView show_contact_img;



    public Person getContact() {
        return p;
    }

    public void setContact(Person contact) {
        this.p = contact;
    }

    @SuppressLint("ValidFragment")
    public FragmentProfileActivity(Person contact) {
        this.p=contact;
    }

    public FragmentProfileActivity(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_profile, container, false);
        profile_list = (ListView) rootView.findViewById(R.id.profile_list);
        profile_name= (TextView) rootView.findViewById(R.id.profile_name);
        profile_phone= (TextView) rootView.findViewById(R.id.profile_phone);
        profile_name.setText(p.name);
        profile_phone.setText(p.Phone);
        contactList=this.p.relationship;
        relationshipAdapter = new MyAdapter(getContext(), R.layout.list_item,contactList,true,false);
        profile_list.setAdapter(relationshipAdapter);
        profile_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity)getContext()).swipToProfileFragment(contactList.get(position));
            }
        });
        return rootView;
    }

    public void updateWidgets(){
        profile_name.setText(p.name);
        profile_phone.setText(p.Phone);
        contactList=this.p.relationship;
        relationshipAdapter=null;
        relationshipAdapter=new MyAdapter(getContext(),R.layout.list_item,contactList,true,false);
        profile_list.setAdapter(relationshipAdapter);
    }

    public void updatelist(){
        relationshipAdapter.notifyDataSetChanged();
    }

}
