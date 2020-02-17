package com.munuka.virtuallibrary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_display_book.*


class DisplayBook : AppCompatActivity() {

    lateinit var bookList:MutableList<Book>

    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_book)

        val ImgURL: String = intent.getStringExtra("imageURL")
        Picasso.get().load(ImgURL).fit().placeholder(R.drawable.ic_launcher_foreground)
            .into(bk_image)

        bookList = mutableListOf()
        val ref = FirebaseDatabase.getInstance().getReference("Book")

        val query = ref.orderByChild("image").equalTo(ImgURL)

        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(img: DatabaseError) {
            }

            override fun onDataChange(img: DataSnapshot) {
                if (img!!.exists()) {
                    for (va in img.children) {
                        val book = va.getValue(Book::class.java)
                        bookList.add(book!!)
                    }
                    for (i in bookList) {
                        bk_titile.text = i.title
                        bk_datencate.text = i.date
                        bk_author.text=i.author
                        bk_lang.text=i.langugage

                    }
                }
            }

        })



        //Get the added by user's name
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        mAuth = FirebaseAuth.getInstance()
        val mUser = mAuth!!.currentUser
        val mUserReference = mDatabaseReference!!.child(mUser!!.uid)

        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bk_addedby!!.text = snapshot.child("firstName").value as String

            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })


        btn_request_lend.setOnClickListener {

        }

    }


}
