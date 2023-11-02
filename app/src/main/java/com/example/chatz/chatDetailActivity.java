package com.example.chatz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.chatz.Adapters.chatadapter;
import com.example.chatz.Models.messagemodel;
import com.example.chatz.databinding.ActivityChatDetailBinding;
import com.example.chatz.databinding.SamplesenderBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;




public class chatDetailActivity extends AppCompatActivity {
    ActivityChatDetailBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;

    DBhelper dbhelper;
    private SamplesenderBinding samplesenderBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbhelper = new DBhelper(this);
        dbhelper.getReadableDatabase();

        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        binding.call.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(chatDetailActivity.this, inside_call_activity.class);
//                startActivity(intent);
//            }
//        });

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        final String senderid = auth.getUid();
        String reciverid = getIntent().getStringExtra("userid");
        String profilepic = getIntent().getStringExtra("profilepic");
        String username = getIntent().getStringExtra("username");
        binding.username.setText(username);
        Picasso.get().load(profilepic).placeholder(R.drawable.avatar).into(binding.profilepic);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(chatDetailActivity.this,MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });
        final ArrayList<messagemodel> messagemodels = new ArrayList<>();
        final chatadapter chatadapter = new chatadapter(messagemodels, this);
        binding.chatRecyclerView.setAdapter(chatadapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);


        final String senderRoom = senderid + reciverid;
        final String reciverRoom = reciverid + senderid;



        database.getReference().child("Chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        messagemodels.clear();
                        messagemodel model = null;
                        for (DataSnapshot snapshot1 : snapshot.getChildren())      // this is what happen when a message is recived
                        {
                            model = snapshot1.getValue(messagemodel.class);
                            messagemodels.add(model);
                            binding.chatRecyclerView.scrollToPosition(chatadapter.getItemCount() - 1);
                        }
                        chatadapter.notifyDataSetChanged();

                        try
                        {
                            chatadapter.notifyItemInserted(messagemodels.size()-1);
                            dbhelper.addMessage(senderid, model.getMessage(), reciverid);
//                        messagemodels.clear();
                        }catch(NullPointerException e)
                        {
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.sendin.setAnimation("sendpart2.json");
        MediaPlayer mp;
        mp = MediaPlayer.create(this, R.raw.balsound);
        binding.sendin.setSpeed(2.0f);
        binding.sendin.setOnClickListener(new View.OnClickListener() {            // this is what happen when you click the button
            @Override
            public void onClick(View view) {
                String message = binding.message.getText().toString().trim();
                if (message.isEmpty())
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "An empty message can't be sent", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else
                {
                    binding.sendin.playAnimation();
                    mp.start();
                    final messagemodel model = new messagemodel(senderid, message);
                    model.setTimestamp(new Date().getTime());
                    binding.message.setText("");
                    database.getReference().child("Chats")
                            .child(senderRoom)
                            .push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    binding.chatRecyclerView.scrollToPosition(chatadapter.getItemCount() - 1);
                                    database.getReference().child("Chats")
                                            .child(reciverRoom)
                                            .push()
                                            .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                }
                                            });
                                }
                            });
                }
            }
        });





    }

}