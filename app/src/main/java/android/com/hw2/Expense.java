package android.com.hw2;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by murali101002 on 9/8/2016.
 */
public class Expense implements Parcelable, Comparable<Expense> {
    String expenseName, expenseCategory, expenseDate, expenseImageUri, expenseAmount;

    public ArrayList<Expense> getExpenseObjectList() {
        return expenseObjectList;
    }

    public void setExpenseObjectList(ArrayList<Expense> expenseObjectList) {
        this.expenseObjectList = expenseObjectList;
    }

    public Expense() {
    }

    public ArrayList<Expense> expenseObjectList = new ArrayList<>();
    public Expense(String expenseAmount, String expenseCategory, String expenseDate, String expenseImageUri, String expenseName) {
        this.expenseAmount = expenseAmount;
        this.expenseCategory = expenseCategory;
        this.expenseDate = expenseDate;
        this.expenseImageUri = expenseImageUri;
        this.expenseName = expenseName;
    }

    protected Expense(Parcel in) {
        expenseName = in.readString();
        expenseCategory = in.readString();
        expenseDate = in.readString();
        expenseImageUri = in.readString();
        expenseAmount = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(expenseName);
        dest.writeString(expenseCategory);
        dest.writeString(expenseDate);
        dest.writeString(expenseImageUri);
        dest.writeString(expenseAmount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Expense> CREATOR = new Creator<Expense>() {
        @Override
        public Expense createFromParcel(Parcel in) {
            return new Expense(in);
        }

        @Override
        public Expense[] newArray(int size) {
            return new Expense[size];
        }
    };

    public String getExpenseAmount() {
        return expenseAmount;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "expenseAmount='" + expenseAmount + '\'' +
                ", expenseName='" + expenseName + '\'' +
                ", expenseCategory='" + expenseCategory + '\'' +
                ", expenseDate='" + expenseDate + '\'' +
                ", expenseImageUri='" + expenseImageUri + '\'' +
                '}';
    }

    public void setExpenseAmount(String expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(String expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(String expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getExpenseImageUri() {
        return expenseImageUri;
    }

    public void setExpenseImageUri(String expenseImageUri) {
        this.expenseImageUri = expenseImageUri;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    @Override
    public int compareTo(Expense another) {
        if(this.expenseName.compareToIgnoreCase(another.expenseName)>0) return 1;
        else if(this.expenseName.compareToIgnoreCase(another.expenseName)<0) return -1;
        else return 0;
    }
}
