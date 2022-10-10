package com.example.tripsi.screens.media

import android.content.Context
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tripsi.data.InternalStoragePhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
                InternalStoragePhoto(it.name, bmp, it.absolutePath)
            }
        }
        return images!![0]
    }
}