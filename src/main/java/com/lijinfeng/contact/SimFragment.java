package com.lijinfeng.contact;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.provider.Contacts.People;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ljf-梁燕双栖 on 2016/5/9.
 */
public class SimFragment extends ListFragment {

    private ListView listView;
    private SimpleAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new SimpleAdapter(getActivity(),
                getData(),
                R.layout.card_item,
                new String[] { "name", "tel" },
                new int[] { R.id.name, R.id.tel, });
        setListAdapter(adapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sim, container, false);
        listView = (ListView) root.findViewById(android.R.id.list);
        return root;
    }

    private List<? extends Map<String, ?>> getData() {
        List<Map<String,String>> list = new ArrayList<>();

        //获取sim卡联系人
        Uri uri = Uri.parse("content://icc/adn");
        Cursor cur = getActivity().getContentResolver().query(
                uri,
                null ,
                null ,
                null ,
                ContactsContract.Contacts.DISPLAY_NAME
                        + " COLLATE LOCALIZED ASC" );
        if(cur.moveToFirst()) {
            do{
                try {
                    Map<String,String> map = new HashMap<>();
                    int  displayNameColumn = cur.getColumnIndex(People.NAME);
                    int  phoneColumn = cur.getColumnIndex(People.NUMBER);
                    String name = cur.getString(displayNameColumn);
                    map.put("name", name);
                    if(name == null)
                    {
                        continue;
                    }
                    String phone = cur.getString(phoneColumn);
                    if(phone == null)
                    {
                        continue;
                    }
                    map.put("tel",phone);
                    list.add(map);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }while(cur.moveToNext());
        }

        cur.close();
        return list;
    }
}
