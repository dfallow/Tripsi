package com.example.tripsi.screens.media

import android.content.Context
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.collection.ArrayMap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tripsi.data.InternalStoragePhoto
import com.example.tripsi.data.LocationWithImagesAndNotes
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.utils.rotateImageIfRequired
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class MediaViewModel : ViewModel() {

    val imageBitmaps: MutableLiveData<MutableList<InternalStoragePhoto?>> = MutableLiveData()

    suspend fun loadPhotosFromStorage(
        context: Context,
        filenamesAndNotes: List<ArrayMap<String, String?>>
    ) {
        var image: InternalStoragePhoto?

        val images: MutableList<InternalStoragePhoto?> = mutableListOf()

        withContext(Dispatchers.IO) {
            // for each filename in filenamesAndNotes list, retrieve a photo from storage
            filenamesAndNotes.forEach { item ->
                item.forEach { (filename, note) ->
                    //first, get the list of files in filesDir
                    val files = context.filesDir.listFiles()

                    //second, filter through the list looking for a file matching the filename
                    image = files?.filter {
                        it.canRead() && it.isFile &&
                                it.name.equals("$filename.jpg")
                    }?.map {
                        //then transform the file into InternalStoragePhoto
                        val bytes = it.readBytes()
                        val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        InternalStoragePhoto(it.name, bmp, it.absolutePath, note)
                    }
                        ?.first() //there will always be just one image with the same name, so only return that from the list
                    if (image != null) {
                        //check if the image needs to be rotated to display in correct orientation
                        val rotatedImg = rotateImageIfRequired(image!!.bmp, image!!.path)

                        //every rotated image is added to the images list, until the code runs through all filenames
                        images.add(
                            InternalStoragePhoto(
                                image!!.name,
                                rotatedImg,
                                image!!.path,
                                note
                            )
                        )
                    }
                }
            }
        }
        //post all the images to imageBitmaps
        imageBitmaps.postValue(images)
    }

    //this function converts trip start or end coordinates into a city name
    fun getCity(coordinates: LocationWithImagesAndNotes?, context: Context): String? {
        val geoCoder = Geocoder(context, Locale.getDefault())
        val address = coordinates?.location?.let {
            geoCoder.getFromLocation(
                it.coordsLatitude,
                it.coordsLongitude,
                1
            )
        }
        return address?.get(0)?.locality
    }

    fun getDistance(startLat: Double?, startLong: Double?, endLat: Double?, endLong: Double?): Int? {
        var distance: Int? = null
        if (startLat != null && startLong != null && endLat != null && endLong != null) {
            val start = Location("start")
            start.latitude = startLat
            start.longitude = startLong

            val end = Location("end")
            end.latitude = endLat
            end.longitude = endLong

            distance = start.distanceTo(end).toInt()
        }
        return distance
    }
}