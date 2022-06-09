package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;

public class PersistentExpenseManager extends ExpenseManager{
    private Context context;
    public PersistentExpenseManager(Context context)  {
        this.context=context;
        setup();
    }
    @Override
    public void setup()  {
        SQLiteDatabase newDatabase=context.openOrCreateDatabase("200251X",Context.MODE_PRIVATE,null);
        newDatabase.execSQL("CREATE TABLE IF NOT EXISTS Account(" +
                "account_no VARCHAR PRIMARY KEY,"+
                "bank VARCHAR,"+
                "account_holder VARCHAR,"+
                "balance Real"+");");

        newDatabase.execSQL("CREATE TABLE IF NOT EXISTS Transaction_log("+
                "Transaction_id INTEGER PRIMARY KEY,"+
                "account_no VARCHAR,"+
                "expense_type INT,"+
                "amount Real,"+
                "date_of_Transaction DATE,"+
                "FOREIGN KEY (account_no) references Account(account_no)"+
                ");");

        PersistentAccountDAO dao=new PersistentAccountDAO(newDatabase);
        setAccountsDAO(dao);
        PersistentTransactionDAO tdao=new PersistentTransactionDAO(newDatabase);
        setTransactionsDAO(tdao);



    }
}
