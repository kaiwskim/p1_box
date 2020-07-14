package com.example.gallery

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.gallery.R.layout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.first_fragment.*
import kotlinx.android.synthetic.main.main_activity.*

class FirstFragment() : Fragment(), Parcelable {
    constructor(parcel: Parcel) : this() {
    }

    fun newInstance(): FirstFragment{
        val args = Bundle()
        val frag = FirstFragment()
        frag.arguments = args

        return frag
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layout.first_fragment,container,false)
        val view1 = layoutInflater.inflate(R.layout.addcontact_layout,null)
        var list_open = view.findViewById<ListView>(R.id.listView)
        var button_add = view.findViewById<Button>(R.id.btn_addContact)
        var button_addcontact = view.findViewById<Button>(R.id.btn_add)
        var btn_addcontact = view1.findViewById<Button>(R.id.btn_add)
        val bottomSheetDialog = BottomSheetDialog(context!!)

        bottomSheetDialog.setContentView(view1)
        button_add.setOnClickListener {
            bottomSheetDialog.show()
        }


        fun read() {
            var cursor : Cursor? = getActivity()?.contentResolver?.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null)
            getActivity()?.startManagingCursor(cursor)
            var from = arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone._ID)
            var to = intArrayOf(android.R.id.text1,android.R.id.text2)
            var simple : SimpleCursorAdapter = SimpleCursorAdapter(activity,android.R.layout.simple_list_item_2,cursor,from,to)
//            getActivity()?.list_open?.adapter = simple
            list_open.adapter = simple
        }

        read()

        fun addContact(view1 : View) {
            val etContactFirstName: EditText = view1.findViewById(R.id.AddFirstName)
            val etContactFamilyName: EditText = view1.findViewById(R.id.AddFamilyName)
            val etContactNumber: EditText = view1.findViewById(R.id.AddContact)
            val firstname: String = etContactFirstName.text.toString()
            val familyname: String = etContactFamilyName.text.toString()
            val phone = etContactNumber.text.toString()
            val intent = Intent(ContactsContract.Intents.Insert.ACTION)
            intent.setType(ContactsContract.RawContacts.CONTENT_TYPE)
            intent.putExtra(ContactsContract.Intents.Insert.NAME, firstname+" "+familyname)
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone)
            startActivityForResult(intent, 1)
        }

//        fun onActivityResult(
//            requestCode: Int,
//            resultCode: Int,
//            intent: Intent?
//        ) {
//            super.onActivityResult(requestCode, resultCode, intent)
//            if (requestCode == 1) {
//                if (resultCode == Activity.RESULT_OK) {
////                    Toast.makeText(this, "Added Contact", Toast.LENGTH_SHORT).show()
//                    System.out.println("OK")
//                }
//                if (resultCode == Activity.RESULT_CANCELED) {
////                    Toast.makeText(this, "Cancelled Added Contact", Toast.LENGTH_SHORT).show()
//                    System.out.println("CANCEL")
//                }
//            }
//        }

        btn_addcontact.setOnClickListener {
            addContact(view1)
            bottomSheetDialog.dismiss()
//            onActivityResult()
        }

        return view
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FirstFragment> {
        override fun createFromParcel(parcel: Parcel): FirstFragment {
            return FirstFragment(parcel)
        }

        override fun newArray(size: Int): Array<FirstFragment?> {
            return arrayOfNulls(size)
        }
    }
}