package com.example.myorbital;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.scrounger.countrycurrencypicker.library.Buttons.CountryCurrencyButton;
import com.scrounger.countrycurrencypicker.library.Country;
import com.scrounger.countrycurrencypicker.library.Currency;
import com.scrounger.countrycurrencypicker.library.Listener.CountryCurrencyPickerListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class TravelSetUp extends AppCompatActivity implements View.OnClickListener {

    private static TextFieldBoxes mStartTimeText;
    private static ExtendedEditText mStartTime;
    private static TextFieldBoxes mEndTimeText;
    private static ExtendedEditText mEndTime;
    private static ExtendedEditText title;
    private static Button doneButton;
    private static EditText foreignCurrencyBudget;
    private ExtendedEditText localER;
    private ExtendedEditText foreignER;
    private Button getExchangeRate;
    FirebaseFirestore databaseReference;
    Country countryofTravel;
    private String localCountryCode;
    private String userID;
    private DocumentReference userProfile;

    private DatePickerDialogFragment mDatePickerDialogFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.travelsetup);

        databaseReference = FirebaseFirestore.getInstance();

        //Date Picker Dialog
        title = findViewById(R.id.title_edit);
        mStartTimeText = findViewById((R.id.start_date_header));
        mStartTime = findViewById(R.id.start_date_edit);
        mEndTimeText = findViewById(R.id.end_date_header);
        mEndTime = findViewById(R.id.end_date_edit);

        // Setting TextView for the exchange rates
        foreignER = findViewById(R.id.foreign_currency_er_edit);
        localER = findViewById(R.id.local_currency_er_edit);
        localER.setText("1");

        foreignCurrencyBudget = findViewById(R.id.foreign_currency_budget);

        doneButton = findViewById(R.id.travel_setup_done_button);
        getExchangeRate = findViewById(R.id.get_er);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userProfile = FirebaseFirestore.getInstance().collection("user Profile").document(userID);
        userProfile.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    localCountryCode = documentSnapshot.get("Country Code").toString();
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

        mDatePickerDialogFragment = new DatePickerDialogFragment();


        mStartTimeText.setOnClickListener(this);
        mEndTimeText.setOnClickListener(this);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double FCB = Double.parseDouble(foreignCurrencyBudget.getText().toString());
                String travelTitle = title.getText().toString().trim();
                //Adding entries to the RecyclerView and returning to MainActivity
                String date = mStartTime.getText().toString() + " - " + mEndTime.getText().toString();
                TravelDetails travelDetails = new TravelDetails(travelTitle, date);
                travelDetails.setDates(date);
                travelDetails.setTitle(travelTitle);
                travelDetails.setBudget(FCB);
                travelDetails.setTotalSpending(0);
                travelDetails.setCountryName(countryofTravel.getName());
                travelDetails.setCurrency(countryofTravel.getCurrency().getSymbol());
                databaseReference.collection("user Profile")
                        .document(userID)
                        .collection("trips")
                        .add(travelDetails);

                Intent homePageIntent = new Intent(getApplicationContext(), MainActivity.class);
                homePageIntent.putExtra("travelTittle", title.getText().toString());
                homePageIntent.putExtra("travelDates", date);
                setResult(Activity.RESULT_OK, homePageIntent);
                finish();
            }
        });

        //Country Picker
        final CountryCurrencyButton button = (CountryCurrencyButton) findViewById(R.id.button);
        button.setOnClickListener(new CountryCurrencyPickerListener() {
            @Override
            public void onSelectCountry(Country country) {
                if (country.getCurrency() == null) {
                    Toast.makeText(getApplicationContext(),
                            String.format("name: %s\ncode: %s", country.getName(), country.getCode())
                            , Toast.LENGTH_SHORT).show();
                } else {
                    countryofTravel = country;
                    Toast.makeText(getApplicationContext(),
                            String.format("name: %s\ncurrencySymbol: %s", country.getName(), country.getCurrency().getSymbol())
                            , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSelectCurrency(Currency currency) {

            }
        });

        // Click Get Exchange Rate button to get ER
        getExchangeRate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String travelCountry = button.getCountry().getCurrency().getCode();
                Log.i("String", travelCountry);

                Log.i("String", "Home Country travel code is : " + localCountryCode);

                AsyncHttpClient client = new AsyncHttpClient();
                client.get("https://openexchangerates.org/api/latest.json?app_id=43b520da21ac4401bdbd15be74ffaea6",
                        new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject responseBody) {
                                Log.i("Exchange Rate", "HTTP SUCCESS");
                                try {
                                    JSONObject jsonObj = new JSONObject(String.valueOf(responseBody));
                                    JSONObject ratesObject = jsonObj.getJSONObject("rates");


                                    Double usdRate = ratesObject.getDouble("USD");
                                    Log.i("Exchange Rate", "USD " + usdRate);
                                    Double travelRate = ratesObject.getDouble(travelCountry);
                                    Log.i("Exchange rate", "Travel Country " + travelRate);
                                    Double homeCountryRate = ratesObject.getDouble(localCountryCode);
                                    Log.i("EXCHANGE RATE", "Home Country " + homeCountryRate);

                                    Double travelBaseRate = travelRate / homeCountryRate;
                                    Log.i("EXCHANGE RATE", "Travel Base Rate: " + travelBaseRate);

                                    String forER = String.format("%.5f", travelBaseRate);
                                    foreignER.setText(forER);

                                } catch (JSONException e) {
                                    // TODO: 20/7/2019
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.start_date_header) {
            mDatePickerDialogFragment.setFlag(DatePickerDialogFragment.FLAG_START_DATE);
            mDatePickerDialogFragment.show(getSupportFragmentManager(), "datePicker");
        } else if (id == R.id.end_date_header) {
            mDatePickerDialogFragment.setFlag(DatePickerDialogFragment.FLAG_END_DATE);
            mDatePickerDialogFragment.show(getSupportFragmentManager(), "datePicker");
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    public static class DatePickerDialogFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        public static final int FLAG_START_DATE = 0;
        public static final int FLAG_END_DATE = 1;

        private int flag = 0;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void setFlag(int i) {
            flag = i;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            month = month + 1;
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
            if (flag == FLAG_START_DATE) {
                /*
                mStartTime.setText(format.format(calendar.getTime()));
                */
                String stringOfDate = dayOfMonth + "/" + month + "/" + year;
                mStartTime.setText(stringOfDate);
            } else if (flag == FLAG_END_DATE) {
                String stringOfDate = dayOfMonth + "/" + month + "/" + year;
                mEndTime.setText(stringOfDate);
                /*
                mEndTime.setText(format.format(calendar.getTime()));
                */
            }
        }
    }

    public void showMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}

