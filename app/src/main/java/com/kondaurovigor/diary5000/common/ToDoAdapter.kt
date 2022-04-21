package com.kondaurovigor.diary5000.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kondaurovigor.diary5000.R
import com.kondaurovigor.diary5000.database.DBHelper
import com.kondaurovigor.diary5000.model.QuestModel
import com.kondaurovigor.diary5000.view.InfoView

class ToDoAdapter() : BaseAdapter() {

    var ctx: Context? = null
    var lInflater: LayoutInflater? = null
    var toDos: ArrayList<ToDoData>? = null
    var name: TextView? = null
    var flagTack: CheckBox? = null
    var spase: RelativeLayout? = null
    var dbHelper: DBHelper? = null

    constructor(context: Context?, toDos: ArrayList<ToDoData>?) : this() {
        this.ctx = context
        this.toDos = toDos
        lInflater = ctx?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        dbHelper = DBHelper(context)
    }

    override fun getCount(): Int {
        return toDos!!.size
    }

    override fun getItem(position: Int): ToDoData {
        return toDos!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("CutPasteId")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        // используем созданные, но не используемые view
        var view: View? = convertView
        if (view == null) {
            view = lInflater?.inflate(R.layout.item_task, parent, false)
        }

        val p = getItem(position)
        // заполняем View в пункте списка данными из истории
        name = view!!.findViewById(R.id.it_name)
        name?.setText(p.name)
        spase = view.findViewById(R.id.it_space)
        spase?.setOnClickListener {
            //переход на экран описание таска
            val intent = Intent(ctx, InfoView::class.java)
            intent.putExtra(InfoView.EVERYDAY, p.everyday)
            intent.putExtra(InfoView.IDTASK, p.id)
            ctx!!.startActivity(intent)
        }

        flagTack = view.findViewById(R.id.it_check)
        if (p.everyday == 0) {
            val finalView: View = view
            val questModel = dbHelper?.let { QuestModel(it) }
            flagTack?.setOnClickListener {
                //окрашивается при нажатии на чекбокс
                if (p.ok== 0) {
                    p.ok = 1
                    questModel?.completeQuest(p)
                    spase = finalView.findViewById(R.id.it_space)
                    spase?.setBackgroundColor(ContextCompat.getColor(ctx!!, R.color.green))
                } else {
                    p.ok = 0
                    questModel?.completeQuest(p)
                    spase = finalView.findViewById(R.id.it_space)
                    spase?.setBackgroundColor(ContextCompat.getColor(ctx!!, R.color.white))
                }
            }
        } else {
            flagTack?.setVisibility(View.GONE)
        }
        //для изначального окрашивания
        if (p.ok == 1) {
            (view.findViewById<View>(R.id.it_check) as CheckBox).isChecked =
                true
            spase?.setBackgroundColor(ContextCompat.getColor(ctx!!, R.color.green))
        } else {
            (view.findViewById<View>(R.id.it_check) as CheckBox).isChecked =
                false
            spase?.setBackgroundColor(ContextCompat.getColor(ctx!!, R.color.white))
        }

        return view
    }
}