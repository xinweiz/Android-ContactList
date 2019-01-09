package com.example.nizi.mycontactlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    private EditText detail_name;
    private EditText detail_phone;
    private Button btn_confirm;
    private ListView contact_list;
    private MyAdapter contactAdapter;
    private ArrayList<Person> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_detail);

        Intent intent = getIntent();
        list = com.example.nizi.mycontactlist.MainActivity.parseJson(intent.getStringExtra("contactList"));
        String name_in=intent.getStringExtra("name_in");
        String phone_in=intent.getStringExtra("phone_in");
        if(list!=null){
            System.out.println(list.size());
        }else {
            list=new ArrayList<>();
        }
        detail_name = (EditText) findViewById(R.id.detail_name);
        detail_phone = (EditText) findViewById(R.id.detail_phone);
        if(name_in!=null){
            detail_name.setText(name_in);
        }
        if(phone_in!=null){
            detail_phone.setText(phone_in);
        }
        btn_confirm = (Button) findViewById(R.id.detail_add);
        contact_list = (ListView) findViewById(R.id.detail_list);
        contactAdapter = new MyAdapter(this, R.layout.list_item, list,false,true);
        contact_list.setAdapter(contactAdapter);
        setListener();
    }

    private void setListener() {

        contact_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent3 = new Intent(DetailActivity.this, ProfileActivity.class);
                intent3.putExtra("contact", list.get(position));
                startActivity(intent3);
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
            ArrayList<Integer> relationshipPosition = new ArrayList<Integer>();
            for (Integer key : contactAdapter.checkMap.keySet()) {
               if ((Boolean) contactAdapter.checkMap.get(key)) {
                   relationshipPosition.add(contactAdapter.mapPreShow.get(key.intValue()));
               }
            }
            String name = detail_name.getText().toString().trim();
            String phoneNum = detail_phone.getText().toString().trim();
            detail_name.setText("");
            detail_phone.setText("");
            Intent intent2 = new Intent();
            intent2.putExtra("name", name);
            intent2.putExtra("phoneNum", phoneNum);
            intent2.putIntegerArrayListExtra("RelationshipPos", relationshipPosition);
            DetailActivity.this.setResult(RESULT_OK, intent2);
            finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        String name=detail_name.getText().toString().trim();
        String phoneNum=detail_phone.getText().toString().trim();
        Intent intent2 = new Intent();
        intent2.putExtra("name",name);
        intent2.putExtra("phoneNum", phoneNum);
        DetailActivity.this.setResult(-2, intent2);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
