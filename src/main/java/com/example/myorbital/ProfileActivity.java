package com.example.myorbital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.scrounger.countrycurrencypicker.library.Buttons.CountryCurrencyButton;
import com.scrounger.countrycurrencypicker.library.Country;
import com.scrounger.countrycurrencypicker.library.Currency;
import com.scrounger.countrycurrencypicker.library.Listener.CountryCurrencyPickerListener;


import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {


    private static final String TAG = "ProfileActivity";
    private EditText FullName;
    private EditText Age;
    private CountryCurrencyButton Country;
    private Button doneBtn;
    private String countryCodeChosen;
    private FirebaseFirestore databaseReference;
    private FirebaseUser user;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();

        //data stored into Firestore Cloud
        databaseReference = FirebaseFirestore.getInstance();
        FullName = findViewById(R.id.fullNameEditText);
        Age = findViewById(R.id.ageEditText);
        Country = (CountryCurrencyButton) findViewById(R.id.countryOfStayBtn);

        Country.setOnClickListener(new CountryCurrencyPickerListener() {
            @Override
            public void onSelectCountry(Country country) {
                if (country.getCurrency() == null) {
                    Toast.makeText(getApplicationContext(),
                            String.format("name: %s\ncode: %s", country.getName(), country.getCode())
                            , Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            String.format("name: %s\ncurrencySymbol: %s", country.getName(), country.getCurrency().getSymbol())
                            , Toast.LENGTH_SHORT).show();
                    countryCodeChosen = country.getCurrency().getCode();
                }
            }

            @Override
            public void onSelectCurrency(Currency currency) {

            }
        });

        doneBtn = findViewById(R.id.doneProfileBtn);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = FullName.getText().toString();
                String age = Age.getText().toString();
                String country = Country.getText().toString();
               //int totalSpending = 0;

                Map<String, Object> profile = new HashMap<>();
                profile.put("Full Name", name);
                profile.put("Age", age);
                profile.put("Country", country);
                profile.put("Country Code", countryCodeChosen);
                //profile.put("Total Spending", totalSpending);
                databaseReference.collection("user Profile").document(userId).set(profile)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                showMessage("Profile Updated");
                                Intent homePageIntent = new Intent(ProfileActivity.this, MainActivity.class);
                                startActivity(homePageIntent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("Error");
                        Log.d(TAG, e.toString());
                    }
                });

            }
        });
    }

    //to show toast message
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
