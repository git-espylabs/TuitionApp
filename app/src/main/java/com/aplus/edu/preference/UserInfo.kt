package com.aplus.edu.preference

import com.aplus.edu.constants.SharedPrefereceConstants

class UserInfo {
    var uid by Preference(SharedPrefereceConstants.PREF_UID, "")
    var classid by Preference(SharedPrefereceConstants.PREF_CLASSID, "")

    ///
    var emailid by Preference(SharedPrefereceConstants.PREF_EMAILID, "")
    var mob by Preference(SharedPrefereceConstants.PREF_MOB, "")
    var photo by Preference(SharedPrefereceConstants.PREF_PHOTO, "")

    var studname by Preference(SharedPrefereceConstants.PREF_STUDNAME, "")

    var studclass by Preference(SharedPrefereceConstants.PREF_STUD_CLASS, "")
}