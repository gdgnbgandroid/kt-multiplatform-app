package io.github.gdgnbgandroid.mpp.mobile

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        if (sharedPreferences.getString(KEY_USER_IDENTIFICATION, "")?.isBlank() == true) {
            overridePendingTransition(0, 0)
            startActivityForResult(Intent(this, LoginActivity::class.java), REQUEST_CODE_USER)
        } else {
            loadTopics()
        }
    }

    private fun loadTopics() {
        val meetupTopicListAdapter = MeetupTopicListAdapter(this, Repository.topics.toList())
        topiclistView.adapter = meetupTopicListAdapter

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            val intent = Intent(this, AddTopicActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_USER && resultCode == Activity.RESULT_OK && data != null) {
            sharedPreferences
                .edit()
                .putString(
                    KEY_USER_IDENTIFICATION,
                    data.getStringExtra(LoginActivity.EXTRA_USER_IDENTIFICATION)
                )
                .apply()
            loadTopics()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        private const val KEY_USER_IDENTIFICATION = "key_user_identification"
        private const val REQUEST_CODE_USER = 591
    }
}