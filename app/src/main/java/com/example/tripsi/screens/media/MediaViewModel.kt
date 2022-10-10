package com.example.tripsi.screens.media

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tripsi.data.InternalStoragePhoto
import com.example.tripsi.data.TripData
import com.example.tripsi.functionality.TripDbViewModel
import kotlinx.coroutines.*
import java.io.IOException

class MediaViewModel : ViewModel() {

    val imageBmps: MutableLiveData<MutableList<InternalStoragePhoto>> = MutableLiveData()

    suspend fun loadPhotosFromInternalStorage(
        context: Context,
        filename: String
    ): InternalStoragePhoto {
        var images: List<InternalStoragePhoto>? = null
        withContext(Dispatchers.IO) {
            val files = context.filesDir.listFiles()
            images = files?.filter {
                it.canRead() && it.isFile &&
                        it.name.equals("$filename.jpg")
            }?.map {
                val bytes = it.readBytes()
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                InternalStoragePhoto(it.name, bmp)
            }
        }
        return images!![0]
    }
}