package android.com.hw2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class ShowExpense extends AppCompatActivity implements View.OnClickListener {
    TextView showExpenseName, showExpenseCategory, showExpenseAmount, showExpenseDate;
    ImageView receipt, first, last, previous, next;
    Button btnFinish;
    ArrayList<Expense> expenses = new ArrayList<>();
    Expense expenseObject, current, prev;
    Bitmap bitmap;
    ListIterator<Expense> listIterator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expense);
        if(getIntent().getExtras() == null){
            System.out.println("Empty");
            setResult(RESULT_CANCELED);
            finish();
        }
        showExpenseName = (TextView) findViewById(R.id.name_show);
        showExpenseCategory = (TextView) findViewById(R.id.category_show);
        showExpenseAmount = (TextView) findViewById(R.id.amount_show);
        showExpenseDate = (TextView) findViewById(R.id.date_show);
        receipt = (ImageView) findViewById(R.id.bill_show);
        first = (ImageView) findViewById(R.id.firstImage_show);
        last = (ImageView) findViewById(R.id.lastImage_show);
        previous = (ImageView) findViewById(R.id.previousImage_show);
        next = (ImageView) findViewById(R.id.nextImage_show);
        btnFinish = (Button) findViewById(R.id.btnFinish_show);
        expenses = (ArrayList<Expense>) getIntent().getExtras().getSerializable(MainActivity.EXPENSE_LIST);
        if (expenses.size() < 1) {
            Toast.makeText(getApplicationContext(), "No expenses added", Toast.LENGTH_SHORT).show();
            finish();
        }
        listIterator = expenses.listIterator();
        expenseObject = expenses.get(0);
        displayExpenseDetails(expenseObject);
        btnFinish.setOnClickListener(this);
        first.setOnClickListener(this);
        last.setOnClickListener(this);
        previous.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFinish_show:
                finish();
                break;
            case R.id.firstImage_show:
                displayExpenseDetails(expenses.get(0));
                break;
            case R.id.previousImage_show:
                if(listIterator.hasPrevious()){
                    expenseObject = listIterator.previous();
                    displayExpenseDetails(expenseObject);
                }else {
                    Toast.makeText(getApplicationContext(), "No prevoius expenses found", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nextImage_show:
               if (listIterator.hasNext()) {
                    expenseObject = listIterator.next();
                    displayExpenseDetails(expenseObject);
                }else {
                   Toast.makeText(getApplicationContext(), "No more expenses found", Toast.LENGTH_SHORT).show();
               }
                break;
            case R.id.lastImage_show:
                displayExpenseDetails(expenses.get(expenses.size() - 1));
                break;
        }
    }

    private void displayExpenseDetails(Expense expenseObject) {
        showExpenseName.setText(expenseObject.getExpenseName());
        showExpenseAmount.setText(expenseObject.getExpenseAmount());
        showExpenseCategory.setText(expenseObject.getExpenseCategory());
        showExpenseDate.setText(expenseObject.getExpenseDate());
        try {
           bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(expenseObject.getExpenseImageUri()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        receipt.setImageBitmap(bitmap);
    }
}
