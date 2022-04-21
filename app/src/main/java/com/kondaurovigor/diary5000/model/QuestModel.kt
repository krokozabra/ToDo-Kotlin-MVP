package com.kondaurovigor.diary5000.model

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.kondaurovigor.diary5000.common.ToDoData
import com.kondaurovigor.diary5000.database.DBHelper
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class QuestModel(dbHelper: DBHelper) {
    private val dbHelper: DBHelper = dbHelper
    fun addQuest(newQuest: ToDoData?) {
        Observable.just(newQuest)
            .subscribeOn(Schedulers.newThread())
            .subscribe(observerAdd)
    }

    private var observerAdd: Observer<ToDoData?> = object : Observer<ToDoData?> {
        override fun onSubscribe(d: Disposable) {
            println("onSubscribe: ")
        }

       override fun onNext(toDoData: ToDoData) {
            addNewQuest(toDoData)
        }

        override fun onError(e: Throwable) {
            println("onError: ")
        }

        override fun onComplete() {
            println("onComplite: ")
        }
    }

    fun deleteQuest(quest: ToDoData?) {
        Observable.just(quest)
            .subscribeOn(Schedulers.newThread())
            .subscribe(observerDel)
    }

    var observerDel: Observer<ToDoData?> = object : Observer<ToDoData?> {
        override fun onSubscribe(d: Disposable) {
            println("onSubscribe: ")
        }

        override fun onNext(toDoData: ToDoData) {
            deleteDBQuest(toDoData)
        }

        override fun onError(e: Throwable) {
            println("onError: ")
        }

        override fun onComplete() {
            println("onComplete: All Done!")
        }
    }

    fun completeQuest(quest: ToDoData?) {
        Observable.just(quest)
            .subscribeOn(Schedulers.newThread())
            .subscribe(observerComplete)
    }

    var observerComplete: Observer<ToDoData?> = object : Observer<ToDoData?> {
        override fun onSubscribe(d: Disposable) {
            println("onSubscribe: ")
        }

        override fun onNext(quest: ToDoData) {
            completeDBQuest(quest)
        }

        override fun onError(e: Throwable) {
            println("onError: ")
        }

        override fun onComplete() {
            println("onComplete: All Done!")
        }
    }

    fun getQuest(id: Int, every: Int, observerGet: Observer<ToDoData?>) {
        Observable.just(getDBQuest(id, every))
            .subscribeOn(Schedulers.newThread())
            .subscribe(observerGet)
    }

    //Добавление нового квеста
    //добавить в выполнение в observer
    private fun addNewQuest(quest: ToDoData) {
        val database: SQLiteDatabase = dbHelper.getWritableDatabase()
        val contentValues = ContentValues()
        if (quest.everyday == 1) {
            contentValues.put(DBHelper.TWO_NAME_TODO, quest.name)
            contentValues.put(DBHelper.TWO_DESCRIPTION_TODO, quest.description)
            database.insert(DBHelper.TABLE_EVERY_DAY_LIST, null, contentValues)
        } else {
            contentValues.put(DBHelper.ONE_NAME_TODO, quest.name)
            contentValues.put(DBHelper.ONE_DESCRIPTION_TODO, quest.description)
            contentValues.put(DBHelper.ONE_DAY_TODO, quest.day)
            contentValues.put(DBHelper.ONE_MONTH_TODO, quest.month)
            contentValues.put(DBHelper.ONE_YEAR_TODO, quest.year)
            contentValues.put(DBHelper.ONE_OK_TODO, 0)
            database.insert(DBHelper.TABLE_TO_DO_LIST, null, contentValues)
        }
        dbHelper.close()
    }

    private fun getDBQuest(id: Int, everyday: Int): ToDoData {
        val database: SQLiteDatabase = dbHelper.getWritableDatabase()
        var currentQuest = ToDoData()
        val cursor: Cursor
        if (everyday == 0) {
            cursor = database.query(
                DBHelper.TABLE_TO_DO_LIST,
                null,
                DBHelper.ONE_KEY_ID + " = " + id,
                null,
                null,
                null,
                null
            )
            cursor.moveToFirst()
            val idIndex = cursor.getColumnIndex(DBHelper.ONE_KEY_ID)
            val nameIndex = cursor.getColumnIndex(DBHelper.ONE_NAME_TODO)
            val descIndex = cursor.getColumnIndex(DBHelper.ONE_DESCRIPTION_TODO)
            val dayIndex = cursor.getColumnIndex(DBHelper.ONE_DAY_TODO)
            val monthIndex = cursor.getColumnIndex(DBHelper.ONE_MONTH_TODO)
            val yearIndex = cursor.getColumnIndex(DBHelper.ONE_YEAR_TODO)
            val OKIndex = cursor.getColumnIndex(DBHelper.ONE_OK_TODO)
            println("cursor query = $id")
            println("cursor size = " + cursor.count)
            println(cursor.getInt(idIndex))
            println(cursor.getString(nameIndex))
            println(cursor.getString(dayIndex))
            println(dayIndex)
            println(monthIndex)
            println(yearIndex)
            println(OKIndex)
            currentQuest = ToDoData(
                cursor.getInt(idIndex),
                cursor.getString(nameIndex),
                cursor.getString(dayIndex),
                cursor.getString(monthIndex),
                cursor.getString(yearIndex),
                cursor.getString(descIndex),
                cursor.getInt(OKIndex),
                everyday
            )
        } else {
            //что то тут не чисто. подумать надо над логикой вывода из этого метода. может даже убрать вывод из ежедневной таблицы
            cursor = database.query(
                DBHelper.TABLE_EVERY_DAY_LIST,
                null,
                DBHelper.TWO_KEY_ID.toString() + " = " + id,
                null,
                null,
                null,
                null
            )
            cursor.moveToFirst()
            val idIndex = cursor.getColumnIndex(DBHelper.TWO_KEY_ID)
            val nameIndex = cursor.getColumnIndex(DBHelper.TWO_NAME_TODO)
            val descIndex = cursor.getColumnIndex(DBHelper.TWO_DESCRIPTION_TODO)
            currentQuest = ToDoData(
                cursor.getInt(idIndex),
                cursor.getString(nameIndex),
                "0",
                "0",
                "0",
                cursor.getString(descIndex),
                0,
                everyday
            )
        }
        cursor.close()
        dbHelper.close()
        return currentQuest
    }

    @SuppressLint("CheckResult")
    fun deleteEveryQuest(quest: ToDoData?) {
        Observable.just<ToDoData>(quest)
            .subscribeOn(Schedulers.newThread())
            .subscribe(observerEveryDel)
    }

    var observerEveryDel: Observer<ToDoData> = object : Observer<ToDoData> {
        override fun onSubscribe(d: Disposable) {
            println("onSubscribe: ")
        }

        override fun onNext(toDoData: ToDoData) {
            deleteEveryDBQuest(toDoData)
        }

        override fun onError(e: Throwable) {
            println("onError: ")
        }

        override fun onComplete() {
            println("onComplete: All Done!")
        }
    }

    private fun completeDBQuest(quest: ToDoData) {
        val database: SQLiteDatabase = dbHelper.getWritableDatabase()
        val cv = ContentValues()
        cv.put(DBHelper.ONE_OK_TODO, quest.ok)
        database.update(
            DBHelper.TABLE_TO_DO_LIST,
            cv,
            DBHelper.ONE_KEY_ID + " = " + quest.id,
            null
        )
        dbHelper.close()
    }

    private fun deleteDBQuest(quest: ToDoData) {
        val database: SQLiteDatabase = dbHelper.writableDatabase
        database.delete(
            DBHelper.TABLE_TO_DO_LIST,
            DBHelper.ONE_KEY_ID+ " = " + quest.id,
            null
        )
        dbHelper.close()
    }

    private fun deleteEveryDBQuest(quest: ToDoData) {
        val database = dbHelper.writableDatabase
        database.delete(
            DBHelper.TABLE_EVERY_DAY_LIST,
            DBHelper.ONE_KEY_ID + " = " + quest.id,
            null
        )
        dbHelper.close()
    }

    companion object {
        const val LOG_TAG = "work width QuestModel"
    }

}