package com.example.chatapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.chatapp.Group;
import com.example.chatapp.R;
import com.google.firebase.database.FirebaseDatabase;

public class Chat_add extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_add);
        Button addbtn = (Button)findViewById(R.id.addChat);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText chat_name = (EditText) findViewById(R.id.chat_name);
                EditText chat_pass = (EditText) findViewById(R.id.chat_pass);
                Group temp = new Group();
                temp.name = chat_name.getText().toString();
                temp.pass = chat_pass.getText().toString();
                FirebaseDatabase.getInstance().getReference().child("Rooms").child(temp.name).child("Password").setValue(temp.pass);
                finish();
            }
        });
    }

}
