package com.undispuated.alertsystem.fragment

import android.app.Application
import androidx.lifecycle.ViewModel
import com.undispuated.alertsystem.model.LocationLiveData

class ProfileViewModel(application: Application) : ViewModel() {
    var profileName: String = ""
    var userMobile: Int = 0
    var userAge: Int = 0
    var userGender: String = ""

    var locationData = LocationLiveData(application)
}