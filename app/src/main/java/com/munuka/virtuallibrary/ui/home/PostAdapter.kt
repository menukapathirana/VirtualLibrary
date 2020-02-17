package com.munuka.virtuallibrary.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.munuka.virtuallibrary.DisplayBook
import com.munuka.virtuallibrary.R
import com.squareup.picasso.Picasso

class PostAdapter(val posts: ArrayList<String>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    override fun getItemCount() = posts.size



    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        Picasso.get().load(posts[position]).fit().placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.imageview)

        holder.imageview.setOnClickListener {


            //Log.v("Menuka",posts[position].toString())
            val intent = Intent(holder.itemView.context, DisplayBook::class.java)
            intent.putExtra("imageURL",posts[position].toString())
            holder.itemView.context.startActivity(intent)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.row_post,parent,false)
        return ViewHolder(view)
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        //card textview
        var imageview=itemView.findViewById(R.id.BookView) as ImageView

       /* var imgageid = itemView.findViewById(R.id.imgTitle) as TextView*/

    }




}
