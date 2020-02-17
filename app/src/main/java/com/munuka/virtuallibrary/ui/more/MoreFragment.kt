package com.munuka.virtuallibrary.ui.more

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.munuka.virtuallibrary.AddBook
import com.munuka.virtuallibrary.R

class MoreFragment: Fragment(){
    private lateinit var moreViewModel: MoreViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        moreViewModel =
            ViewModelProviders.of(this).get(MoreViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_more, container, false)


        val btnBookView:Button = root.findViewById(R.id.btn_AddBookView)



        btnBookView!!.setOnClickListener {
            val AddBookIntent = Intent(context,AddBook::class.java)
            startActivity(AddBookIntent) }
        return root
    }
}