package com.kondaurovigor.diary5000.view

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kondaurovigor.diary5000.R
import com.kondaurovigor.diary5000.common.ToDoData
import com.kondaurovigor.diary5000.database.DBHelper
import com.kondaurovigor.diary5000.interfaces.InfoQuestInterface
import com.kondaurovigor.diary5000.model.QuestModel
import com.kondaurovigor.diary5000.presenter.InfoQuestPresenter

class InfoView : AppCompatActivity(), InfoQuestInterface {
    private var presenter: InfoQuestPresenter? = null
    private var every = 0
        private set

    override fun getEvery(): Int {
        return every
    }

    private var id = 0
        private set

    override fun getID(): Int {
        return id
    }

    private var currentQuest: ToDoData? = null
    var name: TextView? = null
    var description: TextView? = null
    var date: TextView? = null
    var dellBtn: Button? = null
    var completeBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_view)
        init()
    }

    private fun init() {
        val arguments: Bundle? = getIntent().getExtras()
        every = arguments?.getInt(EVERYDAY)!!
        id = arguments.getInt(IDTASK)
        println("current id= " + id)
        name = findViewById(R.id.aiv_name_tack)
        description = findViewById(R.id.aiv_description_tack)
        date = findViewById(R.id.aiv_date)
        dellBtn = findViewById(R.id.aiv_btn_dell)
        completeBtn = findViewById(R.id.aiv_btn_complete)
        dellBtn!!.setOnClickListener {
            if (every == 1)
                presenter?.deleteEveryQuest()
            else
                presenter?.deleteQuest()
        }
        completeBtn!!.setOnClickListener { presenter?.completeQuest() }
        val dbHelper = DBHelper(this)
        val questModel = QuestModel(dbHelper)
        presenter = InfoQuestPresenter(questModel)
        presenter?.attachView(this)
        presenter?.viewIsReady()
    }

    override fun showQuest(quest: ToDoData?) {
        currentQuest = quest
        title = currentQuest?.name
        name?.text = quest?.name
        description?.text = quest?.description
        date?.text = quest?.day + "." + quest?.month + "." + quest?.year
    }

    override fun startOtherScreen(activity: Class<*>?) {
        if (activity == MainActivity::class.java) MainActivity.start(this)
        finish()
    }

    override fun getCurrentQuest(): ToDoData {
        return currentQuest!!
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachView()
    }

    companion object {
        const val EVERYDAY = "everyday"
        const val IDTASK = "idtask"
    }
}