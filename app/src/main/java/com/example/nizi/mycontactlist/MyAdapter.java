package com.example.nizi.mycontactlist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private boolean isProfile;
    public void setList(List<Person> list) {
        this.list = list;
    }
    private boolean isDetail;
    private List<Person> list;
    public HashMap<Integer,Boolean> checkMap;
    private ArrayList<Integer> selected;
    public HashMap<Integer,Integer>mapPreShow;

    public MyAdapter(Context context, int resource, List<Person> list,boolean isProfile,boolean isDetail) {
        super(context, resource, list);
        this.context = context;
        this.resource = resource;
        this.list = list;
        this.isDetail=isDetail;
        this.isProfile=isProfile;
        checkMap=new HashMap<>();
        selected=new ArrayList<>();
        mapPreShow=new HashMap<>();
        for(int i=0;i<list.size();i++){
            mapPreShow.put(i,i);
        }
        for (int i = 0; i <this.list.size() ; i++) {
            checkMap.put(i,false);
        }
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        convertView = View.inflate(this.context, R.layout.list_item, null);
        TextView sub_task_title = (TextView) convertView.findViewById(R.id.list_item);
        final CheckBox task_check = (CheckBox) convertView.findViewById(R.id.list_del);
        if (checkMap.containsKey(position) && checkMap.get(position)==true){
            task_check.setChecked(true);
        }
        if(!isProfile){
            task_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkMap.put(position,task_check.isChecked());
                    if(isDetail){
                        HashMap<Integer,Integer>mapPreShow_=new HashMap<Integer, Integer>();
                        if(task_check.isChecked()){
                            ArrayList<Person> relationship_check = list.get(position).relationship;
                            for (Person contact_check:relationship_check) {
                                checkMap.put(list.indexOf(contact_check),true);
                            }
                        }
                        ArrayList<Integer> checked=new ArrayList<Integer>();
                        ArrayList<Integer> unchecked=new ArrayList<Integer>();
                        for (Map.Entry<Integer,Boolean> entry : checkMap.entrySet()) {
                            Integer key = entry.getKey();
                            Boolean value = entry.getValue();
                            if (value){
                                checked.add(key);
                            }
                        }
                        for (int k=0;k<list.size();k++){
                            if(!checked.contains(k)){
                                unchecked.add(k);
                            }
                        }
                        Collections.sort(checked);
                        Collections.sort(unchecked);
                        checkMap=new HashMap<Integer, Boolean>();
                        int insertPos=0;
                        for (Integer pos:
                                checked) {
                            Person contactInsert = list.get(pos);
                            list.remove(contactInsert);
                            list.add(insertPos, contactInsert);
                            mapPreShow_.put(insertPos,mapPreShow.get(pos));
                            checkMap.put(insertPos,true);

                            insertPos+=1;
                        }
                        for (Integer pos:
                                unchecked) {
                            mapPreShow_.put(insertPos,mapPreShow.get(pos));
                            insertPos+=1;
                        }
                        mapPreShow=mapPreShow_;
                        MyAdapter.this.notifyDataSetChanged();
                    }
                }
            });
            task_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                }
            });
        }else {
            task_check.setVisibility(View.GONE);
        }
        sub_task_title.setText(list.get(position).name);
        return convertView;
    }
    public void removeAllCheck(){
        checkMap=new HashMap<>();
    }
}
