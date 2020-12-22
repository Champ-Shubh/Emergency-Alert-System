package com.undispuated.alertsystem.fragment

import android.content.Context
import android.content.SharedPreferences
import android.location.Address
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.undispuated.alertsystem.R
import com.undispuated.alertsystem.util.User

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
    lateinit var fabEditProfile: FloatingActionButton
    lateinit var sharedPreferences: SharedPreferences
    lateinit var viewModel: ProfileViewModel

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
            //TODO : Remember you have to use a ViewModel shared between both this and the Editing Fragment to exchange data
            
        }

        return view
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