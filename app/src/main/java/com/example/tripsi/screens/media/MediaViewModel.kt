package com.example.tripsi.screens.media

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.collection.ArrayMap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tripsi.data.InternalStoragePhoto
import com.example.tripsi.utils.rotateImageIfRequired
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MediaViewModel : ViewModel() {

    val imageBitmaps: MutableLiveData<MutableList<InternalStoragePhoto?>> = MutableLiveData()

    suspend fun loadPhotosFromInternalStorage(
        context: Context,
        filenamesAndNotes: List<ArrayMap<String, String?>>
    ) {
        var image: InternalStoragePhoto?
        val images: MutableList<InternalStoragePhoto?> = mutableListOf()
        withContext(Dispatchers.IO) {
            filenamesAndNotes.forEach { item ->
                item.forEach { (filename, note) ->
                    val files = context.filesDir.listFiles()
                    image = files?.filter {
                        it.canRead() && it.isFile &&
                                it.name.equals("$filename.jpg")
                    }?.map {
                        val bytes = it.readBytes()
                        val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        InternalStoragePhoto(it.name, bmp, it.absolutePath, note)
                    }?.first()
                    if (image != null) {
                        val rotatedImg = rotateImageIfRequired(image!!.bmp, image!!.path)
                        images.add(InternalStoragePhoto(image!!.name, rotatedImg, image!!.path, note))
                    }
                }
            }
        }
        imageBitmaps.postValue(images)
    }
}