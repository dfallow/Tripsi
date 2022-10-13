package com.example.tripsi.screens.media

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.collection.ArrayMap
import androidx.collection.arrayMapOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripsi.data.InternalStoragePhoto
import com.example.tripsi.data.TripData
import com.example.tripsi.functionality.TripDbViewModel
import com.example.tripsi.screens.currentTrip.MomentPosition
import com.example.tripsi.screens.currentTrip.imagesAndFilenames
import com.example.tripsi.utils.rotateImageIfRequired
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*

class MediaViewModel : ViewModel() {

    val imageBitmaps: MutableLiveData<MutableList<InternalStoragePhoto?>> = MutableLiveData()
    private val imagesAndFilenames: MutableList<ArrayMap<Bitmap, String>> = mutableListOf()

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
                        imagesAndFilenames.add(arrayMapOf(Pair(rotatedImg, filename)))

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

    val startCity: MutableLiveData<String?> = MutableLiveData(null)
    val endCity: MutableLiveData<String?> = MutableLiveData(null)

    private val startLat: MutableLiveData<Double> = MutableLiveData(null)
    private val startLong: MutableLiveData<Double> = MutableLiveData(null)
    private val endLat: MutableLiveData<Double> = MutableLiveData(null)
    private val endLong: MutableLiveData<Double> = MutableLiveData(null)

    suspend fun getStartEndCoords(tripData: TripData, context: Context) {
        withContext(Dispatchers.IO) {
            tripData.location?.forEach {
                if (it.position.position == MomentPosition.START.position) {
                    startLat.postValue(it.coordsLatitude)
                    startLong.postValue(it.coordsLongitude)

                } else if (it.isEnd) {
                    endLat.postValue(it.coordsLatitude)
                    endLong.postValue(it.coordsLongitude)
                }
            }
        }
        if (startLat.value != null && startLong != null && endLat != null && endLong != null) {
            getStartLocale(context)
            getEndLocale(context)
        }
    }


    //this function converts trip start or end coordinates into a city name
    private fun getStartLocale(context: Context) {
        val geoCoder = Geocoder(context, Locale.getDefault())
        val address: MutableList<Address>

        if (startLat.value != null && startLong.value != null) {
            address = geoCoder.getFromLocation(startLat.value!!, startLong.value!!, 1)
            startCity.postValue(address[0].locality.toString())
        }
    }

    private fun getEndLocale(context: Context) {
        val geoCoder = Geocoder(context, Locale.getDefault())
        val address: MutableList<Address>

        if (endLat.value != null && endLong.value != null) {
            address = geoCoder.getFromLocation(endLat.value!!, endLong.value!!, 1)
            endCity.postValue(address[0].locality.toString())
        }
    }

    suspend fun deleteTrip(tripId: Int, tripDbViewModel: TripDbViewModel, context: Context) {

        val files = context.filesDir.listFiles()
        val toRemove : MutableList<ArrayMap<Bitmap, String>> = mutableListOf()

        withContext(Dispatchers.IO) {
            imagesAndFilenames.forEach { pair ->
                pair.forEach { (image, filename) ->
                    val file = files?.first {
                        it.name.equals("$filename.jpg")
                    }
                    try {
                        file?.delete()
                        toRemove.add(arrayMapOf(Pair(image, filename)))
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            tripDbViewModel.deleteTripById(tripId)
            imagesAndFilenames.removeAll(toRemove)

        }

    }

    fun deleteOneImage() {
    }
}