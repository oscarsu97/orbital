package com.example.myorbital;

public class TravelDetails {
    private String title;
    private String dates;
    private double budget;
    private int totalSpending;
    private String countryOfTravel;
    private String comments;
    private String currency;

    private int Lodge = 0;
    private int Dining = 0;
    private int Transport = 0;
    private int Entertainment = 0;
    private int Grocery = 0;
    private int Shopping = 0;
    private int Car_Rental = 0;
    private int Flight = 0;
    private int Fee_And_Charges = 0;
    private int Others = 0;


    public TravelDetails() {
        // no argument constructor
    }

    public TravelDetails(String travelTitle, String travelDates) {
        this.title = travelTitle;
        this.dates = travelDates;
    }

    public int getTotalSpending() {
        return totalSpending;
    }

    public void setTotalSpending(int totalSpending) {
        this.totalSpending = totalSpending;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public String getCountryName() {
        return countryOfTravel;
    }

    public void setCountryName(String countryOfTravel) {
        this.countryOfTravel = countryOfTravel;
    }

    public String getCountryOfTravel() {
        return countryOfTravel;
    }

    public void setCountryOfTravel(String countryOfTravel) {
        this.countryOfTravel = countryOfTravel;
    }

    public int getLodge() {
        return Lodge;
    }

    public void setLodge(int lodge) {
        Lodge = lodge;
    }

    public int getDining() {
        return Dining;
    }

    public void setDining(int dining) {
        Dining = dining;
    }

    public int getTransport() {
        return Transport;
    }

    public void setTransport(int transport) {
        Transport = transport;
    }

    public int getEntertainment() {
        return Entertainment;
    }

    public void setEntertainment(int entertainment) {
        Entertainment = entertainment;
    }

    public int getGrocery() {
        return Grocery;
    }

    public void setGrocery(int grocery) {
        Grocery = grocery;
    }

    public int getShopping() {
        return Shopping;
    }

    public void setShopping(int shopping) {
        Shopping = shopping;
    }

    public int getCar_Rental() {
        return Car_Rental;
    }

    public void setCar_Rental(int car_Rental) {
        Car_Rental = car_Rental;
    }

    public int getFlight() {
        return Flight;
    }

    public void setFlight(int flight) {
        Flight = flight;
    }

    public int getFee_And_Charges() {
        return Fee_And_Charges;
    }

    public void setFee_And_Charges(int fee_And_Charges) {
        Fee_And_Charges = fee_And_Charges;
    }

    public int getOthers() {
        return Others;
    }

    public void setOthers(int others) {
        Others = others;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}