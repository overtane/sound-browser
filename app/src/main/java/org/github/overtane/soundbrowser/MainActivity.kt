package org.github.overtane.soundbrowser

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private var _pendingIntent: PendingIntent? = null
    val pendingIntent
        get() = _pendingIntent
    val hasPendingIntent: Boolean
        get() = _pendingIntent != null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Get selected sound in fragment result and pass it to origin of pending intent
        supportFragmentManager.setFragmentResultListener(SELECTED_SOUND_KEY, this) { _, bundle ->
            Log.d("MainActivity", "Got fragment result")
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("SoundBrowser", "onNewIntent")
        intent?.extras?.let {
            Log.d("SoundBrowser", "Intent has extras")
            Log.d("SoundBrowser", "Extras == $it)")
            val pi: PendingIntent? = it.getParcelable("PendingIntent")
            _pendingIntent = pi
            Log.d("SoundBrowser", "PendingIntent == $hasPendingIntent")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    companion object {
        const val SELECTED_SOUND_KEY = "SelectedSound"
    }

}