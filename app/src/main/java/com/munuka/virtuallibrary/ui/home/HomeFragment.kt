package com.munuka.virtuallibrary.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.munuka.virtuallibrary.Book
import com.munuka.virtuallibrary.MyListActivity
import com.munuka.virtuallibrary.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    lateinit var bookList:MutableList<Book>




    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        /*val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(this, Observer {
            textView.text = it
        })*/

       val recyView: RecyclerView = root.findViewById(R.id.recyclerView)
        recyView.layoutManager=LinearLayoutManager(context,OrientationHelper.HORIZONTAL,false)


        val recyViewTrend: RecyclerView = root.findViewById(R.id.recyclerTrending)
        recyViewTrend.layoutManager=LinearLayoutManager(context,OrientationHelper.HORIZONTAL,false)


        val btnMyList : TextView = root.findViewById(R.id.btn_myList)

        btnMyList.setOnClickListener {
            val i = Intent(activity, MyListActivity::class.java)
            startActivity(i)
            (activity as Activity). overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }



        val posts:ArrayList<String> = ArrayList()




        bookList = mutableListOf()
       val ref = FirebaseDatabase.getInstance().getReference("Book")

        //Get the current date
        val currentDate = Date();
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        val answer: String = formatter.format(currentDate)

        //Get the date of last 7 seven days
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -7)

        val recentDate : String = formatter.format(calendar.time)

        val query = ref.orderByChild("date").startAt(recentDate)



        query.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0!!.exists())
                {
                    for (va in p0.children)
                    {
                           val book = va.getValue(Book::class.java)
                           bookList.add(book!!)
                    }
                    for(i in bookList){
                        posts.add(i.image)
                    }
                    recyView.adapter = PostAdapter(posts)

                    recyViewTrend.adapter = PostAdapter(posts)


                }
            }

        })




        return root
    }
}