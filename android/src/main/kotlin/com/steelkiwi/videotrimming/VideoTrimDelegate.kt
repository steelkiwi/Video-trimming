package com.steelkiwi.videotrimming

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.steelkiwi.videotrimming.trim.TrimmerActivity
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry
import java.io.File


class VideoTrimDelegate(private var activity: Activity) : PluginRegistry.ActivityResultListener {
    private var pendingResult: MethodChannel.Result? = null
    private val fileUtils: FileUtils = FileUtils()


    fun startTrim(call: MethodCall, result: MethodChannel.Result?) {
        val sourcePath = call.argument<String>("source_path")
        val maxWidth = call.argument<Int>("max_seconds")

        pendingResult = result

        val intent = Intent(activity, TrimmerActivity::class.java)
        intent.putExtra(TrimmerActivity.EXTRA_INPUT_URI, sourcePath)
        activity?.startActivityForResult(intent, TrimmerActivity.REQUEST_VIDEO_TRIMMER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == TrimmerActivity.REQUEST_VIDEO_TRIMMER) {
                var data = data?.getStringExtra("trim_video");
                //result?.success(data)

                if (data != null) {
                    finishWithSuccess(data)
                } else {
//                    val cropError: Throwable = UCrop.getError(data)
                    // finishWithError("crop_error", cropError.localizedMessage, cropError)
                }

                return true

            }
        } else if (pendingResult != null) {
            // pendingResult.success(null);
            clearMethodCallAndResult();
            return true;
        }
        return false
    }


    private fun finishWithSuccess(imagePath: String) {
        if (pendingResult != null) {
            pendingResult!!.success(imagePath)
            clearMethodCallAndResult()
        }
    }

    private fun finishWithError(errorCode: String, errorMessage: String, throwable: Throwable) {
        if (pendingResult != null) {
            pendingResult!!.error(errorCode, errorMessage, throwable)
            clearMethodCallAndResult()
        }
    }


    private fun clearMethodCallAndResult() {
        pendingResult = null
    }

}