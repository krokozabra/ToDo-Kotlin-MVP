package com.kondaurovigor.diary5000.presenter

import com.kondaurovigor.diary5000.common.ToDoData
import com.kondaurovigor.diary5000.interfaces.InfoQuestInterface
import com.kondaurovigor.diary5000.model.QuestModel
import com.kondaurovigor.diary5000.view.EverydayActivity
import com.kondaurovigor.diary5000.view.MainActivity
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class InfoQuestPresenter(private val model: QuestModel) {
    private var view: InfoQuestInterface? = null
    fun attachView(view: InfoQuestInterface?) {
        this.view = view
    }

    fun detachView() {
        view = null
    }

    var observer: Observer<ToDoData?> = object : Observer<ToDoData?> {
        override fun onSubscribe(d: Disposable) {
            println("onSubscribe: ")
        }

        override fun onNext(toDoData: ToDoData) {
            view?.showQuest(toDoData)
        }

        override fun onError(e: Throwable) {
            println("onError: $e")
        }

        override fun onComplete() {
            println("onComplete: All Done!")
        }
    }

    fun viewIsReady() {
        //загрузка текущего таска
        model.getQuest(view!!.getID(), view!!.getEvery(), observer)
    }

    fun deleteQuest() {
        val delQuest : ToDoData? = view?.getCurrentQuest()
        model.deleteQuest(delQuest)
        view?.startOtherScreen(MainActivity::class.java)
    }

    fun deleteEveryQuest() {
        val delQuest : ToDoData? = view?.getCurrentQuest()
        model.deleteEveryQuest(delQuest)
        view!!.startOtherScreen(EverydayActivity::class.java)
    }


    fun completeQuest() {
        //вызов метода view которые собирает данные и возвращает их сюда
        val complQuest: ToDoData? = view?.getCurrentQuest()
        complQuest?.ok =1;
        model.completeQuest(complQuest)
        view?.startOtherScreen(MainActivity::class.java)//должен передавать class объект что бы на него потом перейти
    }

}