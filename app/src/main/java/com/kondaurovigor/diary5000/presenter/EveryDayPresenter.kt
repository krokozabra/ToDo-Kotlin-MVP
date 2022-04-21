package com.kondaurovigor.diary5000.presenter

import com.kondaurovigor.diary5000.common.ToDoData
import com.kondaurovigor.diary5000.interfaces.EveryInterface
import com.kondaurovigor.diary5000.model.EveryDayModel
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.util.ArrayList

class EveryDayPresenter(model: EveryDayModel) {
    private var view: EveryInterface? = null
    private val model: EveryDayModel
    private val everyList: ArrayList<ToDoData> = ArrayList<ToDoData>()
    fun attachView(view: EveryInterface?) {
        this.view = view
    }

    fun detachView() {
        view = null
    }

    fun viewIsReady() {
        loadList()
    }

    fun loadList() {
        //RxJava
        model.loadEvery(observer)
    }

    fun startAddQuest(activity: Class<*>?) {
        view?.startOtherScreen(activity)
    }

    var observer: Observer<ToDoData> = object : Observer<ToDoData> {
        override fun onSubscribe(d: Disposable) {
            println("onSubscribe: ")
            everyList.clear()
        }

        override fun onNext(toDoData: ToDoData) {
            everyList.add(toDoData)
        }

        override fun onError(e: Throwable) {
            println("onError: $e")
        }

        override fun onComplete() {
            println("onComplete: All Done!")
            view?.showList(everyList)
        }
    }

    init {
        this.model = model
    }
}