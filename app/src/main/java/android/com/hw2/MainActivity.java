package android.com.hw2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQ_CODE_ADD_EXPENSE = 100;
    public static final String EDIT_EXPENSE_LIST = "Updated Expense";
    public static final String DELETE_EXPENSE_LIST = "Delete Expense";
    public static final String RESULT_KEY = "Result";
    public static final String EXPENSE_LIST = "Expenses";
    private static final int REQUEST_EDIT = 101;
    private static final int REQUEST_DLT = 102;
    Button addExpense, editExpense, dltExpense, showExpense, finishExpense;
    ArrayList<Expense> expensesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(getResources().getDrawable(R.mipmap.ic_launcher));
        addExpense = (Button) findViewById(R.id.btn_addExpense_main);
        editExpense = (Button) findViewById(R.id.btn_editExpense_main);
        dltExpense = (Button) findViewById(R.id.btn_dltExpense_main);
        showExpense = (Button) findViewById(R.id.btn_showExpense_main);
        finishExpense = (Button)findViewById(R.id.btn_finishExpense_main);
        addExpense.setOnClickListener(this);
        showExpense.setOnClickListener(this);
        editExpense.setOnClickListener(this);
        dltExpense.setOnClickListener(this);
        finishExpense.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_ADD_EXPENSE:
                if (resultCode == RESULT_OK) {
                    Expense newExpense = data.getExtras().getParcelable(RESULT_KEY);
                    expensesList.add(newExpense);
                    Collections.sort(expensesList);
                    Toast.makeText(getApplicationContext(), "New Expense added", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_EDIT:
                if(resultCode == RESULT_OK){
                    expensesList = (ArrayList<Expense>)data.getExtras().getSerializable(EDIT_EXPENSE_LIST);
                    Toast.makeText(getApplicationContext(), "Expense saved", Toast.LENGTH_SHORT).show();
                }else if (resultCode == RESULT_CANCELED){
//                    Toast.makeText(getApplicationContext(), "No expenses found", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_DLT:
                if(resultCode == RESULT_OK){
                    expensesList = (ArrayList<Expense>)data.getExtras().getSerializable(DELETE_EXPENSE_LIST);
                    Toast.makeText(getApplicationContext(), "Expense removed", Toast.LENGTH_SHORT).show();
                }else if (resultCode == RESULT_CANCELED){
//                    Toast.makeText(getApplicationContext(), "No expenses found", Toast.LENGTH_SHORT).show();
                }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_addExpense_main:
                Intent addExpenseIntent = new Intent(getApplicationContext(), AddExpense.class);
                startActivityForResult(addExpenseIntent, REQ_CODE_ADD_EXPENSE);
                break;
            case R.id.btn_showExpense_main:
                Intent showExpenseIntent = new Intent(getApplicationContext(), ShowExpense.class);
                showExpenseIntent.putExtra(EXPENSE_LIST, expensesList);
                if(expensesList.size()>0){
                    startActivity(showExpenseIntent);
                }else{
                    Toast.makeText(getApplicationContext(),"No expenses, Please add one to view",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_editExpense_main:
                Intent editExpenseIntent = new Intent(getApplicationContext(), EditActivity.class);
                editExpenseIntent.putExtra(EXPENSE_LIST, expensesList);
                if(expensesList.size()>0){
                    startActivityForResult(editExpenseIntent, REQUEST_EDIT);
                }else{
                    Toast.makeText(getApplicationContext(),"No expenses, Please add one to edit",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_dltExpense_main:
                Intent dltExpenseIntent = new Intent(getApplicationContext(), DeleteExpense.class);
                dltExpenseIntent.putExtra(EXPENSE_LIST, expensesList);
                if(expensesList.size()>0){
                    startActivityForResult(dltExpenseIntent, REQUEST_DLT);
                }else{
                    Toast.makeText(getApplicationContext(),"No expenses, Please add one to delete",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_finishExpense_main:
                finish();
        }
    }

}
