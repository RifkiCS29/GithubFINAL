package com.rifki.kotlin.mygithubfinal.view.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import com.rifki.kotlin.mygithubfinal.R
import com.rifki.kotlin.mygithubfinal.receiver.AlarmReceiver
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var mSharedPreferences: SharedPreferences

    companion object{
        const val PREFS_NAME = "SettingPreference"
        private const val DAILY_REMINDER = "daily_reminder"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        supportActionBar?.title = getString(R.string.setting)

        alarmReceiver = AlarmReceiver()
        mSharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        setSwitch()

        Daily_Switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                saveSetting(true)
                alarmReceiver.setDailyAlarm(this, AlarmReceiver.EXTRA_TYPE, "09:00")
            } else {
                saveSetting(false)
                alarmReceiver.cancelAlarm(this, AlarmReceiver.TYPE_REPEATING)
            }
        }

        btn_language.setOnClickListener{
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }
    }

    private fun setSwitch() {
        Daily_Switch.isChecked = mSharedPreferences.getBoolean(DAILY_REMINDER, false)
    }

    private fun saveSetting(value: Boolean) {
        val editor = mSharedPreferences.edit()
        editor.putBoolean(DAILY_REMINDER, value)
        editor.apply()
    }
}