package com.undispuated.alertsystem.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.undispuated.alertsystem.R
import com.undispuated.alertsystem.adapter.ContactsAdapter
import com.undispuated.alertsystem.database.ContactsDatabase
import com.undispuated.alertsystem.database.ContactsEntity
import com.undispuated.alertsystem.model.User

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    lateinit var txtUsernameText: TextView
    lateinit var txtMobileText: TextView
    lateinit var txtAddressText: TextView
    lateinit var txtAgeText: TextView
    lateinit var txtGenderText: TextView
    lateinit var imgAddContact: ImageView
    lateinit var recyclerView: RecyclerView
    lateinit var contactsAdapter: ContactsAdapter
    lateinit var layoutManager: LinearLayoutManager
    lateinit var fabEditProfile: FloatingActionButton
    lateinit var sharedPreferences: SharedPreferences
    lateinit var viewModel: ProfileViewModel
    lateinit var db: DatabaseReference
    private var dbContactsList = listOf<ContactsEntity>()

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
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        sharedPreferences = activity?.getSharedPreferences(getString(R.string.alert_system_sharedPref), Context.MODE_PRIVATE)!!

        txtUsernameText = view.findViewById(R.id.txtUsernameText)
        txtMobileText = view.findViewById(R.id.txtMobileText)
        txtAddressText = view.findViewById(R.id.txtAddressText)
        txtAgeText = view.findViewById(R.id.txtAgeText)
        txtGenderText = view.findViewById(R.id.txtGenderText)
        fabEditProfile = view.findViewById(R.id.fabEditProfile)
        imgAddContact = view.findViewById(R.id.imgAddContact)

        db = Firebase.database.reference
        val userId = sharedPreferences.getInt("uid", -1)

        recyclerView = view.findViewById(R.id.recyclerContacts)
        layoutManager = LinearLayoutManager(activity as Context, LinearLayoutManager.HORIZONTAL, true)

        //Helper method to fetch list of contacts and initialise the adapter
        setUpAdapter()

        fabEditProfile.setOnClickListener {
            //Open editing screen
            if (activity != null){
                activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, EditProfileFragment())
                    .commit()
            }
            else{
                Toast.makeText(activity as Context, "Error occurred while swapping fragment", Toast.LENGTH_SHORT).show()
            }
        }

        //Read data from the Firebase database and display it here
        val dataListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue<User>()

                txtUsernameText.text = user?.username
                txtGenderText.text = user?.gender
                txtAgeText.text = user?.age.toString()
                txtMobileText.text = user?.mobileNumber.toString()
                txtAddressText.text = user?.defAddress
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Error reading from DB", Toast.LENGTH_SHORT).show()
            }

        }

        db.child("users").child(userId.toString()).addValueEventListener(dataListener)

        imgAddContact.setOnClickListener {
            // Add contact
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, AddContactFragment())
                .commit()
        }

        return view
    }

    private fun setUpAdapter() {
        dbContactsList = RetrieveContacts(activity as Context).execute().get()

        if(dbContactsList.isEmpty()){
            recyclerView.visibility = View.GONE
        }
        else{
            contactsAdapter = ContactsAdapter(activity as Context,dbContactsList)
            recyclerView.adapter = contactsAdapter
            recyclerView.layoutManager = layoutManager
        }
    }

    class RetrieveContacts(context: Context): AsyncTask<Void, Void, List<ContactsEntity>>(){

        private val db = Room.databaseBuilder(context, ContactsDatabase::class.java,"contacts-db").build()

        override fun doInBackground(vararg p0: Void?): List<ContactsEntity> {
            val list = db.contactsDao().getAllContacts()

            db.close()
            return list
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}