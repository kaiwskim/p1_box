package com.example.gallery

import android.app.Dialog
import android.content.Context.TELEPHONY_SERVICE
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.third_fragment.*
import com.twilio.Twilio
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.database.core.Context
import com.twilio.Twilio.init
import com.twilio.http.TwilioRestClient


class ThirdFragment() : Fragment(), Parcelable {
    constructor(parcel: Parcel) : this() {
    }

    fun newInstance(): ThirdFragment{
        val args = Bundle()
        val frag = ThirdFragment()
        frag.arguments = args

        return frag
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.third_fragment, container, false)
        val view1 = layoutInflater.inflate(R.layout.more_layout,null)
        val view2 = layoutInflater.inflate(R.layout.dictionary_layout,null)
        val view3 = layoutInflater.inflate(R.layout.sendmessage_layout,null)

        val btn_dotline = view.findViewById<Button>(R.id.morse_dotline)
        val btn_space = view.findViewById<Button>(R.id.morse_space)
        val btn_backspace = view.findViewById<Button>(R.id.morse_backspace)
        val btn_clear = view.findViewById<Button>(R.id.morse_clear)
        val btn_translate = view.findViewById<Button>(R.id.morse_translate)
        var btn_more = view.findViewById<Button>(R.id.morse_more)
        var btn_dictionary = view1.findViewById<LinearLayout>(R.id.btn_morseDictionary)
        var btn_sendmessage = view1.findViewById<LinearLayout>(R.id.btn_sendMessage)
//        var btn_sendmorsecode = view3.findViewById<Button>(R.id.btn_sendmorsecode)

        val dictionaryDialog = Dialog(context!!)
        val bottomSheetDialog = BottomSheetDialog(context!!)
        val bottomSheetDialog1 = BottomSheetDialog(context!!)

        bottomSheetDialog.setContentView(view1)
        btn_more.setOnClickListener {
            bottomSheetDialog.show()
        }

        bottomSheetDialog1.setContentView(view3)
        btn_sendmessage.setOnClickListener {
            bottomSheetDialog1.show()
        }

        val dictionary = HashMap<String, String>()


        fun appendOnExpression(string: String){
            tvInput.append(string)
        }
        fun appendOnResult(string: String){
            tvOutput.append(string)
        }

        btn_dotline.setOnClickListener {
            appendOnExpression("·")}

        btn_dotline.setOnLongClickListener {
            appendOnExpression("-")
            true}

        btn_space.setOnClickListener {appendOnExpression(" ")}

        btn_space.setOnLongClickListener {
            appendOnExpression("   ")
            true}

        btn_backspace.setOnClickListener {
            val string = tvInput.text.toString()
            if(string.isNotEmpty()) {
                tvInput.text = string.substring(0,string.length-1)
            }
        }

        btn_clear.setOnClickListener {
            tvInput.text=""
            tvOutput.text=""
        }

        btn_dictionary.setOnClickListener{
            dictionaryDialog.setContentView(view2)
            bottomSheetDialog.dismiss()
            dictionaryDialog.show()
        }





        fun morseToEnglish() {
            dictionary.put(" ", " ")

            dictionary.put("·-", "A")
            dictionary.put("-···", "B")
            dictionary.put("-·-·", "C")
            dictionary.put("-··", "D")
            dictionary.put("·", "E")
            dictionary.put("··-·", "F")
            dictionary.put("--·", "G")
            dictionary.put("····", "H")
            dictionary.put("··", "I")
            dictionary.put("·---", "J")
            dictionary.put("-·-", "K")
            dictionary.put("·-··", "L")
            dictionary.put("--", "M")
            dictionary.put("-·", "N")
            dictionary.put("---", "O")
            dictionary.put("·--·", "P")
            dictionary.put("--·-", "Q")
            dictionary.put("·-·", "R")
            dictionary.put("···", "S")
            dictionary.put("-", "T")
            dictionary.put("··-", "U")
            dictionary.put("···-", "V")
            dictionary.put("·--", "W")
            dictionary.put("-··-", "X")
            dictionary.put("-·--", "Y")
            dictionary.put("--··", "Z")

            dictionary.put("·----", "1")
            dictionary.put("··---", "2")
            dictionary.put("···--", "3")
            dictionary.put("····-", "4")
            dictionary.put("·····", "5")
            dictionary.put("-····", "6")
            dictionary.put("--···", "7")
            dictionary.put("---··", "8")
            dictionary.put("----·", "9")
            dictionary.put("-----", "0")
        }

        fun translate(string: String): String {
            morseToEnglish()
            var builder: StringBuilder? = null
            var words: List<String> = string.trim().split("   ")
            var letter: String = ""
            var word: String

            for (word in words){
//                System.out.println(word)
                for (letter in word.split(" ")){
                    var alpha: String? = dictionary.get(letter)
//                    System.out.println(letter)
//                    System.out.println(alpha)
                    if (alpha != null) {
                        appendOnResult(alpha)
                    }
                }
                appendOnResult(" ")
            }
            return tvOutput.toString()
        }



        btn_translate.setOnClickListener {
            tvOutput.text=""
            val str: String = tvInput.text.toString()
            var convertedTxt: String?= null
            convertedTxt = translate(str)

//            if(tvOutput.text == null) {
//                Toast.makeText(context, "Insert valid code", Toast.LENGTH_LONG).show()
//            }
        }


        val send = view3.findViewById<Button>(R.id.btn_sendmorsecode)
        val num = view3.findViewById<EditText>(R.id.EditContact)
//val mess = view.findViewById<EditText>(R.id.mess)

        fun sendSMS(phoneNumber: String, message: String){
            val sms: SmsManager = SmsManager.getDefault()
            sms.sendTextMessage(phoneNumber, null, message, null, null)
        }

        send.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View) {
                val inputText = num.text.toString()
                val morseCode = tvInput.text.toString()
                val translated = tvOutput.text.toString()
                sendSMS(inputText, morseCode+"\n"+translated)
                num.text = null
                bottomSheetDialog.dismiss()
                bottomSheetDialog1.dismiss()
            }
        })

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