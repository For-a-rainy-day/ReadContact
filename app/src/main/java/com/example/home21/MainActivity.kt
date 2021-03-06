package com.example.home21

import Adapter.ItemClick
import Adapter.RvAdapter
import Helper.MyButton
import Helper.MySwipeHelper
import Model.Contact
import Utils.MyButtonClickListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.provider.ContactsContract
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

import com.github.florent37.runtimepermission.kotlin.askPermission
import kotlinx.android.synthetic.main.item_rv.*


class MainActivity : AppCompatActivity() {
    lateinit var adapter: RvAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var contactList: ArrayList<Contact>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv.setHasFixedSize(true)




        //add swipe
        val swipe = object : MySwipeHelper(this, rv, 120) {
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<MyButton>
            ) {
                //add button
                buffer.add(
                    MyButton(this@MainActivity,
                        "Sms",
                        30,
                        R.drawable.message,
                        Color.parseColor("#FFDD2371"),
                        object : MyButtonClickListener {
                            override fun onClick(position: Int) {
                                val intent = Intent(this@MainActivity, SmsActivity::class.java)
                                intent.putExtra("key", contactList[position])
                                startActivity(intent)
                            }
                        })
                )
                buffer.add(
                    MyButton(this@MainActivity,
                        "Call",
                        30,
                        R.drawable.call,
                        Color.parseColor("#FFF8CA2A"),
                        object : MyButtonClickListener {
                            override fun onClick(position: Int) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Update id $position",
                                    Toast.LENGTH_SHORT
                                ).show()
                                telefonQilish(position)
                            }
                        })
                )
            }

        }
        readContact()
    }

    private fun telefonQilish(position: Int) {

        askPermission(Manifest.permission.CALL_PHONE) {
            //all permissions already granted or just granted
            val phonNumber = contactList[position].number
            val intent = Intent(Intent(Intent.ACTION_CALL))
            intent.data = Uri.parse("tel:$phonNumber")
            startActivity(intent)
        }.onDeclined { e ->
            if (e.hasDenied()) {

                AlertDialog.Builder(this)
                    .setMessage("Ruxsat bermasangiz ilova ishlay olmaydi ruxsat bering...")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain();
                    } //ask again
                    .setNegativeButton("no") { dialog, which ->
                        dialog.dismiss();
                    }
                    .show();
            }

            if (e.hasForeverDenied()) {
                //the list of forever denied permissions, user has check 'never ask again'

                // you need to open setting manually if you really need it
                e.goToSettings();
            }
        }
    }

    fun readContact() {
        contactList = ArrayList()
        askPermission(Manifest.permission.READ_CONTACTS) {
            //all permissions already granted or just granted
            val contacts =
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        null,
                        null
                    )
                } else {
                    TODO("VERSION.SDK_INT < 0")
                }
            while (contacts!!.moveToNext()) {
                val contact = Contact(
                    contacts!!.getString(contacts!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                    contacts!!.getString(contacts!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                )
//            val rasmUrl = contacts!!.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))

                contactList.add(contact)
                contactList.sortBy { Contact -> Contact.name }
            }
            contacts!!.close()

            adapter = RvAdapter(this, contactList, object : ItemClick {
                override fun call(list: ArrayList<Contact>, position: Int, rvModel: Contact) {

                }
            })
            rv.adapter = adapter
        }.onDeclined { e ->
            if (e.hasDenied()) {

                AlertDialog.Builder(this)
                    .setMessage("Ruxsat bermasangiz ilova ishlay olmaydi ruxsat bering...")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain();
                    } //ask again
                    .setNegativeButton("no") { dialog, which ->
                        dialog.dismiss();
                    }
                    .show();
            }

            if (e.hasForeverDenied()) {
                //the list of forever denied permissions, user has check 'never ask again'

                // you need to open setting manually if you really need it
                e.goToSettings();
            }
        }

    }
}
