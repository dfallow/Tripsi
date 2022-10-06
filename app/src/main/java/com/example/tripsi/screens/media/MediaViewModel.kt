package com.example.tripsi.screens.media

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripsi.data.InternalStoragePhoto
import com.example.tripsi.data.TripData
import com.example.tripsi.functionality.TripDbViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MediaViewModel : ViewModel() {

    val tripData = MutableLiveData<TripData>()

    fun getData(tripId: Int, tripDbViewModel: TripDbViewModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = tripDbViewModel.getTripData(tripId)

            tripData.postValue(data.value)
        }
    }

    fun loadPhotosFromInternalStorage(
        context: Context,
        filename: String
    ): List<InternalStoragePhoto> {
        var images: List<InternalStoragePhoto>? = null
        viewModelScope.launch(Dispatchers.IO) {
            val files = context.filesDir.listFiles()
            images = files?.filter {
                it.canRead() && it.isFile && it.name.equals("$filename.jpg")
            }?.map {
                val bytes = it.readBytes()
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                InternalStoragePhoto(it.name, bmp)
            }
        }
        images?.get(0)?.let { Log.d("GIGI", it.name) }
        return images ?: listOf()
    }
}