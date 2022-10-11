package com.example.tripsi

import android.content.Intent
import android.content.pm.PackageManager
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.NfcEvent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.ui.theme.TripsiTheme
import com.example.tripsi.utils.BottomNavigation
import com.example.tripsi.utils.Location
import org.osmdroid.config.Configuration

class MainActivity : ComponentActivity(), NfcAdapter.CreateNdefMessageCallback {
    companion object {
        private lateinit var tripDbViewModel: TripDbViewModel
        private var nfcAdapter: NfcAdapter? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //initialize database view model
        tripDbViewModel = TripDbViewModel(application)

        if ((Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) !=
                    PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )
        }

        // sets user agent allowing map to be used
        Configuration.getInstance().load(
            this,
            PreferenceManager.getDefaultSharedPreferences(this)
        )
        Configuration.getInstance().userAgentValue

        //
        val location = Location(this)


        //NFC

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show()
        }

        //register callback
        nfcAdapter?.setNdefPushMessageCallback(this, this)



        setContent {
            TripsiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    //for testing purposes
                    //Text(trips.value.toString())

                    BottomNavigation(context = this, location, tripDbViewModel)
                }
            }
        }
    }

    override fun createNdefMessage(p0: NfcEvent): NdefMessage {
        val text = "Here are my contacts!\n\n"

        return NdefMessage(
            arrayOf(
                NdefRecord.createMime(this.filesDir.toString(), text.toByteArray())
            )
        )
    }

    override fun onResume() {
        super.onResume()
        // Check to see that the Activity started due to an Android Beam
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            processIntent(intent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // onResume gets called after this to handle the intent
        setIntent(intent)
    }


    //Parses the NDEF Message from the intent and prints to the TextView
    private fun processIntent(intent: Intent) {
        // only one message sent during the beam
        intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMsgs ->
            (rawMsgs[0] as NdefMessage).apply {
                // record 0 contains the MIME type, record 1 is the AAR, if present
                Log.d("GIGI NFC", String(records[0].payload))
            }
        }
    }
}
