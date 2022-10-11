package com.example.tripsi

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.nfc.NfcAdapter
import android.nfc.NfcEvent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.preference.PreferenceManager
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.ui.theme.TripsiTheme
import com.example.tripsi.utils.BottomNavigation
import com.example.tripsi.utils.Location
import org.osmdroid.config.Configuration
import java.io.File
import java.net.URI

class MainActivity : ComponentActivity() {
    companion object {
        private lateinit var tripDbViewModel: TripDbViewModel
        lateinit var nfcAdapter: NfcAdapter
        // Flag to indicate that Android Beam is available
        private var androidBeamAvailable = false
    }

    // List of URIs to provide to Android Beam
    val fileUris = mutableListOf<Uri>()
    /**
     * Callback that Android Beam file transfer calls to get
     * files to share
     */
    private inner class FileUriCallback : NfcAdapter.CreateBeamUrisCallback {
        /**
         * Create content URIs as needed to share with another device
         */
        override fun createBeamUris(event: NfcEvent): Array<Uri> {
            return fileUris.toTypedArray()
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        androidBeamAvailable = if(!packageManager.hasSystemFeature(PackageManager.FEATURE_NFC)) {
            // NFC isn't available on the device
            /*
              * Disable NFC features here.
              * For example, disable menu items or buttons that activate
              * NFC-related features
              */
            false
            // Android Beam file transfer isn't supported
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // If Android Beam isn't available, don't continue.
            androidBeamAvailable = false
            /*
             * Disable Android Beam file transfer features here.
             */
            false
        } else {
            nfcAdapter = NfcAdapter.getDefaultAdapter(this)
            true
        }

        // Android Beam file transfer is available, continue
        nfcAdapter = NfcAdapter.getDefaultAdapter(this).apply {

            /*
             * Instantiate a new FileUriCallback to handle requests for
             * URIs
             */
            val fileUriCallback = FileUriCallback()
            // Set the dynamic callback for URI requests.
            nfcAdapter.setBeamPushUrisCallback(fileUriCallback, this@MainActivity)
        }

        /*
         * Create a list of URIs, get a File,
         * and set its permissions
         */
        val fileUris = mutableListOf<Uri>()
        val transferFile = "my_contact_information.txt"
        val dir = this.filesDir
        Log.d("GIGI dir", dir.absolutePath)
        val requestFile = File(dir, transferFile).apply {
            setReadable(true, false)
        }
        val path = requestFile.absolutePath
        Log.d("GIGI path", path)
        val uri = Uri.parse("content://com.example.tripsi/my_images/$transferFile")
        Log.d("GIGI uri", uri.toString())
        fileUris += uri
        /*// Get a URI for the File and add it to the list of URIs
        Uri.fromFile(requestFile)?.also { fileUri ->
            Log.d("GIGI uri", fileUri.toString())
            fileUris += fileUri
        } ?: Log.e("My Activity", "No File URI available for file.")*/


        //   /data/data/com.example.tripsi/files/myContactInformation.txt

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

        setContent {
            TripsiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    //for testing purposes
                    //Text(trips.value.toString())
                    BottomNavigation(context = this, location, tripDbViewModel, application)
                }
            }
        }
    }
}
