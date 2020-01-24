package com.example.myorbital;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class ExpenseTracker extends AppCompatActivity implements CommentDialog.CommentDialogListener {

    private List<ExpenseDetails> expenseDetailsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ExpenseTrackerAdapter mAdapter;

    private TravelDetails travelDetails;

    private TextView balance;
    private TextView spending;
    private ImageView itemCat;
    private Button endTripBtn;

    private long totalSpending;
    private long budget;

    //FireBase
    private FirebaseFirestore databaseReference;
    private String userID;
    private DocumentReference tripProfile;
    private String documentId;

    private Intent homeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_tracker);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        homeIntent = new Intent(getApplicationContext(), MainActivity.class);

        endTripBtn = findViewById(R.id.endTripBtn);

        //Recycler view
        recyclerView = findViewById(R.id.recycler_view_expenseTracker);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setting each views to its respective IDs
        balance = findViewById(R.id.budgetBalance);
        spending = findViewById(R.id.spending);
        itemCat = findViewById(R.id.itemCategory);

        databaseReference = FirebaseFirestore.getInstance();
        documentId = getIntent().getExtras().getString("documentId");
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        tripProfile = FirebaseFirestore.getInstance()
                .collection("user Profile")
                .document(userID)
                .collection("trips")
                .document(documentId);

        tripProfile.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    travelDetails = documentSnapshot.toObject(TravelDetails.class);
                    totalSpending = (long) travelDetails.getTotalSpending();
                    String s = "Spending: " + Long.toString(totalSpending);
                    spending.setText(s);

                    budget = (long) travelDetails.getBudget();
                    String b = "Balance: " + Long.toString(budget);
                    balance.setText(b);

                } else {
                    showMessage("Document does not exist");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage("Error");
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ExpenseActivity.class);
                intent.putExtra("documentId", documentId);
                startActivityForResult(intent, 2);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        endTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        setupRecyclerView();
    }

    private void openDialog() {
        CommentDialog commentDialog = new CommentDialog();
        commentDialog.show(getSupportFragmentManager(), "prompt dialog");
    }

    private void setupRecyclerView() {
        databaseReference.collection("user Profile")
                .document(userID)
                .collection("trips")
                .document(documentId)
                .collection("expense List")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            ExpenseDetails expenseDetails = documentSnapshot.toObject(ExpenseDetails.class);
                            expenseDetailsList.add(expenseDetails);
                        }
                        mAdapter = new ExpenseTrackerAdapter(expenseDetailsList);
                        recyclerView.addItemDecoration(new DividerItemDecoration(ExpenseTracker.this,
                                DividerItemDecoration.VERTICAL));
                        recyclerView.setAdapter(mAdapter);
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                String itemDescription = data.getStringExtra("itemDescription");
                String itemCategory = data.getStringExtra("itemCategory");
                String itemCost = data.getStringExtra("itemCost");

                itemCat = findViewById(R.id.itemCategory);

                ExpenseDetails expenses = new ExpenseDetails(itemDescription, itemCategory, itemCost);
                expenseDetailsList.add(expenses);
                mAdapter.notifyDataSetChanged();

                totalSpending += Long.parseLong(itemCost);
                String s = "Spendings: " + Long.toString(totalSpending);
                spending.setText(s);

                budget -= totalSpending;
                String b = "Balance " + Long.toString(budget);
                balance.setText(b);

            }
        }
    }

    @Override
    public void saveTexts(final String comments) {
      //  WriteBatch batch = databaseReference.batch();

        tripProfile.update("comments", comments).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                travelDetails.setComments(comments);
                databaseReference.collection("forum").add(travelDetails);
                startActivity(homeIntent);
            }
        });
       // Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);

      /*  batch.commit().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("saveText error", "onFailure: ");
            }
        });*/
      //  startActivity(homeIntent);
    }

    private void showMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}