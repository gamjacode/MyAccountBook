package com.example.myaccountbook.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AccountDAO {
    //insert 메서드를 통해 AccountEntity에 데이터를 삽입
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAccount(account: AccountEntity)

    //get을 통해 기입한 날짜의 Entitiy를 가져와줌
    @Query("SELECT * FROM account WHERE date LIKE :date")
    fun getAccount(date: String): LiveData<List<AccountEntity>>
    //return 값 List<AccountEntity>를 LiveData로 감싸주어 변화를 감지

    @Delete
    fun deleteAccount(account:AccountEntity)

    @Update
    fun updateAccount(account:AccountEntity)
}