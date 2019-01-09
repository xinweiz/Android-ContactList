package com.example.nizi.mycontactlist;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Collections;

public class FragmentListActivity extends Fragment {
    private View rootView;
    private Button app_add;
    private Button app_del;
    private ListView contact_list;
    private MyAdapter myAdapter;
    private ArrayList<Person> contactList;
    private int clickedPos=-1;


    public void setContactList(ArrayList<Person> contactList) {
        this.contactList = contactList;
    }

    public FragmentListActivity() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_list, container, false);
        app_add = (Button) rootView.findViewById(R.id.app_add);
        app_del = (Button) rootView.findViewById(R.id.app_del);
        contact_list = (ListView) rootView.findViewById(R.id.app_list);
        contactList = ((MainActivity) getContext()).getList();
        myAdapter = new MyAdapter(getContext(), R.layout.list_item, contactList,false,false);
        contact_list.setAdapter(myAdapter);
        setListeners();
        return rootView;
    }
    private void setListeners() {
        app_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                String json = MainActivity.jsonEncoder(contactList);
                intent.putExtra("contactList", json);
                FragmentDetailActivity fragmentContactDetail= (FragmentDetailActivity) ((MainActivity)getContext()).getFragmentContatctDetail();
                intent.putExtra("name_in",fragmentContactDetail.getInputName());
                intent.putExtra("phone_in",fragmentContactDetail.getInputPhone());
                startActivityForResult(intent, 0);
            } else if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                ((MainActivity) getContext()).swipToDetailFragment();
            }
            }
        });
        app_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            ArrayList<Integer> deletePosition = new ArrayList<Integer>();
            for (Integer key : myAdapter.checkMap.keySet()) {
                if ((Boolean) myAdapter.checkMap.get(key)) {
                    deletePosition.add(key);
                }
            }
            Collections.sort(deletePosition);
            Collections.reverse(deletePosition);
            for (Integer position : deletePosition) {
                Person removeContact = contactList.get(position);
                ArrayList<Person> relationship = removeContact.relationship;
                for (Person contact : relationship) {
                    contact.relationship.remove(removeContact);
                }
                contactList.remove(position.intValue());
                myAdapter.checkMap.remove(position);
            }
            updateList();
            Fragment fragmentContatctDetail = ((MainActivity) getContext()).getFragmentContatctDetail();
            Fragment fragmentContatctProfile = ((MainActivity) getContext()).getFragmentContatctProfile();
            if (fragmentContatctDetail != null) {
                ((FragmentDetailActivity) fragmentContatctDetail).updateList();
            }
            if (fragmentContatctProfile != null) {
                ((FragmentProfileActivity) fragmentContatctProfile).updatelist();
            }
            if(getContext().getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
                for (Integer pos:deletePosition) {
                    if(pos.equals(Integer.valueOf(clickedPos))){
                        clickedPos=-1;
                        ((MainActivity)getContext()).swipToDetailFragment();
                        break;
                    }
                }
                if(clickedPos!=-1){
                    for (Integer pos:deletePosition){
                        if(pos<clickedPos){
                            clickedPos-=1;
                        }
                    }
                }
            }else {
                clickedPos=-1;
            }
            }
        });
        contact_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                clickedPos = position;
                ((MainActivity) getContext()).swipToProfileFragment(contactList.get(position));
            } else if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra("contact", String.valueOf(contactList.get(position)));
                startActivity(intent);
            }
            }
        });
    }
    public void updateList() {
        myAdapter.notifyDataSetChanged();
    }
}

