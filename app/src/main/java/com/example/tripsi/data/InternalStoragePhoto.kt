package com.example.tripsi.data

import android.graphics.Bitmap

// This is used to display images from storage in MediaView
data class InternalStoragePhoto(
    val name: String,
    val bmp: Bitmap,
    val path: String,
    val note: String?
)
