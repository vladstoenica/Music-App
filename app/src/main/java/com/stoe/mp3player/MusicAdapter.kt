package com.stoe.mp3player

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

//Recycler Adapter
class MusicAdapter(
    var list: ArrayList<String>,
    var mContext: Context // echivalent in cod cu parent.context
    ) : RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewFileName: TextView = itemView.findViewById(R.id.textViewFileName)
        var cardView: CardView = itemView.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        return MusicViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_card_music, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val filePath: String = list.get(position)
        Log.e("path ", filePath)
        val title: String = filePath.substring(filePath.lastIndexOf("/") + 1)   //formatam numele fisierelor cum sa apara in recycler
        Log.e("title ", title)
        holder.textViewFileName.text = title
        holder.cardView.setOnClickListener{
            val intent: Intent = Intent(mContext, MusicActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("filepath", filePath)
            intent.putExtra("position", position)
            intent.putExtra("list", list)

            mContext.startActivity(intent)   //nu putem startActivity singur pt ca MusicAdapter nu are un activity
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}