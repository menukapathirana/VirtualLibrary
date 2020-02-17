package com.munuka.virtuallibrary

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.munuka.virtuallibrary.ui.home.PostAdapter

class MyListActivity : AppCompatActivity() {

    lateinit var bookList:MutableList<Book>
    private var mAuth: FirebaseAuth? = null

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_list)


        val posts:ArrayList<String> = ArrayList()

        val mybookList: RecyclerView = findViewById(R.id.mybookList)
        mybookList.layoutManager= LinearLayoutManager(applicationContext, OrientationHelper.VERTICAL,false)


        bookList = mutableListOf()
        val ref = FirebaseDatabase.getInstance().getReference("Book")

        mAuth = FirebaseAuth.getInstance()
        val userId = mAuth!!.currentUser!!.uid
        val query = ref.orderByChild("addedby").equalTo(userId)



        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0!!.exists()) {
                    for (va in p0.children) {
                        val book = va.getValue(Book::class.java)
                        bookList.add(book!!)
                    }
                    for (i in bookList) {
                        posts.add(i.image)
                    }
                    mybookList.adapter = PostAdapter(posts)
                }
            }
        })

    }
}
