package com.alex.willtrip.ui.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.alex.willtrip.R
import com.alex.willtrip.extensions.inflate
import com.alex.willtrip.ui.adapter.data.OpenAction
import kotlinx.android.synthetic.main.today_action.view.*

class TodayActionAdapter (val context: Context, val list: List<OpenAction>,
                          val listener: OnResultSelectedListener):
    RecyclerView.Adapter<TodayActionAdapter.TodayActionHolder>() {

    companion object {
        const val DONE = "done"
        const val NOT_DONE = "not_done"
        const val SKIPPED = "skipped"
        const val SPECIAL_DAY = "special_day"
    }

    interface OnResultSelectedListener {
        fun onResultSelected(action: OpenAction, resultSelected: String, note: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayActionHolder {
        val inflatedView = parent.inflate(R.layout.today_action, false)
        return TodayActionHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TodayActionHolder, position: Int) {
        holder.bindAction(list[position])
    }


    inner class TodayActionHolder (v: View) : RecyclerView.ViewHolder(v) {
        private var view = v

        fun bindAction(openAction: OpenAction){
            view.doNameTextView.text = openAction.name
            view.compWPTextView.text = openAction.complexity.toString()
            view.chainWPTextView.text = openAction.chainWP.toString()
            view.chainLengthTextView.text = context.getString(R.string.chain_length_days, openAction.chainLength)
            view.totalWPTextView.text = openAction.totalWP.toString()

            view.doneImageButton.setOnClickListener {
                listener.onResultSelected(openAction, DONE, view.noteEditText.text.toString())
            }

            view.failImageButton.setOnClickListener {
                listener.onResultSelected(openAction, NOT_DONE, view.noteEditText.text.toString())
            }

            view.skipButton.setOnClickListener {
                listener.onResultSelected(openAction, SKIPPED, view.noteEditText.text.toString())
            }

            when (openAction.isPositive) {
                true -> {
                    view.doneImageButton.setColorFilter(ContextCompat.getColor(context, R.color.colorGreen), android.graphics.PorterDuff.Mode.SRC_IN)
                    view.failImageButton.setColorFilter(ContextCompat.getColor(context, R.color.colorRed), android.graphics.PorterDuff.Mode.SRC_IN)
                }
                false -> {
                    view.doneImageButton.setColorFilter(ContextCompat.getColor(context, R.color.colorRed), android.graphics.PorterDuff.Mode.SRC_IN)
                    view.failImageButton.setColorFilter(ContextCompat.getColor(context, R.color.colorGreen), android.graphics.PorterDuff.Mode.SRC_IN)
                }
            }

            if (openAction.isObligatory) view.skipButton.visibility = View.GONE

            when (openAction.isSpecialDayEnabled) {
                true -> {
                    view.menuTextView.setOnClickListener {
                        val popupMenu = PopupMenu (context, view.menuTextView)
                        popupMenu.inflate(R.menu.today_action_items)
                        popupMenu.setOnMenuItemClickListener {
                            when (it.itemId) {
                                R.id.action_special_day -> {
                                    listener.onResultSelected(openAction, SPECIAL_DAY, view.noteEditText.text.toString())
                                    true
                                }
                                else -> false
                            }

                        }
                    }
                }
                false -> view.menuTextView.visibility = View.GONE
            }
        }
    }
}
