package android.com.hw2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DeleteExpense extends AppCompatActivity implements View.OnClickListener {
    Button selectExpense, save, cancel;
    EditText expName, expAmount, expDate;
    ImageView receipt;
    Spinner items;
    ArrayList<Expense> expenseList;
    ArrayList<String> expenseNameList = new ArrayList<>();
    CharSequence[] expenseNames;
    String selectedExpenseName;
    String[] categories;
    Bitmap bitmap;
    int objectPosition;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_expense);
        selectExpense = (Button)findViewById(R.id.btn_slctExp_dlt);
        save = (Button)findViewById(R.id.btn_saveExpense_dlt);
        cancel = (Button)findViewById(R.id.btn_cancelExpense_dlt);
        selectExpense.setOnClickListener(this);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        expName = (EditText) findViewById(R.id.newExpense_dlt);
        expAmount = (EditText) findViewById(R.id.amount_dlt);
        expDate = (EditText) findViewById(R.id.date_dlt);
        receipt = (ImageView) findViewById(R.id.bill_dlt);
        items = (Spinner) findViewById(R.id.category_items_dlt);

        expenseList = (ArrayList<Expense>) getIntent().getExtras().getSerializable(MainActivity.EXPENSE_LIST);
        for (Expense expense : expenseList) {
            expenseNameList.add(expense.expenseName);
        }

        if (expenseNameList.size() > 1) Collections.sort(expenseNameList);
        expenseNames = expenseNameList.toArray(new CharSequence[expenseNameList.size()]);
        categories = getResources().getStringArray(R.array.categories);
        ArrayAdapter<CharSequence> categories_list = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        categories_list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        items.setAdapter(categories_list);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setItems(expenseNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedExpenseName = (String) expenseNames[which];
                for (Expense expense : expenseList) {
                    if (expense.expenseName.equals(selectedExpenseName)) {
                        setExpenseDetails(expense);
                    }
                }
            }
        });
        alertDialog = alertBuilder.create();


    }

    private void setExpenseDetails(Expense expense) {
        expName.setText(expense.expenseName);
        expAmount.setText(expense.expenseAmount);
        expDate.setText(expense.expenseDate);
        List<String> list = Arrays.asList(categories);
        for (String category : list) {
            if (category.equals(expense.expenseCategory)) {
                items.setSelection(((ArrayAdapter<String>) items.getAdapter()).getPosition(category));
                break;
            }
        }
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(expense.expenseImageUri));
        } catch (IOException e) {
            e.printStackTrace();
        }
        receipt.setImageBitmap(bitmap);
        objectPosition = expenseList.indexOf(expense);
        disableAllComponents();
    }

    private void disableAllComponents() {
        expName.setKeyListener(null);
        expAmount.setKeyListener(null);
        expDate.setClickable(false);
        items.setEnabled(false);
//        items.setClickable(false);
        receipt.setOnClickListener(null);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_slctExp_dlt:
                alertDialog.show();
                break;
            case R.id.btn_saveExpense_dlt:
                expenseList.remove(objectPosition);
                Intent dltIntent = new Intent();
                dltIntent.putExtra(MainActivity.DELETE_EXPENSE_LIST,expenseList);
                setResult(RESULT_OK,dltIntent);
                finish();
                break;
            case R.id.btn_cancelExpense_dlt:
                finish();
                break;
        }
    }
}
