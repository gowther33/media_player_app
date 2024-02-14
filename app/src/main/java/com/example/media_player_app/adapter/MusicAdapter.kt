package com.example.media_player_app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.media_player_app.PlayerActivity
import com.example.media_player_app.databinding.MusicItemViewBinding
import com.example.media_player_app.exoplayer.MusicPlayer
import com.example.media_player_app.model.Song

class MusicAdapter(
    val context: Context
) : RecyclerView.Adapter<MusicAdapter.MusicItemViewHolder>(){


    inner class MusicItemViewHolder(binding: MusicItemViewBinding):RecyclerView.ViewHolder(binding.root) {
        private val title = binding.tvTitle
        private val artist = binding.tvArtist
        private val layout = binding.songLayout

        fun bind(item: Song){
            title.text = item.name
            artist.text = item.artist


            layout.setOnClickListener {
                MusicPlayer.startPlaying(context, item)
                context.startActivity(Intent(context, PlayerActivity::class.java))
            }
        }
    }


    private val diffCallback = object: DiffUtil.ItemCallback<Song>(){
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private var differ = AsyncListDiffer(this, diffCallback)

    var songList:List<Song>
        get() = differ.currentList
        set(value) = differ.submitList(value)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicItemViewHolder {
        return MusicItemViewHolder(
            MusicItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MusicItemViewHolder, position: Int) {
        val item = songList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return songList.size
    }
}