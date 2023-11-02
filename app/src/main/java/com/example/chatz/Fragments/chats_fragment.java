package com.example.chatz.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatz.Adapters.users_adapters;
import com.example.chatz.DBhelper;
import com.example.chatz.Models.Users;
import com.example.chatz.adder;
import com.example.chatz.databinding.FragmentChatsFragmentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Objects;

public class chats_fragment extends Fragment {

    public chats_fragment() {
    }

    int forone  = 0;
    FragmentChatsFragmentBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    DBhelper dBhelper;
    ArrayList<Users> list = new ArrayList<>();
    DBhelper dbhelper;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentChatsFragmentBinding.inflate(inflater, container, false);
        dBhelper = new DBhelper(getContext());
        dbhelper = new DBhelper(getContext());
        dBhelper.getReadableDatabase();
        dbhelper.getReadableDatabase();

        database = FirebaseDatabase.getInstance();

        String cid = FirebaseAuth.getInstance().getUid();

                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                binding.chatRecyclerView.setLayoutManager(layoutManager);
                users_adapters adapters = new users_adapters(list,getContext());
                binding.chatRecyclerView.setAdapter(adapters);




        database.getReference().child("Users").addValueEventListener
                (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Users users = dataSnapshot.getValue(Users.class);
                    users.setUserid(dataSnapshot.getKey());
                    dBhelper.addusers(users.getUserid(),users.getEmail(),users.getUsername()
                            ,users.getProfilepic());                                                // to get users data from realtime database
                    if(!Objects.equals(cid, users.getUserid()))
                    {
                        list.add(users);
                    }
                    else{
                    }
                }
                adapters.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


                ArrayList<Users> arraylist = dBhelper.fetchdata();
                list.clear();
                for(Users u : arraylist)
                {
                    String username = u.getUsername();
                    String email = u.getEmail();
                    String profilepic = u.getProfilepic();
                    String userid = u.getUserid();
                    if(!userid.equals(cid))                                                         // show users offline
                    {
                        list.add(new Users(username,userid,profilepic,email));
                    }
                    else{
                        continue;
                    }
                }

                adapters.notifyDataSetChanged();





        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(),adder.class);
                startActivity(intent);

            }
        });


        return binding.getRoot();

    }


}
