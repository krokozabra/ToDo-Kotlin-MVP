package com.kondaurovigor.diary5000.view

import androidx.appcompat.app.AppCompatActivity
import com.kondaurovigor.diary5000.view.AddQuestView
import android.os.Bundle
import android.content.Intent
import com.kondaurovigor.diary5000.view.MainActivity
import android.app.Activity
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import com.kondaurovigor.diary5000.R
import com.kondaurovigor.diary5000.common.ToDoAdapter
import com.kondaurovigor.diary5000.common.ToDoData
import com.kondaurovigor.diary5000.database.DBHelper
import com.kondaurovigor.diary5000.interfaces.EveryInterface
import com.kondaurovigor.diary5000.model.EveryDayModel
import com.kondaurovigor.diary5000.presenter.EveryDayPresenter
import com.kondaurovigor.diary5000.view.EverydayActivity
import java.util.ArrayList

class EverydayActivity : AppCompatActivity(), EveryInterface {
    private var presenter: EveryDayPresenter? = null
    var list: ListView? = null
    var toDoList: ArrayList<ToDoData> = ArrayList<ToDoData>()
    var toDoAdapter: ToDoAdapter? = null
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.dots_every_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_every_add -> {
                presenter?.startAddQuest(AddQuestView::class.java)
            }
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_everyday)
        init()
    }

    private fun init() {
        title = "Ежедневные квесты"
        list = findViewById(R.id.ae_list)
        toDoAdapter = ToDoAdapter(this, toDoList)
        val dbHelper = DBHelper(this)
        val everyDayModel = EveryDayModel(dbHelper)
        presenter = EveryDayPresenter(everyDayModel)
        presenter?.attachView(this)
        presenter?.viewIsReady()
    }

    override fun showList(listToDo: ArrayList<ToDoData>) {
        toDoList.clear()
        toDoList.addAll(listToDo)
        //прочитать про runOnUiThread
        runOnUiThread { list!!.adapter = toDoAdapter }
    }

    override fun startOtherScreen(activity: Class<*>?) {
        if (activity == AddQuestView::class.java) AddQuestView.start(this)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachView()
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // закрываем эту активити
    }

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, EverydayActivity::class.java)
            activity.startActivity(intent)
        }
    }

}