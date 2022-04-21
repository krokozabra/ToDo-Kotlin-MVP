package com.kondaurovigor.diary5000.interfaces

import com.kondaurovigor.diary5000.common.ToDoData

interface EveryInterface {
    fun showList(listToDo: ArrayList<ToDoData>)
    fun startOtherScreen(activity: Class<*>?)
}