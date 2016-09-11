package android.com.hw2;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;

public class EditActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Button selectExpense, save, cancel;
    EditText expName, expAmount, expDate;
    ImageView receipt;
    Spinner items;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener datePicker;
    String name, item, date, uri, amount;
    boolean isItemSelected = false;
    ArrayList<Expense> expenseList;
    ArrayList<String> expenseNameList = new ArrayList<>();
    CharSequence[] expenseNames;
    String selectedExpenseName;
    String[] categories;
    Bitmap bitmap;
    int objectPosition;
    AlertDialog alertDialog;
    ArrayAdapter<CharSequence> categories_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        if (getIntent().getExtras() == null) {
            setResult(RESULT_CANCELED);
            finish();
        }
        categories = getResources().getStringArray(R.array.categories);
        expenseList = (ArrayList<Expense>) getIntent().getExtras().getSerializable(MainActivity.EXPENSE_LIST);
        myCalendar = Calendar.getInstance();

        for (Expense expense : expenseList) {
            expenseNameList.add(expense.expenseName);
        }

        if (expenseNameList.size() > 1) Collections.sort(expenseNameList);
        expenseNames = expenseNameList.toArray(new CharSequence[expenseNameList.size()]);
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

        final DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                String month, day;
                if (monthOfYear < 10) {
                    month = "0" + (monthOfYear+1);
                } else month = String.valueOf(monthOfYear);
                if (dayOfMonth < 10) {
                    day = "0" + dayOfMonth;
                } else day = String.valueOf(dayOfMonth);
                date = month + "/" + day + "/" + year;
                expDate.setText(date);
            }

        };
        selectExpense = (Button) findViewById(R.id.btn_slctExp);
        save = (Button) findViewById(R.id.btn_saveExpense_edit);
        cancel = (Button) findViewById(R.id.btn_cancelExpense_edit);
        expName = (EditText) findViewById(R.id.newExpense_edit);
//        expName.requestFocus();
        expAmount = (EditText) findViewById(R.id.amount_edit);
        expDate = (EditText) findViewById(R.id.date_edit);
        expDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isItemSelected = true;
                new DatePickerDialog(EditActivity.this, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        receipt = (ImageView) findViewById(R.id.bill_edit);
        receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                imageIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                imageIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(imageIntent, "Select Picture"), AddExpense.PICK_IMAGE);
            }
        });
        items = (Spinner) findViewById(R.id.category_items_edit);
        categories_list = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        categories_list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        items.setAdapter(categories_list);
        items.setOnItemSelectedListener(this);
        selectExpense.setOnClickListener(this);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AddExpense.PICK_IMAGE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            uri = String.valueOf(imageUri);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            receipt.setImageBitmap(bitmap);
        }
    }

    private void setExpenseDetails(Expense expense) {
        expName.setText(expense.expenseName);
        expAmount.setText(expense.expenseAmount);
        expDate.setText(expense.expenseDate);
        for (String category : categories) {
            if (category.equals(expense.expenseCategory)) {
                items.setSelection(((ArrayAdapter<String>) items.getAdapter()).getPosition(category));
            }
        }
        if(!expense.getExpenseImageUri().equals("")) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(expense.getExpenseImageUri()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            Drawable myDrawable = getResources().getDrawable(R.drawable.no_image);
            bitmap      = ((BitmapDrawable) myDrawable).getBitmap();
        }
        receipt.setImageBitmap(bitmap);
        objectPosition = expenseList.indexOf(expense);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_slctExp:
                alertDialog.show();
                break;
            case R.id.btn_saveExpense_edit:
                name = expName.getText().toString().trim();
                amount = expAmount.getText().toString().trim();
                if (name.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter Expense name", Toast.LENGTH_SHORT).show();
                } else if (amount.length() == 0 || Double.parseDouble(amount) == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter valid value of your Expense", Toast.LENGTH_SHORT).show();
                } else if(item.equals("Select Category")){
                    Toast.makeText(getApplicationContext(),"Please select valid Expense category",Toast.LENGTH_SHORT).show();
                }else {
                    expenseList.get(objectPosition).setExpenseName(name);
                    expenseList.get(objectPosition).setExpenseAmount(amount);
                    expenseList.get(objectPosition).setExpenseDate(expDate.getText().toString().trim());
                    expenseList.get(objectPosition).setExpenseCategory(item);
                    expenseList.get(objectPosition).setExpenseImageUri(uri);
                    expenseList.set(objectPosition,expenseList.get(objectPosition));
                    Intent editIntent = new Intent();
                    editIntent.putExtra(MainActivity.EDIT_EXPENSE_LIST,expenseList);
                    setResult(RESULT_OK,editIntent);
                    finish();
                }
                break;
            case R.id.btn_cancelExpense_edit:
                finish();
                break;

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        expName.clearFocus();
//        expAmount.requestFocus();
        item = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
