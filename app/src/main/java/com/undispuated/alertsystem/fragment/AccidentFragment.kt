package com.undispuated.alertsystem.fragment

import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.undispuated.alertsystem.R
import java.util.*

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccidentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccidentFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var txtDetectedLoc: TextView
    private lateinit var btnCancelAlert: Button
    private lateinit var btnOutOfEmergency: Button
    private lateinit var viewModel: ProfileViewModel

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
        val view = inflater.inflate(R.layout.fragment_accident, container, false)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        txtDetectedLoc = view.findViewById(R.id.detected_loc_accident)
        btnCancelAlert = view.findViewById(R.id.btnCancelAccident)
        btnOutOfEmergency = view.findViewById(R.id.btnOutOfEmergAccident)

        val location = viewModel.locationData.value

        if(location == null){
            txtDetectedLoc.text = ""
        }
        else{
            val geocoder = Geocoder(activity as Context, Locale.getDefault())

            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            val address = addresses[0].getAddressLine(0)
            val city = addresses[0].locality
            val state = addresses[0].adminArea

            txtDetectedLoc.text = getString(R.string.txt_current_location, address, city, state)
        }

        btnCancelAlert.setOnClickListener {
            //Implement Cancel Button
        }

        btnOutOfEmergency.setOnClickListener {
            //Implement Out of Emergency Button
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
         * @return A new instance of fragment AccidentFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AccidentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}