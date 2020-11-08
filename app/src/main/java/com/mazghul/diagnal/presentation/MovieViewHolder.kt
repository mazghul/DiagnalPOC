package com.mazghul.diagnal.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mazghul.diagnal.R
import com.mazghul.diagnal.data.model.Content


class MovieViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.movie_item, parent, false)) {
    private var movie_image: ImageView? = null
    private var movie_name: TextView? = null

    init {
        movie_image = itemView.findViewById(R.id.movie_image)
        movie_name = itemView.findViewById(R.id.movie_name)
    }

    fun bind(
        content: Content
    ) {
        movie_image!!.let {
//            movie_image?.setImageResource(
//
//                movie_image!!.context.resources.getIdentifier(
//                    content.poserImage.dropLast(4), "drawable", movie_image!!.context.packageName
//                )
//            )
            Glide.with(movie_image!!.context)
                .load(movie_image!!.context.getResources().getIdentifier(content.poserImage.dropLast(4), "drawable", movie_image!!.context.getPackageName()))
                .placeholder(R.drawable.placeholder_for_missing_posters)
                .error(R.drawable.placeholder_for_missing_posters)
                .into(movie_image!!)
        }


        movie_name!!.text = content.name
    }

}