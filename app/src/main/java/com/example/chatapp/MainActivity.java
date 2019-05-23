package com.example.chatapp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static int SIGN_IN_REQUEST_CODE = 1;
    String[] groupArr = new String[3];
    final List<String> groups = new ArrayList<String>();
    Group[] Arr2 =new Group [10];
    Group gr1 = new Group("Default");
    Group gr2 = new Group("Room 1");
    Group gr3 = new Group("Room 2");

    @Override                                                   // Авторизация
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView list = (ListView) findViewById(R.id.groupView);
        ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.shit, groups);
        TextView text = (TextView) findViewById(R.id.group_name);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .build(),SIGN_IN_REQUEST_CODE
            );
        }
        FirebaseDatabase bd = FirebaseDatabase.getInstance();
        DatabaseReference ref = bd.getReference();
        DatabaseReference ref2 = ref.child("Rooms");
        ref.child("Rooms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count " ,""+snapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String gr = postSnapshot.getKey();
                    int n = 0;
                    for (int i = 0;i<groups.size();i++){
                        if (groups.get(i).equals(gr))
                            n++;
                    }
                    if (n==0)
                        groups.add(gr);
                        list.requestLayout();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        int n = 2;
        list.setAdapter(adapter);
       /* for (int i = 0; i < 3; i++) {
            groupArr[i] = Arr2[i].getName();
            list.setAdapter(adapter);
        }*/
        ref.child("Rooms").child("Room 4").child("Password").setValue(" ");
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String temp;
                for (int i = 0; i < groups.size(); i++) {
                    temp = (String) parent.getItemAtPosition(position);
                    if (temp.equals(groups.get(i))) {
                        Intent intent1 = new Intent(MainActivity.this, Chat_test.class);
                        intent1.putExtra("group", temp);
                        startActivity(intent1);
                        break;
                    }
                }
            }
        });
    }
    public void addNew(View view) {
        Intent i = new Intent(MainActivity.this, Chat_add.class);
        startActivity(i);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {             //НА старте активити получаем код
                                                                                                //авторизации и выводим сообщение о том что вход выполнен
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                Snackbar.make(this.findViewById(R.id.Main), "Вход выполнен", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(this.findViewById(R.id.Main), "Вход не выполнен", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
