package com.example.nizi.mycontactlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    private ListView profile_list;
    private MyAdapter relationshipAdapter;
    private ArrayList<Person> contactList;
    private Person p;
    private TextView profile_name;
    private TextView profile_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_profile);

        Intent intent = getIntent();
        contactList = com.example.nizi.mycontactlist.MainActivity.parseJson(intent.getStringExtra("contact"));
        String name_in = intent.getStringExtra("name_in");
        String phone_in = intent.getStringExtra("phone_in");
        profile_list = (ListView) this.findViewById(R.id.profile_list);
        profile_name= (TextView) this.findViewById(R.id.profile_name);
        profile_phone= (TextView) this.findViewById(R.id.profile_phone);
        if(contactList!=null){
            System.out.println(contactList.size());
        }else {
            contactList=new ArrayList<>();
        }
        profile_name.setText(name_in);
        profile_phone.setText(phone_in);
        relationshipAdapter = new MyAdapter(this, R.layout.list_item,contactList,true,false);
        profile_list.setAdapter(relationshipAdapter);

        profile_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                intent.putExtra("contact", contactList.get(position));
                startActivity(intent);
            }
        });
    }
}
