package com.example.chatapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Chat_test extends AppCompatActivity {
    String name ="Default";
    Activity activity;
    NestedScrollView scrollView;
    Button button;
    private FirebaseListAdapter<Message> adapter;
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_test);
        name = getIntent().getStringExtra("group");
        TextView tool = (TextView) findViewById(R.id.chat_tool);
        tool.setText(name);
       /* Group[] Arr = (Group[])getIntent().getParcelableArrayExtra("parcel_data");
        Group temp = new Group();
        for (int i = 0; i<10; i++) {
            if (Arr[i].isSelected == 1)
                temp = Arr[i];
        }*/
        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText) findViewById(R.id.editText);
                FirebaseDatabase.getInstance().getReference().child("Rooms").child(name).child("Messages").push()
                        .setValue(new Message(input.getText().toString(),
                                FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));
                input.setText("");
            }
        });
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            displayChat();
        }
    }
    private void displayChat() {
        String name = getIntent().getStringExtra("group");
        Query query = FirebaseDatabase.getInstance().getReference().child("Rooms").child(name).child("Messages");
        ListView listMessages = (ListView)findViewById(R.id.listView);
        FirebaseListOptions<Message> options = new FirebaseListOptions.Builder<Message>()
                .setLayout(R.layout.item)
                .setQuery(query, Message.class)
                .build();
        adapter = new FirebaseListAdapter<Message>(options) {
            @Override
            protected void populateView(View v, Message model, int position) {
                TextView textMessage, autor, timeMessage;
                textMessage = (TextView)v.findViewById(R.id.tvMessage);
                autor = (TextView)v.findViewById(R.id.tvUser);
                timeMessage = (TextView)v.findViewById(R.id.tvTime);
                textMessage.setText(model.getTextMessage());
                autor.setText(model.getAutor());
                timeMessage.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getTimeMessage()));
            }
        };
        listMessages.setAdapter(adapter);

        adapter.startListening();
    }
}
