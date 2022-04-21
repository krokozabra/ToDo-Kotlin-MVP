package com.kondaurovigor.diary5000.view

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.kondaurovigor.diary5000.R
import com.kondaurovigor.diary5000.common.ToDoData
import com.kondaurovigor.diary5000.database.DBHelper
import com.kondaurovigor.diary5000.interfaces.AddQuestInterface
import com.kondaurovigor.diary5000.model.QuestModel
import com.kondaurovigor.diary5000.presenter.AddQuestPresenter
import java.util.*


class AddQuestView : AppCompatActivity(), AddQuestInterface {
    private var presenter: AddQuestPresenter? = null
    var addQuestBTN: Button? = null
    var nameET: EditText? = null
    var descriptionET: EditText? = null
    var dateET: EditText? = null
    var everyDayCB: CheckBox? = null
    var dateExec = Calendar.getInstance()
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_quest_view)
        init()
    }

    private fun init() {
        addQuestBTN = findViewById(R.id.aaqv_btn_add)
        nameET = findViewById(R.id.aaqv_et_name)
        descriptionET = findViewById(R.id.aaqv_et_decription)
        dateET = findViewById(R.id.aaqv_et_date)
        everyDayCB = findViewById(R.id.aaqv_cb_every_day)
        addQuestBTN!!.setOnClickListener {
            //метод презентера
            presenter?.addNewQuest()
        }
        everyDayCB!!.setOnClickListener {
            if (everyDayCB!!.isChecked) //скорее всего сюда надо отправить ещё один метод презентера и там отмечать флаг
                dateET?.setVisibility(View.INVISIBLE) else  //скорее всего сюда надо отправить ещё один метод презентера и там отмечать флаг
                dateET?.setVisibility(View.VISIBLE)
        }
        dateET?.setOnFocusChangeListener { _: View?, hasFocus: Boolean ->
            if (hasFocus) {
                presenter?.clickDate()
            }
        }
        dateET?.setOnClickListener({ presenter?.clickDate() })
        val dbHelper = DBHelper(this)
        val questModel = QuestModel(dbHelper)
        presenter = AddQuestPresenter(questModel)
        presenter?.attachView(this)
        presenter?.viewIsReady()
    }

    override fun getNewQuest(): ToDoData{
            val newQuest = ToDoData()
            newQuest.name = nameET?.text.toString()
            newQuest.description = descriptionET?.text.toString()
            newQuest.everyday = if (everyDayCB!!.isChecked) 1 else 0
            newQuest.day = dateExec[Calendar.DAY_OF_MONTH].toString()
            newQuest.month = dateExec[Calendar.MONTH].toString()
            newQuest.year = dateExec[Calendar.YEAR].toString()
            return newQuest
        }

    override fun startOtherScreen(activity: Class<*>?) {
        if (activity == MainActivity::class.java) MainActivity.Companion.start(this)
        finish()
    }

    // отображаем диалоговое окно для выбора даты
    override fun setDate() {
        DatePickerDialog(
            this, d,
            dateExec[Calendar.YEAR],
            dateExec[Calendar.MONTH],
            dateExec[Calendar.DAY_OF_MONTH]
        )
            .show()
    }

    // вывод на экран даты
    override fun showDate() {
        dateET?.setText(
            DateUtils.formatDateTime(
                this,
                dateExec.timeInMillis,
                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR
            )
        )
    }

    // установка обработчика выбора даты
    var d: DatePickerDialog.OnDateSetListener =
        DatePickerDialog.OnDateSetListener { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            dateExec[Calendar.YEAR] = year
            dateExec[Calendar.MONTH] = monthOfYear
            dateExec[Calendar.DAY_OF_MONTH] = dayOfMonth
            showDate()
        }

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, AddQuestView::class.java)
            activity.startActivity(intent)
        }
    }
}