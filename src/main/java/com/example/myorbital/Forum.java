package com.example.myorbital;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Forum extends AppCompatActivity {

    //FireBase
    private FirebaseFirestore databaseReference;
    private CollectionReference userDetails;
    private String userId;
    //logged in
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    // Variables for TravelAdapter and RecycleView
    private RecyclerView recyclerView;
    private ForumAdapter mAdapter;

    private String currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        //Recycler view
        recyclerView = findViewById(R.id.recycler_view_forum);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //display user trip information if they are logged in
            databaseReference = FirebaseFirestore.getInstance();
            userId = mAuth.getUid();
            userDetails = databaseReference.collection("forum");
                    /*.document(userId)
                    .collection("trips");*/
            Log.i("recycle145", "onCreate: ");
        }
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        Query query = userDetails.orderBy("title");
        FirestoreRecyclerOptions<TravelDetails> options = new FirestoreRecyclerOptions.Builder<TravelDetails>()
                .setQuery(query, TravelDetails.class)
                .build();
        mAdapter = new ForumAdapter(options);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        //on item click of the recycler view
        mAdapter.setOnItemClickListener(new ForumAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {
                Log.i("recycling123", "OnItemClick: ");
                TravelDetails travelDetails = documentSnapshot.toObject(TravelDetails.class);
                String id = documentSnapshot.getId();
                Intent i = new Intent(getApplicationContext(), ForumFeedback.class);
                i.putExtra("documentId", id);
                startActivity(i);
            }
        });
        Log.i("recylcing1234", "setupRecyclerView: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser != null) {
            mAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (currentUser != null) {
            mAdapter.stopListening();
        }
    }
}
