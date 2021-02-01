package com.example.myaccountbook.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myaccountbook.database.AccountEntity
import com.example.myaccountbook.repository.AccountRepository

//context->application
class AccountViewModel(application: Application):AndroidViewModel(application) {
    private val repository = AccountRepository(application)

    //DB를 제어할 함수는 Repository에 있는 함수를 이용해 설정해준다.
    fun getAccount(date:String): LiveData<List<AccountEntity>> {
        return repository.getAccount(date)
    }

    fun insertAccount(account:AccountEntity){
        repository.insertAccount(account)
    }

    fun deleteAccount(account: AccountEntity){
        repository.deleteAccount(account)
    }

    fun updateAccount(account:AccountEntity){
        repository.updateAccount(account)
    }
}