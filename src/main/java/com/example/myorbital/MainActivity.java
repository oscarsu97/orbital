package com.example.myorbital;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.util.Log;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Firebase
    private FirebaseFirestore databaseReference;
    private CollectionReference userDetails;
    private String userId;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    // Variables for TravelAdapter and RecycleView
    private RecyclerView recyclerView;
    private TravelCountryAdapter mAdapter;
    private List<TravelDetails> travelDetailsList = new ArrayList<>();

    private Button setUpButton;
    private NavigationView navigationView;
    private View headerView;
    private TextView navUsername;
    private TextView navEmail;
    private ImageView navUserPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //FireBase Authentication
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        //Header View of Navigation View
        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        navUsername = headerView.findViewById(R.id.nav_username);
        navEmail = headerView.findViewById(R.id.nav_mail);
        navUserPhoto = headerView.findViewById(R.id.nav_user_photo);

        setUpButton = findViewById(R.id.setUpButton);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        displayUserInfo();

    }


    private void setupRecyclerView() {
        Query query = userDetails.orderBy("title");
        FirestoreRecyclerOptions<TravelDetails> options = new FirestoreRecyclerOptions.Builder<TravelDetails>()
                .setQuery(query, TravelDetails.class)
                .build();
        mAdapter = new TravelCountryAdapter(options);
        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this,
                DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        //on item click of the recycler view
        mAdapter.setOnItemClickListener(new TravelCountryAdapter.OnItemClickListener() {

            @Override
            public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {
                TravelDetails travelDetails = documentSnapshot.toObject(TravelDetails.class);
                String id = documentSnapshot.getId();
                Intent i = new Intent(getApplicationContext(), ExpenseTracker.class);
                i.putExtra("documentId", id);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    //when i clicked on the navigation icon
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Intent intentItemSelected;

        int id = item.getItemId();

        if (id == R.id.nav_login) {
            //fragment = new LoginFragment();
            if (item.getTitle().equals("Logout")) {
                FirebaseAuth.getInstance().signOut();
                navUsername.setText("TET");
                navEmail.setText("");
                item.setTitle("Login");
                Glide.with(this).load(R.drawable.ic_menu_login).into(navUserPhoto);
                Toast.makeText(MainActivity.this, "successfully signed out", Toast.LENGTH_LONG).show();
            } else {
                intentItemSelected = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentItemSelected);

                //setUpButton.setVisibility(View.INVISIBLE);
            }
        } else if (id == R.id.nav_tools) {
            intentItemSelected = new Intent(getApplicationContext(), Forum.class);
            startActivity(intentItemSelected);
        } else if (id == R.id.nav_share) {

        }
        //this will change the layout depending on which icon is clicked
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void displayUserInfo() {
        if (currentUser != null) {

            setUpButton.setVisibility(View.VISIBLE);

            navUsername.setText(currentUser.getDisplayName());
            navEmail.setText(currentUser.getEmail());

            //use Glide to load user image
            Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhoto);
            //update the navigation drawer as well
            Menu menuNav = navigationView.getMenu();
            MenuItem loginItem = menuNav.findItem(R.id.nav_login);
            loginItem.setTitle("Logout");


            //display user trip information if they are logged in
            databaseReference = FirebaseFirestore.getInstance();
            userId = mAuth.getUid();
            userDetails = databaseReference.collection("user Profile")
                    .document(userId)
                    .collection("trips");

            userDetails.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        TravelDetails travelDetails = documentSnapshot.toObject(TravelDetails.class);
                        travelDetailsList.add(travelDetails);
                        String date = travelDetails.getDates();

                        //this log didn't display in the debug logcat
                        Log.d("travel detail", date);
                    }
                }
            });

            setupRecyclerView();
        } else {
            setUpButton.setVisibility(View.INVISIBLE);
        }
    }

    public void openTravelSetup(View view) {
        Intent intent = new Intent(this, TravelSetUp.class);
        startActivityForResult(intent, 1);
    }

    /* @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         if (requestCode == 1) {
             if (resultCode == Activity.RESULT_OK) {
                 String travelTitle = data.getStringExtra("travelTitle");
                 String travelDate = data.getStringExtra("travelDates");
                 TravelDetails travelDetails = new TravelDetails(travelTitle, travelDate);
                 travelDetails.setDates(travelDate);
                 travelDetails.setTitle(travelTitle);
                 travelDetailsList.add(travelDetails);
                 mAdapter.notifyDataSetChanged();
             } else if (resultCode == Activity.RESULT_CANCELED) {
                 //ToDO
             }
         }
     }
 */
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

    private void showMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}
