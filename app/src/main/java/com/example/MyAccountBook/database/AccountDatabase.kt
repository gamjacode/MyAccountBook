package com.example.myaccountbook.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AccountEntity::class], version = 1)   //version = 1
abstract class AccountDatabase : RoomDatabase() {//Entity와 Dao를 이어주기 위해 RoomDatabase()상속
    abstract fun accountDao(): AccountDAO  //인터페이스를 상속 받을때 -> abstract

    companion object{ //객체가 단 한번 생성
        private var INSTANCE :AccountDatabase? = null //현재 변수는 현재 어디에서든 null임 (final static)

        //companion object 다른곳에서 변수를 사용할 있으며 넘나들 수 있음 해준다.
        //다른 class에서 AccountDatabase.INSTANCE로 쓰면 수정 가능
        fun getInstance(context: Context):AccountDatabase?{
            if(INSTANCE == null){ //INSTANCE가 한번도 쓰이지 않았을 경우
                synchronized(AccountDatabase::class){ //싱크를 맞추는데 AccountDatabase라는 클래스를 맞출거다.
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        //Room.databaseBuilder : 앞으로 db를 객체화 시킬꺼다(applicationContext 에서)
                        //무엇을? AccountDatabase를 객체 이름은 account라고 지정
                        AccountDatabase::class.java,
                        "account"
                    ).fallbackToDestructiveMigration()
                        //버전 0과 버전1을 싱크를 맞출때 버전1로 덮어 씌운다.
                        .build()
                        //이 빌더의 마무리
                }
            }
            return INSTANCE
        }
    }
}