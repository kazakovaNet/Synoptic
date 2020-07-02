package ru.kazakovanet.synoptic.data.provider

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

/**
 * Created by NKazakova on 01.07.2020.
 */
abstract class PreferenceProvider(context: Context) {
    private val appContext = context.applicationContext
    protected val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)
}