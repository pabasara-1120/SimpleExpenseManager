package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import static android.provider.Telephony.ThreadsColumns.DATE;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {
    private final SQLiteDatabase database;

    public PersistentTransactionDAO(SQLiteDatabase database) {
        this.database = database;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy",java.util.Locale.getDefault());
        ContentValues values = new ContentValues();
        values.put("date_of_Transaction", dateFormat.format(date));
        values.put("account_no", accountNo);
        values.put("expense_type", (expenseType == ExpenseType.INCOME) ? 0 : 1);
        values.put("amount", amount);

        database.insert("Transaction_log", null, values);


    }

    @Override
    public List<Transaction> getAllTransactionLogs()  {
        List<Transaction> transactionList = new ArrayList<>();
        String query = "select*from Transaction_log";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Date date = new Date(cursor.getLong(0));
                String accountNo = cursor.getString(1);

                ExpenseType expenseType = ExpenseType.INCOME;
                if (cursor.getInt(2) == 1) {
                    expenseType = ExpenseType.EXPENSE;
                }
                double amount = cursor.getDouble(3);
                Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
                transactionList.add(transaction);

            }
            while (cursor.moveToNext());

        }
        cursor.close();
        return transactionList;

    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit)  {
        List<Transaction> transactionList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM Transaction_log LIMIT " + limit, null);
        if (cursor.moveToFirst()) {
            do {
                Date date = new Date(cursor.getLong(0));
                String accountNo = cursor.getString(1);

                ExpenseType expenseType = ExpenseType.INCOME;
                if (cursor.getInt(2) == 1) {
                    expenseType = ExpenseType.EXPENSE;
                }
                double amount = cursor.getDouble(3);
                Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
                transactionList.add(transaction);

            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return transactionList;
    }


}
