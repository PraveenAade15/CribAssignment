package com.example.crib

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Telephony
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var list: ListView
    var arrayList = ArrayList<String>()

    lateinit var phoneNumber: EditText
    lateinit var NumberOfdays: EditText
    lateinit var submit: Button
    lateinit var text: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        phoneNumber = findViewById(R.id.etPhoneNumebr)
        NumberOfdays = findViewById(R.id.etNumberOfDay)
        submit = findViewById(R.id.btnSubmit)
        text = findViewById(R.id.tvShow)

        list = findViewById<ListView>(R.id.list)
        if (ContextCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.READ_SMS), 0)

        }
        GlobalScope.launch {
            submit.setOnClickListener {
                arrayList.clear()
                val PNumber = phoneNumber.text.toString()
                val Days = NumberOfdays.text.toString()
                if (PNumber.isEmpty() || Days.isEmpty()) {
                    arrayList.clear()
                    text.text = "Sorry,no messages found"
                }
                else if (PNumber.isNotEmpty() && Days.isNotEmpty()) {
                    val uri = Uri.parse("content://sms/inbox")
                    val cursor = contentResolver.query(uri, null, null, null, null)
                    while (cursor?.moveToNext() == true) {
                        val number = cursor.getString(2)
                        val message = cursor.getString(12)
                        val smsDate: String = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE))
                        val dateFormat = Date(java.lang.Long.valueOf(smsDate))
                        val formatDate: String = SimpleDateFormat("dd-MM-yyyy").format(dateFormat)

                        if (number.contains(PNumber)) {
                            arrayList.add("$number \n $message \n $formatDate")
                        }
                    }
                    if (arrayList.size > 0){
                        text.text = "${arrayList.size.toString()} messages found"

                    }
                    else {
                        arrayList.clear()
                        text.text = "Sorry,no messages found"
                    }
                }


            }
        }

            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList)
            list.adapter = adapter

        }
    }

