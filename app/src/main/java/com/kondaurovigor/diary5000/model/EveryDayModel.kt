package com.kondaurovigor.diary5000.model

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.kondaurovigor.diary5000.common.ToDoData
import com.kondaurovigor.diary5000.database.DBHelper
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.util.ArrayList

class EveryDayModel(dbHelper: DBHelper) {
    private val dbHelper: DBHelper = dbHelper
    fun loadEvery(observer: Observer<ToDoData>) {
        Observable.fromIterable(everyList)
            .subscribeOn(Schedulers.newThread())
            .subscribe(observer)
    }

    //получение списка ежедневок
    private val everyList: ArrayList<ToDoData>
        private get() {
            val everys: ArrayList<ToDoData> = ArrayList<ToDoData>()
            val database: SQLiteDatabase = dbHelper.getWritableDatabase()
            val cursor: Cursor =
                database.query(DBHelper.TABLE_EVERY_DAY_LIST, null, null, null, null, null, null)
            try {
                if (cursor.moveToFirst()) {
                    val idIndex = cursor.getColumnIndex(DBHelper.TWO_KEY_ID)
                    val nameIndex = cursor.getColumnIndex(DBHelper.TWO_NAME_TODO)
                    val descIndex = cursor.getColumnIndex(DBHelper.TWO_DESCRIPTION_TODO)
                    do {
                        val rec = ToDoData(
                            cursor.getInt(idIndex),
                            cursor.getString(nameIndex),
                            "0",
                            "0",
                            "0",
                            cursor.getString(descIndex),
                            0,
                            1
                        )
                        everys.add(rec)
                    } while (cursor.moveToNext())
                } else Log.d("mainLog", "0 rows")
            } catch (e: Exception) {
                Log.d("mainLog", "exept: $e")
            }
            cursor.close()
            dbHelper.close()
            return everys
        }

}