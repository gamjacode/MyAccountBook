package com.example.myaccountbook.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.myaccountbook.database.AccountDatabase
import com.example.myaccountbook.database.AccountEntity
import java.lang.Exception
//Repository는 쿼리를 관리하고 여러 백엔드를 사용할 수 있도록 함
class AccountRepository(application: Application) {
    private val accountDatabase = AccountDatabase.getInstance(application)!! //싱크 맞춤
    private val accountDao = accountDatabase.accountDao()   //Dao를 쓸수있음

    //ViewModel에서 쓰기 위한 DB에 접근을 요청할 때 수행할 함수를 만들어둔다.
    //insert를 하여 db에 저장한 후 recyclerview에 보여주기 위해 Dao안의 getAll()을 return하여 Enitity의 정보를 가져옴
    fun getAccount(date: String): LiveData<List<AccountEntity>> {
        return accountDao.getAccount(date)
    }

    //Room DB를 메인 스레드에서 접근하려 하면 크래쉬가 발생한다. 따라서 별도의 스레드에서 Room의 데이터에 접근해야 한다.
    //insert를 하여 db에 저장
    fun insertAccount(account:AccountEntity){
        try {
            val thread = Thread(Runnable {
                accountDao.insertAccount(account)
            })
            thread.start()
        } catch (exception: Exception) {
        }
    }
    //delete를 함
    fun deleteAccount(account:AccountEntity){
        try {
            val thread = Thread(Runnable {
                accountDao.deleteAccount(account)
            })
            thread.start()
        } catch (exception: Exception) {
        }
    }
    //update
    fun updateAccount(account: AccountEntity){
        try {
            val thread = Thread(Runnable {
                accountDao.updateAccount(account)
            })
            thread.start()
        } catch (exception: Exception) {
        }
    }
}