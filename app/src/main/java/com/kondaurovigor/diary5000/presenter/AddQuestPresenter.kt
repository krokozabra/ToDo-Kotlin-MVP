package com.kondaurovigor.diary5000.presenter

import com.kondaurovigor.diary5000.common.ToDoData
import com.kondaurovigor.diary5000.interfaces.AddQuestInterface
import com.kondaurovigor.diary5000.model.QuestModel
import com.kondaurovigor.diary5000.view.MainActivity

class AddQuestPresenter(model: QuestModel) {
    private var view: AddQuestInterface? = null
    private val model: QuestModel
    fun attachView(view: AddQuestInterface?) {
        this.view = view
    }

    fun detachView() {
        view = null
    }

    fun viewIsReady() {
        //пока не знаю, какой метод запускать, если это вообще нужно
    }

    fun clickDate() {
        view?.setDate()
    }

    fun addNewQuest() {
        //вызов метода view которые собирает данные и возвращает их сюда
        val newQuest: ToDoData? = view?.getNewQuest()
        //вызов метода модели с передачей в него нового квеста
        model.addQuest(newQuest)
        view?.startOtherScreen(MainActivity::class.java)
    }

    init {
        this.model = model
    }
}