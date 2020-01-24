package com.example.myorbital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ForumFeedback extends AppCompatActivity {

    private String documentId;
    private DocumentReference feedback;
    private TravelDetails travelDetails;
    private TextView comments;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_feedback);

        comments = findViewById(R.id.comments);
        db = FirebaseFirestore.getInstance();

        documentId = getIntent().getExtras().getString("documentId");
        Log.i("documentid", documentId);
        feedback = db.collection("forum").document(documentId);
        Log.i("documentsleh", feedback.toString());
        feedback.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String test = documentSnapshot.getString("comments");
                    travelDetails = documentSnapshot.toObject(TravelDetails.class);
                    Log.i("bypass123", "listener");
                    Pie pie = AnyChart.pie();

                    List<DataEntry> data = new ArrayList<>();
                    data.add(new ValueDataEntry("Lodge", travelDetails.getLodge()));
                    data.add(new ValueDataEntry("Dining", travelDetails.getDining()));
                    data.add(new ValueDataEntry("Transport", travelDetails.getTransport()));
                    data.add(new ValueDataEntry("Entertainment", travelDetails.getEntertainment()));
                    data.add(new ValueDataEntry("Shopping", travelDetails.getShopping()));
                    data.add(new ValueDataEntry("Car Rental", travelDetails.getCar_Rental()));
                    data.add(new ValueDataEntry("Flight", travelDetails.getFlight()));
                    data.add(new ValueDataEntry("Fee & Charges", travelDetails.getFee_And_Charges()));
                    data.add(new ValueDataEntry("Others", travelDetails.getOthers()));
                    pie.data(data);

                    AnyChartView anyChartView = (AnyChartView) findViewById(R.id.pieChart);
                    anyChartView.setChart(pie);

                    comments.setText(travelDetails.getComments());
                    Log.i("traveldetailsleh", "yoyo");
                } else {
                    Log.i("whyyyy", "onSuccess: ");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
}
