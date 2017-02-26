package com.androidproj.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Chat extends AppCompatActivity {

    private Bundle b;
    private String myUid, key, withName, myUserName;
    private ListView lst;
    private AdapterMessege adapter;
    private DatabaseReference data;
    private ArrayList<Message> arr;
    private Button btnSend;
    private EditText edtChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getFormWidgets();

        addEventFormWidgets();

    }

    private void addEventFormWidgets() {
        data.child("Users").child(myUid).child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myUserName = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        data.child("Messeger").child(key).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                arr.add(dataSnapshot.getValue(Message.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                if(edtChat.getText().toString() != null);
                Message m = new Message(edtChat.getText().toString(), myUserName, myUid);
                data.child("Messeger").child(key).push().setValue(m);
                edtChat.setText("");
            }
        });
    }

    private void getFormWidgets() {

        data = FirebaseDatabase.getInstance().getReference();
        b = getIntent().getBundleExtra("user");
        key = b.getString("key");
        myUid = b.getString("uid");
        withName = b.getString("chatWithName");
        edtChat = (EditText) findViewById(R.id.edtChatText);
        btnSend = (Button) findViewById(R.id.btnSend);

        lst = (ListView) findViewById(R.id.lstvChat);
        arr = new ArrayList<>();
        adapter = new AdapterMessege(this, R.layout.adapter_messeger, arr, myUid);
        lst.setAdapter(adapter);
        setTitle(withName);
    }
}
