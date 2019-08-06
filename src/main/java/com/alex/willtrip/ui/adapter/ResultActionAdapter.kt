package com.alex.willtrip.ui.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.alex.willtrip.R
import com.alex.willtrip.extensions.inflate
import com.alex.willtrip.ui.adapter.data.ResultAction
import kotlinx.android.synthetic.main.future_action.view.doNameTextView
import kotlinx.android.synthetic.main.result_action.view.*


class ResultActionAdapter (val context: Context, val list: List<ResultAction>, val isUndoEnabled: Boolean,
                           val listener: ResultOnClickListener? = null):
    RecyclerView.Adapter<ResultActionAdapter.ResultActionHolder>() {

    interface ResultOnClickListener {
        fun onUndoButtonClicked (id: Long)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultActionHolder {
        val inflatedView = parent.inflate(R.layout.result_action, false)
        return ResultActionHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ResultActionHolder, position: Int) {
        holder.bindAction(list[position])
    }

    inner class ResultActionHolder (v: View) : RecyclerView.ViewHolder(v) {
        private var view = v

        fun bindAction(resultAction: ResultAction){
            view.doNameTextView.text = resultAction.name
            view.compWPTextView.text = resultAction.complexity.toString()
            view.chainWPTextView.text = resultAction.chainWP.toString()
            view.noteTextView.text = resultAction.note

            when (resultAction.type) {
                ResultAction.SUCCESS -> {
                    view.resultTextView.text = context.getString(R.string.success)
                    view.resultTextView.setTextColor(ContextCompat.getColor(context, R.color.colorGreen))
                    view.totalWPTextView.text = context.getString(R.string.wp_result_positive, resultAction.totalWP)
                    view.totalWPTextView.background = ContextCompat.getDrawable(context, R.drawable.text_circle_green)
                }

                ResultAction.FAIL -> {
                    view.resultTextView.text = context.getString(R.string.fail)
                    view.resultTextView.setTextColor(ContextCompat.getColor(context, R.color.colorLightRed))
                    view.totalWPTextView.text = context.getString(R.string.wp_result_negative, resultAction.totalWP)
                    view.totalWPTextView.background = ContextCompat.getDrawable(context, R.drawable.text_circle_red)
                }

                ResultAction.SKIPPED -> {
                    view.resultTextView.text = context.getString(R.string.skipped)
                    view.resultTextView.setTextColor(ContextCompat.getColor(context, R.color.colorGrey))
                    view.totalWPTextView.text = context.getString(R.string.wp_result_neutral, resultAction.totalWP)
                    view.totalWPTextView.background = ContextCompat.getDrawable(context, R.drawable.text_circle_grey)
                }

                ResultAction.SPECIAL_DAY -> {
                    view.resultTextView.text = context.getString(R.string.special_day)
                    view.resultTextView.setTextColor(ContextCompat.getColor(context, R.color.colorGrey))
                    view.totalWPTextView.text = context.getString(R.string.wp_result_neutral, resultAction.totalWP)
                    view.totalWPTextView.background = ContextCompat.getDrawable(context, R.drawable.text_circle_grey)
                }
            }

            if (isUndoEnabled) {
                view.undoButton.setOnClickListener {
                    listener?.onUndoButtonClicked(resultAction.doId)
                }
            } else {
                view.undoButton.visibility = View.GONE
            }
        }
    }
}
