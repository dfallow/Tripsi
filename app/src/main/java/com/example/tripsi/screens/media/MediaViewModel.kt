package com.example.tripsi.screens.media

import android.content.Context
import android.graphics.Bitmap
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
import java.io.IOException

class MediaViewModel : ViewModel() {

    var image: MutableLiveData<InternalStoragePhoto>? = null

    fun loadPhotosFromInternalStorage(
        context: Context,
        filename: String
    ) {
        var images: List<InternalStoragePhoto>? = null
        viewModelScope.launch(Dispatchers.IO) {
            val files = context.filesDir.listFiles()
            images = files?.filter {
                //it.canRead() && it.isFile &&
                        it.name.equals("$filename.jpg")
            }?.map {
                val bytes = it.readBytes()
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                InternalStoragePhoto(it.name, bmp)
            }
        }
        if (images != null) {
            image!!.postValue(images!![0])
        }
        //return images?.get(0)
    }
}