package com.avinash.expensetracker.transactionDb;




import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TransactionDao {

    @Query("select * from transactionTable order by date DESC")
    LiveData<List<TransactionEntry>> loadAllTransactions();

    @Query("select * from transactionTable where id = :id")
    LiveData<TransactionEntry> loadExpenseById(int id);

    @Query("select sum(amount) from transactionTable where transactionType =:transactionType")
    int getAmountByTransactionType(String transactionType);

    @Query("select sum(amount) from transactionTable where transactionType =:transactionType and  date between :startDate and :endDate")
    int getAmountbyCustomDates(String transactionType,long startDate,long endDate);

    @Query("select sum(amount) from transactionTable where category=:category")
    int getSumExpenseByCategory(String category);

    @Query("select sum(amount) from transactionTable where category=:category and date between :startDate and :endDate")
    int getSumExpenseByCategoryCustomDate(String category,long startDate, long endDate);

    @Query("select min(date) from transactionTable ")
    long getFirstDate();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertExpense(TransactionEntry transactionEntry);

    @Delete
    void removeExpense(TransactionEntry transactionEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateExpenseDetails(TransactionEntry transactionEntry);
}
