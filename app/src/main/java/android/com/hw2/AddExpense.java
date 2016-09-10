package android.com.hw2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Calendar;

public class AddExpense extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    public static final int PICK_IMAGE = 1000;
    EditText expenseName, expenseAmount, expenseDate;
    Spinner spinner;
    ImageView receipt;
    Button addNewExpense;
    Calendar myCalendar;
    boolean isItemSelected = false, isDateSelected = false;
    String name, item, date, uri, amount;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            uri = String.valueOf(imageUri);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            receipt.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        uri = String.valueOf(Uri.parse("android.resource://android.com.hw2/drawable/no_image.png"));
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener datePicker= new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                isDateSelected = true;
                String month, day;
                if(monthOfYear<10){
                    month = "0"+(monthOfYear+1);
                }else month = String.valueOf(monthOfYear);
                if(dayOfMonth<10){
                    day = "0"+dayOfMonth;
                }else day = String.valueOf(dayOfMonth);
                date = month+"/"+day+"/"+year;
                expenseDate.setText(date);
            }

        };

        expenseName = (EditText)findViewById(R.id.newExpense_add);
//        expenseName.requestFocus();
        expenseAmount = (EditText)findViewById(R.id.amount_add);

        expenseDate = (EditText)findViewById(R.id.date_add);
        expenseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(AddExpense.this,datePicker,myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        spinner = (Spinner)findViewById(R.id.category_items);
        ArrayAdapter<CharSequence> categories_list = ArrayAdapter.createFromResource(this, R.array.categories,android.R.layout.simple_spinner_item);
        categories_list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(categories_list);
        spinner.setOnItemSelectedListener(this);

        receipt = (ImageView)findViewById(R.id.bill_add);
        receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                imageIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                imageIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(imageIntent,"Select Picture"),PICK_IMAGE);
            }
        });

        addNewExpense = (Button)findViewById(R.id.btn_addExpense_add);
        addNewExpense.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        name = expenseName.getText().toString().trim();
        amount = expenseAmount.getText().toString().trim();
        if(name.length() == 0){
            Toast.makeText(getApplicationContext(),"Please enter Expense name",Toast.LENGTH_SHORT).show();
        }else if(amount.length() == 0 || Double.parseDouble(amount) == 0){
            Toast.makeText(getApplicationContext(),"Please enter valid value of your Expense",Toast.LENGTH_SHORT).show();
        }else if(isDateSelected == false){
            Toast.makeText(getApplicationContext(),"Please select Expense date",Toast.LENGTH_SHORT).show();
        }else if(item.equals("Select Category")){
            Toast.makeText(getApplicationContext(),"Please select valid Expense category",Toast.LENGTH_SHORT).show();
        }else{
            Expense expense = new Expense(amount,item,date,uri,name);

            Intent resultIntent = new Intent();
            resultIntent.putExtra(MainActivity.RESULT_KEY,expense);
            setResult(RESULT_OK,resultIntent);
            finish();
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        expenseName.clearFocus();
//        expenseAmount.requestFocus();
        isItemSelected = true;
        item = (String) parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
