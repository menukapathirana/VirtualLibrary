package com.munuka.virtuallibrary

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_add_book.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddBook : AppCompatActivity(){

    //Firebase references
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    //Image Upload
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    private var downloadUri: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        val adapter = ArrayAdapter.createFromResource(this,
            R.array.book_category, R.layout.spinner_text_color)
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_text_color)
        // Apply the adapter to the spinner
        spinner!!.adapter = adapter




        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                Toast.makeText(
                    this@AddBook,
                    parent.getItemAtPosition(position).toString(),
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }



        //FirebaseStorage
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        //FirebaseDatabse
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Book")
        mAuth = FirebaseAuth.getInstance()



        btn_AddBook.setOnClickListener {
            uploadImage()


        }

        btn_choose_image?.setOnClickListener {
            //Launch the phone gallery
            launchGallery()
        }



    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    //Set selected image to the image button
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                btn_choose_image.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    //Upload image to the firebase storage and insert book details to the database
    private fun uploadImage(){
        if(filePath != null){
            //create upload image path by isbn10 and book title
            val ref = storageReference?.child("uploads/" + bk_isbn10?.text.toString() + "-" + bk_name?.text.toString())
            val uploadTask = ref?.putFile(filePath!!)

            val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { task ->
                if (task.isSuccessful) {9
                    //get image url path from firebase storage
                    downloadUri = task.result?.toString()
                    val userId = mAuth!!.currentUser!!.uid
                    if (!TextUtils.isEmpty(bk_isbn10?.text.toString()) && !TextUtils.isEmpty(bk_name?.text.toString()) &&
                        !TextUtils.isEmpty(bk_author?.text.toString()) && !TextUtils.isEmpty(bk_lang?.text.toString()) &&
                        !TextUtils.isEmpty(bk_isbn13?.text.toString()) && !TextUtils.isEmpty(userId) && !TextUtils.isEmpty(downloadUri))
                    {
                        val currentUserDb = mDatabaseReference!!.child(bk_isbn10?.text.toString())
                        //currentUserDb.child("isbn10").setValue(bk_isbn10?.text.toString())
                        currentUserDb.child("title").setValue(bk_name?.text.toString())
                        currentUserDb.child("author").setValue(bk_author?.text.toString())
                        currentUserDb.child("langugage").setValue(bk_lang?.text.toString())
                        currentUserDb.child("isbn13").setValue(bk_isbn13?.text.toString())
                        currentUserDb.child("image").setValue(downloadUri)
                        currentUserDb.child("addedby").setValue(userId)

                        //set data format
                        val currentDate = Date();
                        val formatter = SimpleDateFormat("dd-MM-yyyy")
                        val answer: String = formatter.format(currentDate)

                        currentUserDb.child("date").setValue(answer)
                        //refresh the add book user interface
                        updateBookInfoAndUI()

                    }
                    else {
                        Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    // Handle failures
                }
            }?.addOnFailureListener{

            }
        }else{
            Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }


    private fun updateBookInfoAndUI() {
        val intent = Intent(this@AddBook, AddBook::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }





}
