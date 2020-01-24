package com.example.myorbital;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.maltaisn.calcdialog.CalcDialog;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class ExpenseActivity extends AppCompatActivity implements CalcDialog.CalcDialogCallback {

    @Nullable
    private BigDecimal value = BigDecimal.ZERO;
    private TextFieldBoxes spentAmountText;
    private ExtendedEditText spentAmount;
    private final CalcDialog calcDialog = new CalcDialog();

    private ImageView prevItemChosen;
    private ImageView Dining;
    private ImageView Transport;
    private ImageView Lodge;
    private ImageView Entertainment;
    private ImageView Grocery;
    private ImageView Shopping;
    private ImageView privateRental;
    private ImageView Flight;
    private ImageView FeeAndCharges;
    private ImageView Others;

    private EditText itemDescription;
    private String categoryChosen;
    private int amount;

    private Button doneButton;
    private FirebaseUser user;
    private String userId;
    private String tripDocumentId;
    private FirebaseFirestore databaseReference;
    private long totalSpending;
    private long budget;

    private TravelDetails travelDetails;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        itemDescription = findViewById(R.id.itemDescriptionEditText);

        spentAmountText = findViewById(R.id.spendingTextView);
        spentAmount = findViewById(R.id.spendAmountEditText);
        spentAmountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcDialog.getSettings().setInitialValue(value);
                calcDialog.getSettings().setShouldEvaluateOnOperation(true);
                calcDialog.getSettings().setOrderOfOperationsApplied(true);
                calcDialog.getSettings().setAnswerBtnShown(true);
                calcDialog.getSettings().setNumberFormat(NumberFormat.getInstance());
                calcDialog.show(getSupportFragmentManager(), "calc_dialog");
            }
        });

        Dining = findViewById(R.id.diningImageView);
        Transport = findViewById(R.id.transportImageView);
        Lodge = findViewById(R.id.lodgeImageView);
        Entertainment = findViewById(R.id.entertainmentImageView);
        Grocery = findViewById(R.id.groceryImageView);
        Shopping = findViewById(R.id.shoppingImageView);
        privateRental = findViewById(R.id.rentalCarImageView);
        Flight = findViewById(R.id.flightImageView);
        FeeAndCharges = findViewById(R.id.feeAndChargesImageView);
        Others = findViewById(R.id.othersImageView);

        doneButton = findViewById(R.id.addSpendingBtn);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        tripDocumentId = getIntent().getExtras().getString("documentId");
        databaseReference = FirebaseFirestore.getInstance();

        DocumentReference userTripProfile = FirebaseFirestore.getInstance()
                .collection("user Profile")
                .document(userId)
                .collection("trips")
                .document(tripDocumentId);

        userTripProfile.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    travelDetails = documentSnapshot.toObject(TravelDetails.class);
                    totalSpending = (long) travelDetails.getTotalSpending();
                    budget = (long) travelDetails.getBudget();
                } else {
                    showMessage("Document does not exist");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage("Error");
                Log.d("TravelSetup", e.toString());
            }
        });


        Dining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dining.setColorFilter(Color.BLUE);
                if (prevItemChosen != null) {
                    prevItemChosen.setColorFilter(Color.parseColor("#6D6D6D"));
                }
                prevItemChosen = Dining;
                categoryChosen = "Dining";
            }
        });

        Transport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transport.setColorFilter(Color.BLUE);
                if (prevItemChosen != null) {
                    prevItemChosen.setColorFilter(Color.parseColor("#6D6D6D"));
                }
                prevItemChosen = Transport;
                categoryChosen = "Transport";
            }
        });
        Lodge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lodge.setColorFilter(Color.BLUE);
                if (prevItemChosen != null) {
                    prevItemChosen.setColorFilter(Color.parseColor("#6D6D6D"));
                }
                prevItemChosen = Lodge;
                categoryChosen = "Lodge";
            }
        });
        Entertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Entertainment.setColorFilter(Color.BLUE);
                if (prevItemChosen != null) {
                    prevItemChosen.setColorFilter(Color.parseColor("#6D6D6D"));
                }
                prevItemChosen = Entertainment;
                categoryChosen = "Entertainment";
            }
        });
        Grocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Grocery.setColorFilter(Color.BLUE);
                if (prevItemChosen != null) {
                    prevItemChosen.setColorFilter(Color.parseColor("#6D6D6D"));
                }
                prevItemChosen = Grocery;
                categoryChosen = "Grocery";
            }
        });
        Shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Shopping.setColorFilter(Color.BLUE);
                if (prevItemChosen != null) {
                    prevItemChosen.setColorFilter(Color.parseColor("#6D6D6D"));
                }
                prevItemChosen = Shopping;
                categoryChosen = "Shopping";
            }
        });
        privateRental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                privateRental.setColorFilter(Color.BLUE);
                if (prevItemChosen != null) {
                    prevItemChosen.setColorFilter(Color.parseColor("#6D6D6D"));
                }
                prevItemChosen = privateRental;
                categoryChosen = "Car Rental";
            }
        });
        Flight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Flight.setColorFilter(Color.BLUE);
                if (prevItemChosen != null) {
                    prevItemChosen.setColorFilter(Color.parseColor("#6D6D6D"));
                }
                prevItemChosen = Flight;
                categoryChosen = "Flight";
            }
        });
        FeeAndCharges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeeAndCharges.setColorFilter(Color.BLUE);
                if (prevItemChosen != null) {
                    prevItemChosen.setColorFilter(Color.parseColor("#6D6D6D"));
                }
                prevItemChosen = FeeAndCharges;
                categoryChosen = "Fee & Charges";
            }
        });
        Others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Others.setColorFilter(Color.BLUE);
                if (prevItemChosen != null) {
                    prevItemChosen.setColorFilter(Color.parseColor("#6D6D6D"));
                }
                prevItemChosen = Others;
                categoryChosen = "Others";
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = value.intValue();
                budget -= amount;
                totalSpending += amount;

                /*Map<String, Object> expenses = new HashMap<>();
                expenses.put("Total Spending", totalSpending + amount);
                showMessage("" + amount);*/

                String description = itemDescription.getText().toString().trim();
                ExpenseDetails expenseList = new ExpenseDetails(description, categoryChosen, "" + amount);

                //list of expenses with description,category and amount spent
                databaseReference.collection("user Profile")
                        .document(userId)
                        .collection("trips")
                        .document(tripDocumentId)
                        .collection("expense List")
                        .add(expenseList);

                DocumentReference travelDetailDocRef = databaseReference.collection("user Profile").document(userId
                ).collection("trips").document(tripDocumentId);
                //update total Spending
                travelDetailDocRef.update("totalSpending", totalSpending);
                //update budget left
                travelDetailDocRef.update("budget", budget);

                switch (categoryChosen) {
                    case "Lodge":
                        travelDetailDocRef.update("lodge", travelDetails.getLodge() + amount);
                        break;
                    case "Dining":
                        travelDetailDocRef.update("dining", travelDetails.getDining() + amount);
                        break;
                    case "Transport":
                        travelDetailDocRef.update("transport", travelDetails.getTransport() + amount);
                        break;
                    case "Entertainment":
                        travelDetailDocRef.update("entertainment", travelDetails.getEntertainment() + amount);
                        break;
                    case "Grocery":
                        travelDetailDocRef.update("grocery", travelDetails.getGrocery() + amount);
                        break;
                    case "Shopping":
                        travelDetailDocRef.update("shopping", travelDetails.getShopping() + amount);
                        break;
                    case "Car Rental":
                        travelDetailDocRef.update("car_Rental", travelDetails.getCar_Rental() + amount);
                        break;
                    case "Flight":
                        travelDetailDocRef.update("flight", travelDetails.getFlight() + amount);
                        break;
                    case "Fee & Charges":
                        travelDetailDocRef.update("fee_And_Charges", travelDetails.getFee_And_Charges() + amount);
                        break;
                    default:
                        travelDetailDocRef.update("others", travelDetails.getOthers() + amount);
                }

                // passing back details to expense tracker to create an expense detail object
                Intent toExpenseTracker = new Intent(getApplicationContext(), ExpenseTracker.class);
                toExpenseTracker.putExtra("itemDescription", itemDescription.getText().toString());
                toExpenseTracker.putExtra("itemCategory", categoryChosen);
                toExpenseTracker.putExtra("itemCost", spentAmount.getText().toString());
                setResult(Activity.RESULT_OK, toExpenseTracker);
                finish();


            }
        });

    }

    //result of calculate are returned here
    @Override
    public void onValueEntered(int requestCode, BigDecimal value) {
        // if (requestCode == CALC_REQUEST_CODE) {} <-- If there were many dialogs, this would be used

        // The calculator dialog returned a value
        this.value = value;
        spentAmount.setText(value.toString());
    }

    public void showMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}
