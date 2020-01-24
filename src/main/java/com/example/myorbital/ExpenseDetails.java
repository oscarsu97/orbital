package com.example.myorbital;

import android.util.Log;
import android.widget.ImageView;

public class ExpenseDetails {

    private String itemDescription;
    private String itemCategory;
    private String itemCost;
    private ImageView itemCat;

    public ExpenseDetails(){
        //no argument constructor
    }

    public ExpenseDetails(String itemDescription, String itemCategory, String itemCost){
        this.itemDescription = itemDescription;
        this.itemCategory = itemCategory;
        this.itemCost = itemCost;
    }

    public String getItemDescription(){
        return itemDescription;
    }

    public String getItemCategory(){
        return itemCategory;
    }

    public String getItemCost(){
        return itemCost;
    }

    public void setItemDescription(String itemDescription){
        this.itemDescription = itemDescription;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public void setItemCost(String itemCost) {
        this.itemCost = itemCost;
    }

    public int setImage() {


        switch(itemCategory) {
            case "Lodge":
                return R.drawable.ic_home;
            case "Dining":
                return R.drawable.ic_dining;
            case "Transport":
                return R.drawable.ic_transport;
            case "Entertainment":
                return R.drawable.ic_entertainment;
            case "Grocery":
                return R.drawable.ic_grocery;
            case "Shopping":
                return R.drawable.ic_shopping;
            case "Car Rental":
                return R.drawable.ic_rentalcar;
            case "Flight":
                return R.drawable.ic_flight;
            case "Fee & Charges":
                return R.drawable.ic_fee;
            default:
                return R.drawable.ic_others;
        }

    }
}

