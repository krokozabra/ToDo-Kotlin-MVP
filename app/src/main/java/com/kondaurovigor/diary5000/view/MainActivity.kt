package com.kondaurovigor.diary5000.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kondaurovigor.diary5000.R
import com.kondaurovigor.diary5000.common.ToDoAdapter
import com.kondaurovigor.diary5000.common.ToDoData
import com.kondaurovigor.diary5000.database.DBHelper
import com.kondaurovigor.diary5000.interfaces.ToDoInterface
import com.kondaurovigor.diary5000.model.ToDoModel
import com.kondaurovigor.diary5000.presenter.ToDoPresenter
import java.util.ArrayList

class MainActivity : AppCompatActivity(), ToDoInterface, SwipeRefreshLayout.OnRefreshListener {
    private var presenter: ToDoPresenter? = null
    var list: ListView? = null
    var toDoList: ArrayList<ToDoData> = ArrayList<ToDoData>()
    var toDoAdapter: ToDoAdapter? = null
    var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    override fun onRefresh() {
        toDoList.clear()
        list!!.adapter = null
        presenter?.loadList()
        mSwipeRefreshLayout?.setRefreshing(false) // останавливает анимацию загрузки
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.dots_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_add -> {
                presenter?.startOtherScreen(AddQuestView::class.java)
            }
            R.id.action_every -> {
                presenter?.startOtherScreen(EverydayActivity::class.java)
            }
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        setTitle("Квесты на сегодня")
        mSwipeRefreshLayout = findViewById(R.id.am_swipe)
        mSwipeRefreshLayout?.setOnRefreshListener(this)
        list = findViewById(R.id.am_list)
        toDoAdapter = ToDoAdapter(this, toDoList)
        val dbHelper = DBHelper(this)
        val toDoModel = ToDoModel(dbHelper)
        presenter = ToDoPresenter(toDoModel)
        presenter?.attachView(this)
        presenter?.viewIsReady()
    }

    override fun showList(listToDo: ArrayList<ToDoData>) {
        toDoList.clear()
        toDoList.addAll(listToDo)
        //прочитать про runOnUiThread
        runOnUiThread(Runnable { list!!.adapter = toDoAdapter })
    }


    override fun startOtherScreen(activity: Class<*>?) {
        when (activity) {
            AddQuestView::class.java -> AddQuestView.start(this)
            EverydayActivity::class.java -> EverydayActivity.start(this)
        }
        finish()

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachView()
    }

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
        }
    }
}