package com.lijinfeng.contact;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
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
public class CardFragment extends ListFragment {

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
        View root = inflater.inflate(R.layout.fragment_card,container, false);
        listView = (ListView) root.findViewById(android.R.id.list);
        return root;
    }

    private List<? extends Map<String, ?>> getData() {
        List<Map<String,String>> list = new ArrayList<>();

        Cursor cur = getActivity().getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null ,
                null ,
                null ,
                ContactsContract.Contacts.DISPLAY_NAME
                        + " COLLATE LOCALIZED ASC" );
        if(cur.moveToFirst())
        {
            do{
                Map<String, String> map = new HashMap<>();
                int  idColumn = cur.getColumnIndex(ContactsContract.Contacts._ID);
                int  displayNameColumn = cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                // 获得联系人的ID号
                String contactId = cur.getString(idColumn);
                // 获得联系人姓名
                String disPlayName = cur.getString(displayNameColumn);
                System.out.println(disPlayName);
                map.put("name",disPlayName);
                // 查看该联系人有多少个电话号码。如果没有这返回值为0
                int  phoneCount = cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if(phoneCount <1)
                {
                    continue;
                }
                Cursor phones = getActivity().getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null ,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                + " = "  + contactId,  null ,  null );
                if  (phones.moveToFirst())
                {
                    do  {
                        // 遍历所有的电话号码
                        String phoneNumber = phones
                                .getString(phones
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        int phoneType = phones
                                .getInt(phones
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        if(phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                        {
                            map.put("tel",phoneNumber);
                            list.add(map);
                            break;
                        }
                    } while  (phones.moveToNext());

                }

            }while(cur.moveToNext());
        }
        cur.close();
        return list;
    }

}
