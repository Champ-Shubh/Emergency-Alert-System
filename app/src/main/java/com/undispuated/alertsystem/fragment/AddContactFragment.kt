package com.undispuated.alertsystem.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.room.Room
import com.google.android.material.textfield.TextInputLayout
import com.undispuated.alertsystem.R
import com.undispuated.alertsystem.database.ContactsDatabase
import com.undispuated.alertsystem.database.ContactsEntity
import com.undispuated.alertsystem.model.Contact

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddContactFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddContactFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var etContactName: TextInputLayout
    lateinit var etContactNickname: TextInputLayout
    lateinit var etContactPhone: TextInputLayout
    lateinit var radioGender: RadioGroup
    lateinit var radioButton: RadioButton
    lateinit var btnAdd: Button
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_contact, container, false)
        sharedPreferences = activity?.getSharedPreferences(getString(R.string.alert_system_sharedPref), Context.MODE_PRIVATE)!!

        etContactName = view.findViewById(R.id.etContactName)
        etContactNickname = view.findViewById(R.id.etContactNickname)
        etContactPhone = view.findViewById(R.id.etContactPhone)
        radioGender = view.findViewById(R.id.radioGender)
        btnAdd = view.findViewById(R.id.btnAdd)

        btnAdd.setOnClickListener {
            val selectedID = radioGender.checkedRadioButtonId
            radioButton = view.findViewById(selectedID)

            val gender = radioButton.text.toString()
            val curr_cnt = sharedPreferences.getInt("contacts_count", 0)

            val contactEntity = ContactsEntity(
                curr_cnt+1,
                gender == "Male",
                etContactName.editText.toString(),
                etContactNickname.editText.toString(),
                etContactPhone.editText.toString().toInt()
            )
            sharedPreferences.edit().remove("contacts_count").apply()
            sharedPreferences.edit().putInt("contacts_count", curr_cnt+1).apply()

            val result = DBAsyncTask(activity as Context, contactEntity).execute().get()

            if(result != null){
                activity!!.supportFragmentManager.popBackStackImmediate()
            }
            else{
                Toast.makeText(context,"Operation failed !!", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    class DBAsyncTask(context: Context, val contactsEntity: ContactsEntity): AsyncTask<Void, Void, Boolean>(){
        private val db = Room.databaseBuilder(context, ContactsDatabase::class.java, "contacts-db").build()

        override fun doInBackground(vararg p0: Void?): Boolean {
            db.contactsDao().insertContact(contactsEntity)

            db.close()
            return true
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddContactFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddContactFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}