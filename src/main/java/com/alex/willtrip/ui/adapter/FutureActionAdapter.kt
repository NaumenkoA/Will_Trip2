package com.alex.willtrip.ui.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.alex.willtrip.R
import com.alex.willtrip.extensions.inflate
import com.alex.willtrip.ui.adapter.data.OpenAction
import kotlinx.android.synthetic.main.future_action.view.*

class FutureActionAdapter (val context: Context, val list: List<OpenAction>):
    RecyclerView.Adapter<FutureActionAdapter.FutureActionHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FutureActionHolder {
        val inflatedView = parent.inflate(R.layout.future_action, false)
        return FutureActionHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: FutureActionHolder, position: Int) {
        holder.bindAction(list[position])
    }


    inner class FutureActionHolder (v: View) : RecyclerView.ViewHolder(v) {
        private var view = v

        fun bindAction(openAction: OpenAction){
            view.doNameTextView.text = openAction.name
            when (openAction.isPositive) {
                true -> view.doNameTextView.setTextColor(ContextCompat.getColor(context, R.color.colorGreen))
                false -> view.doNameTextView.setTextColor(ContextCompat.getColor(context, R.color.colorLightRed))
            }

            view.compWPTextView.text = openAction.complexity.toString()
            view.chainWPTextView.text = openAction.chainWP.toString()
            view.chainLengthTextView.text = context.getString(R.string.chain_length_days, openAction.chainLength)
            view.totalWPTextView.text = openAction.totalWP.toString()
            view.obligatoryTextView.text = when (openAction.isObligatory) {
                true -> context.getString(R.string.yes)
                false -> context.getString(R.string.no)
            }
        }
    }
}
