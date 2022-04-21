package com.kondaurovigor.diary5000.presenter

import com.kondaurovigor.diary5000.common.ToDoData
import com.kondaurovigor.diary5000.interfaces.ToDoInterface
import com.kondaurovigor.diary5000.model.ToDoModel
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.util.ArrayList

class ToDoPresenter(model: ToDoModel) {
    private var view: ToDoInterface? = null
    private val model: ToDoModel
    private val toDoList: ArrayList<ToDoData> = ArrayList<ToDoData>()
    fun attachView(view: ToDoInterface?) {
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
        model.loadToDo(observer)
    }

    fun startOtherScreen(activity: Class<*>?) {
        view?.startOtherScreen(activity)
    }

    var observer: Observer<ToDoData?> = object : Observer<ToDoData?> {
        override fun onSubscribe(d: Disposable) {
            println("onSubscribe: ")
            toDoList.clear()
        }

        override fun onNext(toDoData: ToDoData) {
            toDoList.add(toDoData)
        }

        override fun onError(e: Throwable) {
            println("onError: $e")
        }

        override fun onComplete() {
            println("onComplete: All Done!")
            view?.showList(toDoList)
        }
    }

    init {
        this.model = model
    }
}