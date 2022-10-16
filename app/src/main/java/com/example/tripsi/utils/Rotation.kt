package com.example.tripsi.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext

/**
 * Adapted from "Android Capture Image from Camera and Gallery" by Anupam Chugh
 * https://www.digitalocean.com/community/tutorials/android-capture-image-camera-gallery
 */

//this returns a rotated image
private fun rotateImage(img: Bitmap, degree: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degree)
    val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
    img.recycle()
    return rotatedImg
}

//this checks Exif data of an image to get the Orientation tag
//based on the orientation tag, the function determines if the image has to be rotated to display properly
fun rotateImageIfRequired(img: Bitmap, path: String): Bitmap {
    val ei = ExifInterface(path)

    return when (ei.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL
    )) {
        ExifInterface.ORIENTATION_ROTATE_90 -> {
            rotateImage(img, 90f)
        }
        ExifInterface.ORIENTATION_ROTATE_180 -> {
            rotateImage(img, 180f)
        }
        ExifInterface.ORIENTATION_ROTATE_270 -> {
            rotateImage(img, 270f)
        }
        else -> img
    }
}

// Function to set desired orientation inside of any view
@Composable
fun LockScreenOrientation(orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            // restore original orientation when view disappears
            activity.requestedOrientation = originalOrientation
        }
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}