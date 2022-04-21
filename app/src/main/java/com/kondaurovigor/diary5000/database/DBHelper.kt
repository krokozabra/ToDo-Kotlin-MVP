package com.kondaurovigor.diary5000.database

import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.Context

class DBHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE " + TABLE_TO_DO_LIST + "(" +
                    ONE_KEY_ID + " INTEGER PRIMARY KEY, " +
                    ONE_NAME_TODO + " TEXT," +
                    ONE_DESCRIPTION_TODO + " TEXT," +
                    ONE_DAY_TODO + " NUMERIC," +
                    ONE_MONTH_TODO + " NUMERIC," +
                    ONE_YEAR_TODO + " NUMERIC," +
                    ONE_OK_TODO + " NUMERIC" +
                    ")"
        )
        db.execSQL(
            "CREATE TABLE " + TABLE_EVERY_DAY_LIST + "(" +
                    TWO_KEY_ID + " INTEGER PRIMARY KEY, " +
                    TWO_NAME_TODO + " TEXT," +
                    TWO_DESCRIPTION_TODO + " TEXT" +
                    ")"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //при желании реализовать логику сохранения информации предыдущей информации БД, что храниласть в предыдущей версии БД
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TO_DO_LIST")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EVERY_DAY_LIST")
        onCreate(db)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "todoDB"
        const val TABLE_TO_DO_LIST = "todoList"
        const val TABLE_EVERY_DAY_LIST = "everyDayList"
        const val ONE_KEY_ID = "_id"
        const val ONE_NAME_TODO = "name"
        const val ONE_DESCRIPTION_TODO = "description"
        const val ONE_DAY_TODO = "day"
        const val ONE_MONTH_TODO = "month"
        const val ONE_YEAR_TODO = "year"
        const val ONE_OK_TODO = "OK"
        const val TWO_KEY_ID = "_id"
        const val TWO_NAME_TODO = "name"
        const val TWO_DESCRIPTION_TODO = "description"
    }
}