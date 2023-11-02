package com.example.chatz;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.chatz.Adapters.users_adapters;
import com.example.chatz.Models.Users;
import com.example.chatz.databinding.ActivityAdderBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class adder extends AppCompatActivity {
    ActivityAdderBinding binding;
    ArrayList<Users> list = new ArrayList<>();
    ArrayList<Users> searchlist;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityAdderBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        users_adapters adapters = new users_adapters(list,this);
        binding.chatRecyclerView.setAdapter(adapters);


        database = FirebaseDatabase.getInstance();
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users users = dataSnapshot.getValue(Users.class);
                    assert users != null;
                    users.setUserid(dataSnapshot.getKey());
                    list.add(users);
                }
                adapters.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchlist = new ArrayList<>();
                if(newText.length()>0)
                {
                    for (int i = 0; i < list.size() ; i++)
                    {
                        if(list.get(i).getUsername().contains(newText))
                        {
                            Users users = new Users();
                            users.setUsername(list.get(i).getUsername());
                            users.setProfilepic(list.get(i).getProfilepic());
                            searchlist.add(users);
                        }
                    }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(adder.this);
                    binding.chatRecyclerView.setLayoutManager(layoutManager);
                    users_adapters adapters = new users_adapters(searchlist,adder.this);
                    binding.chatRecyclerView.setAdapter(adapters);
                }
                else
                {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(adder.this);
                    binding.chatRecyclerView.setLayoutManager(layoutManager);
                    users_adapters adapters = new users_adapters(list,adder.this);
                    binding.chatRecyclerView.setAdapter(adapters);
                    }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

            }
        }