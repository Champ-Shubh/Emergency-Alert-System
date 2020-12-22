package com.undispuated.alertsystem.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.undispuated.alertsystem.R
import com.undispuated.alertsystem.util.User
import java.util.*

class EditProfileFragment : Fragment() {

    companion object {
        fun newInstance() = EditProfileFragment()
    }

    private val REQUEST_PERMISSION = 9
    private lateinit var viewModel: ProfileViewModel
    lateinit var etProfileUsername: EditText
    lateinit var etProfileMobile: EditText
    lateinit var etProfileAge: EditText
    lateinit var etProfileGender: EditText
    lateinit var txtAutoAddress: TextView
    lateinit var btnSave: Button
    lateinit var addressText: String
    lateinit var sharedPreferences: SharedPreferences
    lateinit var db: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.edit_profile_fragment, container, false)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        sharedPreferences = activity!!.getSharedPreferences(getString(R.string.alert_system_sharedPref), Context.MODE_PRIVATE)

        db = Firebase.database.reference

        etProfileUsername = view.findViewById(R.id.etProfileUsername)
        etProfileMobile = view.findViewById(R.id.etProfileMobile)
        etProfileAge = view.findViewById(R.id.etProfileAge)
        etProfileGender = view.findViewById(R.id.etProfileGender)
        txtAutoAddress = view.findViewById(R.id.txtAutoAddress)
        btnSave = view.findViewById(R.id.btnSave)

        if (ActivityCompat.checkSelfPermission(
                activity as Context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity as Context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            ActivityCompat.requestPermissions(activity as Activity, permissions, REQUEST_PERMISSION)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        else {
            startLocationUpdate()
        }

        val username = sharedPreferences.getString("username", "Mr. Anonymous")
        val userId = sharedPreferences.getInt("uid",-1)
        val mobileText = etProfileMobile.text.toString()
        val ageText = etProfileAge.text.toString()
        val genderText = etProfileGender.text.toString()

        btnSave.setOnClickListener {
            val user = User(username.toString(),
                mobileText.toInt(),
                ageText.toInt(),
                genderText,
                addressText
            )

            // Save changes to the ViewModel
            viewModel.profileName = username.toString()
            viewModel.userAge = ageText.toInt()
            viewModel.userGender = genderText
            viewModel.userMobile = mobileText.toInt()

            db.child("users").child(userId.toString()).setValue(user).addOnSuccessListener {
                //On Success
                Toast.makeText(activity, "Details saved !!", Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                    //On Failure
                }

            activity?.supportFragmentManager?.popBackStack()
        }

        return view
    }

    fun startLocationUpdate(){
        viewModel.locationData.observe(this, Observer {
            val geocoder = Geocoder(activity as Context, Locale.getDefault())

            val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
            val address = addresses[0].getAddressLine(0)
            val city = addresses[0].locality
            val state = addresses[0].adminArea

            addressText = getString(R.string.txt_current_location, address, city, state)
            txtAutoAddress.text = addressText
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSION){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(activity, "Permission Granted", Toast.LENGTH_SHORT).show()
                startLocationUpdate()
            }
            else{
                Toast.makeText(activity, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

}