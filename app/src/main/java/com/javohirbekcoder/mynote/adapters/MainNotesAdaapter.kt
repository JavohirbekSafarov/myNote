package com.javohirbekcoder.mynote.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.javohirbekcoder.mynote.R
import com.javohirbekcoder.mynote.databinding.RecyclerItemBinding
import com.javohirbekcoder.mynote.models.MainRecyclerModel

/*
Created by Javohirbek on 16.04.2023 at 18:06
*/
class MainNotesAdapter(val mContext: Context,val itemsList: ArrayList<MainRecyclerModel>) : RecyclerView.Adapter<MainNotesAdapter.MyViewHolder>() {

    private val color  = arrayListOf(
       R.drawable.color_green,
        R.drawable.color_pink,
        R.drawable.color_yellow,
        R.drawable.color_blue
    )

    inner class MyViewHolder(val binding: RecyclerItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun onBind(mainRecyclerModel: MainRecyclerModel) {
            with(binding) {
                val drawable = ContextCompat.getDrawable(mContext, color[mainRecyclerModel.colorIndex])
                colorImage.setImageDrawable(drawable)
                rvItemTitle.text = mainRecyclerModel.title
                rvItemNote.text = mainRecyclerModel.note
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(itemsList[position])
    }
}