package android.com.hw2;

import java.util.ArrayList;

/**
 * Created by murali101002 on 9/9/2016.
 */
public class ExpenseObjectList {
    public ArrayList<Expense> getExpenseObjectList() {
        return expenseObjectList;
    }

    public void setExpenseObjectList(ArrayList<Expense> expenseObjectList) {
        this.expenseObjectList = expenseObjectList;
    }

    public ArrayList<Expense> expenseObjectList = new ArrayList<>();
}
