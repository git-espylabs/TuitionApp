package com.aplus.edu.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.aplus.edu.app.Session
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.math.min
import kotlin.math.roundToInt

object CommonUtils {

    fun getFileProviderName(context: Context) =
        "${context.packageName}.${Session.FILE_PROVIDER}"

    fun scaleDownImage(realImage: Bitmap): Bitmap {
        val maxImageSize = 300f
        val ratio = min(maxImageSize / realImage.width, maxImageSize / realImage.height)
        val width = (ratio * realImage.width).roundToInt()
        val height = (ratio * realImage.height).roundToInt()
        return Bitmap.createScaledBitmap(realImage, width, height, true)
    }

    fun compressAndSaveImage(context: Context, realImage: Bitmap, fileName: String): File {
        val bytes = ByteArrayOutputStream()
        realImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val resizedFile: File = createImageFile(context, fileName)
        val fos = FileOutputStream(resizedFile)
        fos.write(bytes.toByteArray())
        fos.close()
        return resizedFile
    }

    fun createImageFile(context: Context, fileName: String): File {
        val storageDir: File? =
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDir)
    }

    fun getBitmapFromUri(context: Context, photoUri: Uri): Bitmap? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(
                    context.contentResolver,
                    photoUri
                )
            )
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, photoUri)
        }
    }
}