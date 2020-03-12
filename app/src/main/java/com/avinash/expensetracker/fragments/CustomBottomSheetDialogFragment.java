package com.avinash.expensetracker.fragments;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.avinash.expensetracker.R;
import com.avinash.expensetracker.activities.AddExpenseActivity;
import com.avinash.expensetracker.utils.Constants;

public class CustomBottomSheetDialogFragment extends BottomSheetDialogFragment {

    Button addIncomeButton;
    Button addExpenseButton;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_fragment, container, false);

        addIncomeButton=v.findViewById(R.id.bottom_sheet_income_btn);
        addExpenseButton=v.findViewById(R.id.bottom_sheet_expense_btn);

        addIncomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent intent =new Intent(getActivity(), AddExpenseActivity.class);
                intent.putExtra("from", Constants.addIncomeString);
                startActivity(intent);
            }
        });

        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent intent =new Intent(getActivity(), AddExpenseActivity.class);
                intent.putExtra("from", Constants.addExpenseString);
                startActivity(intent);

            }
        });


        return v;
    }
}