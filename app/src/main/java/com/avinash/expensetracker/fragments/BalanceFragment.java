package com.avinash.expensetracker.fragments;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.avinash.expensetracker.R;
import com.avinash.expensetracker.transactionDb.AppDatabase;
import com.avinash.expensetracker.transactionDb.AppExecutors;
import com.avinash.expensetracker.utils.Constants;
import com.avinash.expensetracker.utils.ExpenseList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.avinash.expensetracker.activities.MainActivity.fab;


public class BalanceFragment extends Fragment implements AdapterView.OnItemSelectedListener{


    private AppDatabase mAppDb;
    PieChart pieChart;
    Spinner spinner;

    private TextView balanceTv,incomeTv,expenseTv;
    private TextView dateTv;

    private int balanceAmount,incomeAmount,expenseAmount;
    private int foodExpense,travelExpense,clothesExpense,moviesExpense,heathExpense,groceryExpense,otherExpense;

    long firstDate;

    ArrayList<ExpenseList> expenseList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_balance,container,false);

        pieChart= view.findViewById(R.id.balancePieChart);
        spinner = view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        mAppDb = AppDatabase.getInstance(getContext());

        balanceTv = view.findViewById(R.id.totalAmountTextView);
        expenseTv = view.findViewById(R.id.amountForExpenseTextView);
        incomeTv = view.findViewById(R.id.amountForIncomeTextView);

        dateTv = view.findViewById(R.id.dateTextView);
        expenseList=new ArrayList<>();
        getAllBalanceAmount();
        setupPieChart();
        return view;

        //TODO 1.Change constraint to linear and change entire layout
        //TODO 2.Align piechart properly with label
        //TODO 3.See if can opytimize queries and spinner state and read about fragment lifecycle

    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.date_array,
                android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i("fragment", String.valueOf(isVisibleToUser));
        if (isVisibleToUser){
            setupSpinner();
            fab.setVisibility(View.GONE);
        } else{
            fab.setVisibility(View.VISIBLE);
        }
    }

    private void setupPieChart() {

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if(spinner.getSelectedItemPosition()==0)
                    getAllPieValues();
                else if(spinner.getSelectedItemPosition()==1) {
                    try {
                        getWeekPieValues();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else if(spinner.getSelectedItemPosition()==2){
                    try {
                        getMonthPieValues();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                expenseList.clear();
             if(foodExpense!=0)
                 expenseList.add(new ExpenseList("Food",foodExpense));
             if(travelExpense!=0)
                 expenseList.add(new ExpenseList("Travel",travelExpense));
             if(clothesExpense!=0)
                 expenseList.add(new ExpenseList("Clothes",clothesExpense));
             if(moviesExpense!=0)
                 expenseList.add(new ExpenseList("Movies",moviesExpense));
             if(heathExpense!=0)
                 expenseList.add(new ExpenseList("Health",heathExpense));
             if(groceryExpense!=0)
                 expenseList.add(new ExpenseList("Grocery",groceryExpense));
             if(otherExpense!=0)
                 expenseList.add(new ExpenseList("Other",otherExpense));
            }
        });


        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {

                List<PieEntry> pieEntries = new ArrayList<>();
                for(int i = 0 ; i <expenseList.size(); i++){
                    pieEntries.add(new PieEntry(expenseList.get(i).getAmount(),expenseList.get(i).getCategory()));
                }
                pieChart.setVisibility(View.VISIBLE);
                PieDataSet dataSet = new PieDataSet(pieEntries,null);
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                PieData pieData = new PieData(dataSet);

                pieData.setValueTextSize(16);
                pieData.setValueTextColor(Color.WHITE);
                pieData.setValueFormatter(new PercentFormatter());
                pieChart.setUsePercentValues(true);
                pieChart.setData(pieData);
                pieChart.animateY(1000);
                pieChart.invalidate();

                pieChart.getDescription().setText("");
                Legend l=pieChart.getLegend();
                l.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
                //l.setXEntrySpace(8f);
                //l.setYEntrySpace(1f);
                //l.setYOffset(0f);
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if(adapterView.getSelectedItemPosition()==0){
            getAllBalanceAmount();
            setupPieChart();
        }

        else if (adapterView.getSelectedItemPosition() == 1){
            //This week
            try {
                getWeekBalanceAmount();
                setupPieChart();
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else if(adapterView.getSelectedItemPosition()==2){
            //This month
            try {
                getMonthBalanceAmount();
                setupPieChart();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }


    private void getAllPieValues(){
        foodExpense =mAppDb.transactionDao().getSumExpenseByCategory("Food");
        travelExpense=mAppDb.transactionDao().getSumExpenseByCategory("Travel");
        clothesExpense=mAppDb.transactionDao().getSumExpenseByCategory("Clothes");
        moviesExpense=mAppDb.transactionDao().getSumExpenseByCategory("Movies");
        heathExpense=mAppDb.transactionDao().getSumExpenseByCategory("Health");
        groceryExpense=mAppDb.transactionDao().getSumExpenseByCategory("Grocery");
        otherExpense=mAppDb.transactionDao().getSumExpenseByCategory("Other");
    }

    private void getWeekPieValues() throws ParseException {
        Calendar calendar;
        calendar=Calendar.getInstance();

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String startDate = "", endDate = "";
        // Set the calendar to sunday of the current week
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        startDate = df.format(calendar.getTime());
        Date sDate=df.parse(startDate);
        final long sdate=sDate.getTime();

        calendar.add(Calendar.DATE, 6);
        endDate = df.format(calendar.getTime());
        Date eDate=df.parse(endDate);
        final long edate=eDate.getTime();

        foodExpense =mAppDb.transactionDao().getSumExpenseByCategoryCustomDate("Food",sdate,edate);
        travelExpense=mAppDb.transactionDao().getSumExpenseByCategoryCustomDate("Travel",sdate,edate);
        clothesExpense=mAppDb.transactionDao().getSumExpenseByCategoryCustomDate("Clothes",sdate,edate);
        moviesExpense=mAppDb.transactionDao().getSumExpenseByCategoryCustomDate("Movies",sdate,edate);
        heathExpense=mAppDb.transactionDao().getSumExpenseByCategoryCustomDate("Health",sdate,edate);
        groceryExpense=mAppDb.transactionDao().getSumExpenseByCategoryCustomDate("Grocery",sdate,edate);
        otherExpense=mAppDb.transactionDao().getSumExpenseByCategoryCustomDate("Other",sdate,edate);
    }

    private void getMonthPieValues() throws ParseException{

        Calendar calendar;
        calendar=Calendar.getInstance();

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String startDate = "", endDate = "";

        calendar.set(Calendar.DAY_OF_MONTH,1);
        startDate = df.format(calendar.getTime());
        Date sDate=df.parse(startDate);
        final long sdate=sDate.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        endDate = df.format(calendar.getTime());
        Date eDate=df.parse(endDate);
        final long edate=eDate.getTime();

        foodExpense =mAppDb.transactionDao().getSumExpenseByCategoryCustomDate("Food",sdate,edate);
        travelExpense=mAppDb.transactionDao().getSumExpenseByCategoryCustomDate("Travel",sdate,edate);
        clothesExpense=mAppDb.transactionDao().getSumExpenseByCategoryCustomDate("Clothes",sdate,edate);
        moviesExpense=mAppDb.transactionDao().getSumExpenseByCategoryCustomDate("Movies",sdate,edate);
        heathExpense=mAppDb.transactionDao().getSumExpenseByCategoryCustomDate("Health",sdate,edate);
        groceryExpense=mAppDb.transactionDao().getSumExpenseByCategoryCustomDate("Grocery",sdate,edate);
        otherExpense=mAppDb.transactionDao().getSumExpenseByCategoryCustomDate("Other",sdate,edate);
    }

    private void getAllBalanceAmount(){

        //get date when first transaction date and todays date
       AppExecutors.getInstance().diskIO().execute(new Runnable() {
           @Override
           public void run() {
               firstDate=mAppDb.transactionDao().getFirstDate();
           }
       });

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String first = df.format(new Date(firstDate));
        Date today=Calendar.getInstance().getTime();
        String todaysDate=df.format(today);
        String Date=first+" - "+todaysDate;
        dateTv.setText(Date);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                int income = mAppDb.transactionDao().getAmountByTransactionType(Constants.incomeCategory);
                incomeAmount = income;
                int expense = mAppDb.transactionDao().getAmountByTransactionType(Constants.expenseCategory);
                expenseAmount = expense;
                int balance = income - expense;
                balanceAmount = balance;
            }
        });
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                balanceTv.setText(String.valueOf(balanceAmount)+" \u20B9");
                incomeTv.setText(String.valueOf(incomeAmount)+" \u20B9");
                expenseTv.setText(String.valueOf(expenseAmount)+" \u20B9");
            }
        });


    }

    private void getWeekBalanceAmount() throws ParseException {
        Calendar calendar;
        calendar=Calendar.getInstance();

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String startDate = "", endDate = "";
        // Set the calendar to sunday of the current week
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        startDate = df.format(calendar.getTime());
        Date sDate=df.parse(startDate);
        final long sdate=sDate.getTime();

        calendar.add(Calendar.DATE, 6);
        endDate = df.format(calendar.getTime());
        Date eDate=df.parse(endDate);
        final long edate=eDate.getTime();

        String dateString = startDate + " - " + endDate;
        dateTv.setText(dateString);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                int income = mAppDb.transactionDao().getAmountbyCustomDates(Constants.incomeCategory,sdate,edate);
                incomeAmount = income;
                int expense = mAppDb.transactionDao().getAmountbyCustomDates(Constants.expenseCategory,sdate,edate);
                expenseAmount = expense;
                int balance = income - expense;
                balanceAmount = balance;

            }
        });
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                balanceTv.setText(String.valueOf(balanceAmount)+" \u20B9");
                incomeTv.setText(String.valueOf(incomeAmount)+" \u20B9");
                expenseTv.setText(String.valueOf(expenseAmount)+" \u20B9");
            }
        });
    }


    private void getMonthBalanceAmount() throws ParseException {
        Calendar calendar;
        calendar=Calendar.getInstance();

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String startDate = "", endDate = "";

        calendar.set(Calendar.DAY_OF_MONTH,1);
        startDate = df.format(calendar.getTime());
        Date sDate=df.parse(startDate);
        final long sdate=sDate.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        endDate = df.format(calendar.getTime());
        Date eDate=df.parse(endDate);
        final long edate=eDate.getTime();

        String dateString = startDate + " - " + endDate;
        dateTv.setText(dateString);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                int income = mAppDb.transactionDao().getAmountbyCustomDates(Constants.incomeCategory,sdate,edate);
                incomeAmount = income;
                int expense = mAppDb.transactionDao().getAmountbyCustomDates(Constants.expenseCategory,sdate,edate);
                expenseAmount = expense;
                int balance = income - expense;
                balanceAmount = balance;

            }
        });
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                balanceTv.setText(String.valueOf(balanceAmount)+" \u20B9");
                incomeTv.setText(String.valueOf(incomeAmount)+" \u20B9");
                expenseTv.setText(String.valueOf(expenseAmount)+" \u20B9");
            }
        });
    }
}
