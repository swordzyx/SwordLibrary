package com.swordlibrary.utils

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.TypedValue
import com.swordlibrary.LogUtils

class UriUtils {

    /**
     * 通过Uri获取文件路径
     * 示例：content://com.android.providers.media.documents/document/image%3A1000000315 ，返回 /storage/emulated/0/Pictures/black/3-7-2.png
     */
    fun getPathFromUri(context: Context, uri: Uri): String? {
        var path: String? = null

        if (DocumentsContract.isDocumentUri(context, uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]

            var contentUri: Uri? = null
            if ("image" == type) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            } else if ("video" == type) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            } else if ("audio" == type) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            } else if ("primary" == type) {
                return "/storage/emulated/0/${split[1]}"
            }

            val selection = "_id=?"
            val selectionArgs = arrayOf(split[1])
            if (contentUri == null) {
                LogUtils.e("get content uri failed, docId: $docId")
                return null
            }

            path = getDataColumn(context, contentUri, selection, selectionArgs)
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            path = getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            path = uri.path
        }

        return path
    }

    private fun getDataColumn(
        context: Context,
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?,
    ): String? {
        var path: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        
        context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            .use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndexOrThrow(projection[0])
                    path = cursor.getString(columnIndex)
                }
            }
        return path
    }

    private fun dp2px(dpValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpValue,
            Resources.getSystem().displayMetrics
        ).toInt()
    }
}