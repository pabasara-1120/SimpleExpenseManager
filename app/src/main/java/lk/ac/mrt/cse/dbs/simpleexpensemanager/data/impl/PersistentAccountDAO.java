package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;


public class PersistentAccountDAO implements AccountDAO {
    private final SQLiteDatabase database;

    public PersistentAccountDAO(SQLiteDatabase database){
        this.database=database;
    }


    @Override
    public List<String> getAccountNumbersList() {
        List<String> accNo=new ArrayList<>();
        Cursor cursor = database.query("Account", new String[]{"account_no"}, null, null, null, null, null);

        if(cursor.moveToFirst()){
            do {
                accNo.add(cursor.getString(0));
            }
            while(cursor.moveToNext());
            }

        cursor.close();
        return accNo;

    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accounts=new ArrayList<>();
        Cursor cursor=database.query("Account",new String[]{"account_no","bank","account_holder","balance"},null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                accounts.add(new Account(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getDouble(3)));
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account account;
        Cursor cursor = database.query("Account",new String[]{"account_no","bank","account_holder","balance"},"account_no=?",new String[]{accountNo},null,null,null);
        if(cursor!=null){
            cursor.moveToFirst();
        }
        account=new Account(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getDouble(3));
        cursor.close();
        return account;

    }

    @Override
    public void addAccount(Account account) {
        ContentValues values=new ContentValues();
        values.put("account_no",account.getAccountNo());
        values.put("bank",account.getBankName());
        values.put("account_holder",account.getAccountHolderName());
        values.put("balance",account.getBalance());
        database.insert("Account",null,values);

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        database.delete("Account","account_no=?",new String[]{String.valueOf(accountNo)});

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Cursor cursor=database.query("Account",new String[]{"account_no","bank","account_holder","balance"},"account_no?",new String[]{accountNo},null,null,null);
        if(cursor!=null){
            cursor.moveToFirst();
        }
        double balance=cursor.getDouble(3);

        ContentValues values=new ContentValues();
        if(expenseType==ExpenseType.EXPENSE)
            values.put("balance",balance-amount);
        else if(expenseType==ExpenseType.INCOME)
            values.put("balance",balance+amount);
        cursor.close();



    }
}
