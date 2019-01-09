package com.example.nizi.mycontactlist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class FragmentDetailActivity extends Fragment {
    private View rootView;
    private ArrayList<Person> list;
    private Button detail_add;
    private ListView contact_list;
    private MyAdapter myAdapter;
    private EditText detail_name;
    private EditText detail_phone;
    private ImageView input_contact_img;


    public FragmentDetailActivity() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_detail, container, false);

        list=((MainActivity)getContext()).getList();
        detail_name= (EditText) rootView.findViewById(R.id.detail_name);
        detail_phone= (EditText) rootView.findViewById(R.id.detail_phone);
        detail_add= (Button) rootView.findViewById(R.id.detail_add);
        contact_list= (ListView) rootView.findViewById(R.id.detail_list);
        myAdapter = new MyAdapter(getContext(), R.layout.list_item,list,false,true);
        contact_list.setAdapter(myAdapter);

        setListeners();
        return rootView;
    }

    private void setListeners() {

        contact_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainActivity)getContext()).swipToProfileFragment(list.get(position));
            }
        });

        detail_add.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if (detail_name.getText().toString().trim().isEmpty() || detail_phone.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getContext(),"phone or name cannot be empty",Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<Person> relationship = new ArrayList<Person>();
                for (Integer key : myAdapter.checkMap.keySet()) {
                    if ((Boolean) myAdapter.checkMap.get(key)) {
                        relationship.add(list.get(key));
                    }
                }

                Person addContact = new Person(detail_name.getText().toString().trim(), detail_phone.getText().toString().trim(), relationship);

                list.add(addContact);

                for (Person contact : relationship) {
                    contact.relationship.add(addContact);
                }

                myAdapter.removeAllCheck();
                myAdapter.notifyDataSetChanged();
                ((FragmentListActivity) ((MainActivity) getContext()).getFragmentContactList()).updateList();
                detail_name.setText("");
                detail_phone.setText("");


            }

        });
    }

    public void updateList(){

        myAdapter.notifyDataSetChanged();
    }

    public void updateInput(String name,String phone){
        detail_name.setText(name);
        detail_phone.setText(phone);
    }

    public String getInputName(){

        return detail_name.getText().toString().trim();
    }

    public String getInputPhone(){

        return detail_phone.getText().toString().trim();
    }


}
