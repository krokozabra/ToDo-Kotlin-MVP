package com.kondaurovigor.diary5000.interfaces

import com.kondaurovigor.diary5000.common.ToDoData


interface InfoQuestInterface {
    //должно быть 2 метода возвращения id и ежедневок
    fun getID(): Int
    fun getEvery(): Int
    fun showQuest(quest: ToDoData?)
    fun startOtherScreen(activity: Class<*>?)

    /*    ToDoData deleteQuest();
    ToDoData completeQuest();*/
    fun getCurrentQuest(): ToDoData
}