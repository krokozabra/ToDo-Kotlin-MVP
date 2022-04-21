package com.kondaurovigor.diary5000.interfaces

import com.kondaurovigor.diary5000.common.ToDoData

interface AddQuestInterface {

    fun getNewQuest(): ToDoData?
    fun startOtherScreen(activity: Class<*>?)
    fun setDate()
    fun showDate()
}