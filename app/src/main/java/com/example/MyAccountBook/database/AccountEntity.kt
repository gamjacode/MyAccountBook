package com.example.myaccountbook.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long?,        //id
    var category :String, //카테고리(수입 or 지출)
    var money:Int,        //돈
    var date:String       //날짜
)   //라는 표를 만듬