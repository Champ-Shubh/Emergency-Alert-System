package com.undispuated.alertsystem.fragment

import android.app.Activity
import android.app.PendingIntent
import android.content.*
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.undispuated.alertsystem.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var rlEmergency: RelativeLayout
    lateinit var rlAccident: RelativeLayout
    lateinit var rlMedical: RelativeLayout
    lateinit var rlTheft: RelativeLayout
    lateinit var rlTerrorism: RelativeLayout
    lateinit var txtGreeting: TextView
    lateinit var username: String
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
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        sharedPreferences = activity!!.getSharedPreferences(
            getString(R.string.alert_system_sharedPref),
            Context.MODE_PRIVATE
        )
        username = sharedPreferences.getString("username", "Mr. Anonymous").toString()

        rlEmergency = view.findViewById(R.id.rlEmergency)
        rlAccident = view.findViewById(R.id.rlAccident)
        rlMedical = view.findViewById(R.id.rlMedical)
        rlTheft = view.findViewById(R.id.rlTheft)
        rlTerrorism = view.findViewById(R.id.rlTerrorism)
        txtGreeting = view.findViewById(R.id.txtGreeting)

        txtGreeting.text = getString(R.string.greeting_text, username)

        rlEmergency.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(
                R.id.frameLayout,
                EmergencyFragment()
            )?.commit()

            sendSMS("")
        }

        rlMedical.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(
                R.id.frameLayout,
                MedicalFragment()
            )?.commit()

            sendSMS("Medical")
        }
        // TODO: Add actual phone numbers while sending SMS, but first test the current code
        // TODO: Add fragments and code for other emergency types too

        rlAccident.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(
                R.id.frameLayout,
                AccidentFragment()
            )?.commit()

            sendSMS("Accidental")
        }

        rlTheft.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(
                R.id.frameLayout,
                TheftFragment()
            )?.commit()

            sendSMS("Theft")
        }

        rlTerrorism.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(
                R.id.frameLayout,
                TerrorismFragment()
            )?.commit()

            sendSMS("Terror")
        }

        return view
    }

    private fun sendSMS(emergencyType: String) {
        val message = " \bURGENT\b Hey buddy !! One of your friend $username is in $emergencyType Emergency. He/She needs your help urgently."

        //TODO: Add Pending Intents and Broadcast Receivers to monitor the SMS sending process

        val SENT = "SMS_SENT"
        val DELIVERED = "SMS_DELIVERED"

        val sentPI = PendingIntent.getBroadcast(
            activity, 0,
            Intent(SENT), 0
        )

        val deliveredPI = PendingIntent.getBroadcast(
            activity, 0,
            Intent(DELIVERED), 0
        )

        // Broadcast Receiver for SMS sent
        activity?.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(arg0: Context, arg1: Intent) {
                when (resultCode) {
                    Activity.RESULT_OK -> Toast.makeText(
                        activity, "SMS sent",
                        Toast.LENGTH_SHORT
                    ).show()
                    SmsManager.RESULT_ERROR_GENERIC_FAILURE -> Toast.makeText(
                        activity, "Generic failure",
                        Toast.LENGTH_SHORT
                    ).show()
                    SmsManager.RESULT_ERROR_NO_SERVICE -> Toast.makeText(
                        activity, "No service",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }, IntentFilter(SENT))

        // Broadcast Receiver for SMS delivered
        activity?.registerReceiver(object : BroadcastReceiver(){
            override fun onReceive(p0: Context?, p1: Intent?) {
                when (resultCode) {
                    Activity.RESULT_OK -> Toast.makeText(
                        activity, "SMS delivered",
                        Toast.LENGTH_SHORT
                    ).show()
                    Activity.RESULT_CANCELED -> Toast.makeText(
                        activity, "SMS delivery cancelled",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }, IntentFilter(DELIVERED))

        val sms = SmsManager.getDefault()
        sms.sendTextMessage("+918601648405", null, message, sentPI, deliveredPI)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}