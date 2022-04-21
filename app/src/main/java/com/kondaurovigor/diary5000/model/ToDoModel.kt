package com.kondaurovigor.diary5000.model

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.kondaurovigor.diary5000.common.ToDoData
import com.kondaurovigor.diary5000.database.DBHelper
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.schedulers.Schedulers
import java.util.*


class ToDoModel(dbHelper: DBHelper) {
    private val dbHelper: DBHelper = dbHelper
    fun loadToDo(observer: Observer<ToDoData?>?) {
        Observable.fromIterable(getToDoList())
            .subscribeOn(Schedulers.newThread())
            .subscribe(observer!!)
    }//проверка на ежедневные задания сегодня

    //получение списка заданий на сегодняшний день
    private fun getToDoList(): ArrayList<ToDoData>{
            //проверка на ежедневные задания сегодня
            checkEverydayInTask()
            val todos: ArrayList<ToDoData> = ArrayList<ToDoData>()
            val today = Calendar.getInstance()
            val database: SQLiteDatabase = dbHelper.getWritableDatabase()
            Log.d("mainLog", "today = " + today[Calendar.DAY_OF_MONTH])
            val cursor: Cursor = database.query(
                DBHelper.TABLE_TO_DO_LIST,
                null,
                DBHelper.ONE_DAY_TODO.toString() + " = " + today[Calendar.DAY_OF_MONTH] + " AND " + DBHelper.ONE_MONTH_TODO + " = " + today[Calendar.MONTH] + " AND " + DBHelper.ONE_YEAR_TODO + " = " + today[Calendar.YEAR],
                null,
                null,
                null,
                null
            )
            try {
                if (cursor.moveToFirst()) {
                    val idIndex = cursor.getColumnIndex(DBHelper.ONE_KEY_ID)
                    val nameIndex = cursor.getColumnIndex(DBHelper.ONE_NAME_TODO)
                    val descIndex = cursor.getColumnIndex(DBHelper.ONE_DESCRIPTION_TODO)
                    val dayIndex = cursor.getColumnIndex(DBHelper.ONE_DAY_TODO)
                    val monthIndex = cursor.getColumnIndex(DBHelper.ONE_MONTH_TODO)
                    val yearIndex = cursor.getColumnIndex(DBHelper.ONE_YEAR_TODO)
                    val OKIndex = cursor.getColumnIndex(DBHelper.ONE_OK_TODO)
                    do {
                        val rec = ToDoData(
                            cursor.getInt(idIndex),
                            cursor.getString(nameIndex),
                            cursor.getString(dayIndex),
                            cursor.getString(monthIndex),
                            cursor.getString(yearIndex),
                            cursor.getString(descIndex),
                            cursor.getInt(OKIndex),
                            0
                        )
                        todos.add(rec)
                    } while (cursor.moveToNext())
                } else Log.d("mainLog", "0 rows")
            } catch (e: Exception) {
                Log.d("mainLog", "exept: $e")
            }
            cursor.close()
            dbHelper.close()
            return todos
        }

    fun checkEverydayInTask() {
        val today = Calendar.getInstance()
        val database: SQLiteDatabase = dbHelper.getWritableDatabase()
        var cursor: Cursor
        Log.d(LOG_TAG, "---INNER JOIN with query---")
        val table: String =
            DBHelper.TABLE_TO_DO_LIST.toString() + " as t, " + DBHelper.TABLE_EVERY_DAY_LIST + " as e "
        val columns = arrayOf("*")
        val selection = "(" +
                "t." + DBHelper.ONE_DAY_TODO + " = " + today[Calendar.DAY_OF_MONTH] + " AND " +
                "t." + DBHelper.ONE_MONTH_TODO + " = " + today[Calendar.MONTH] + " AND " +
                "t." + DBHelper.ONE_YEAR_TODO + " = " + today[Calendar.YEAR] + " AND " +
                "t." + DBHelper.ONE_NAME_TODO + " = e." + DBHelper.TWO_NAME_TODO + " AND " +
                "t." + DBHelper.ONE_DESCRIPTION_TODO + " = e." + DBHelper.TWO_DESCRIPTION_TODO + ")"
        cursor = database.query(table, columns, selection, null, null, null, null)
        val countEveryInToDo = cursor.count
        println("размер курсора =" + cursor.count)
        cursor.close()
        cursor = database.query(DBHelper.TABLE_EVERY_DAY_LIST, null, null, null, null, null, null)
        val countEveryDay = cursor.count
        cursor.close()
        if (countEveryInToDo != countEveryDay) {
            insertInToDo()
        }
        dbHelper.close()
    }

    fun insertInToDo() {
        val today = Calendar.getInstance()
        val database = dbHelper.writableDatabase
        val intersectQuery = "SELECT name, description FROM everyDayList " +
                "EXCEPT " +
                "SELECT name, description FROM todoList WHERE" +
                "(" +
                DBHelper.ONE_DAY_TODO + " = " + today[Calendar.DAY_OF_MONTH] + " AND " +
                DBHelper.ONE_MONTH_TODO + " = " + today[Calendar.MONTH] + " AND " +
                DBHelper.ONE_YEAR_TODO + " = " + today[Calendar.YEAR] + ")"
        val cursorEveryDay = database.rawQuery(intersectQuery, null)
        val nameIndexEvery = cursorEveryDay.getColumnIndex(DBHelper.TWO_NAME_TODO)
        val descIndexEvery = cursorEveryDay.getColumnIndex(DBHelper.TWO_DESCRIPTION_TODO)
        if (cursorEveryDay.moveToFirst()) do {
            val cv = ContentValues()
            try {
                cv.put(DBHelper.ONE_NAME_TODO, cursorEveryDay.getString(nameIndexEvery))
                cv.put(DBHelper.ONE_DESCRIPTION_TODO, cursorEveryDay.getString(descIndexEvery))
                cv.put(DBHelper.ONE_DAY_TODO, today[Calendar.DAY_OF_MONTH])
                cv.put(DBHelper.ONE_MONTH_TODO, today[Calendar.MONTH])
                cv.put(DBHelper.ONE_YEAR_TODO, today[Calendar.YEAR])
                cv.put(DBHelper.ONE_OK_TODO, 0)
                database.insert(DBHelper.TABLE_TO_DO_LIST, null, cv)
            } catch (e: Exception) {
                Log.d("mainLog", "exept: $e")
            }
        } while (cursorEveryDay.moveToNext())
        cursorEveryDay.close()
        dbHelper.close()
    }

    companion object {
        const val LOG_TAG = "work width ToDoModel"
    }

}