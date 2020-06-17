package com.steelkiwi.videotrimming.trim

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.lb.video_trimmer_library.interfaces.VideoTrimmingListener
import com.steelkiwi.videotrimming.R
import io.flutter.app.FlutterActivity
import kotlinx.android.synthetic.main.activity_trimmer.*
import java.io.File
import java.util.*

class TrimmerActivity : FlutterActivity(), VideoTrimmingListener {
//    private var progressDialog: ProgressDialog? = null


    companion object {
        const val REQUEST_VIDEO_TRIMMER = 1
        internal const val EXTRA_INPUT_URI = "EXTRA_INPUT_URI"
        internal const val EXTRA_INPUT_MAX_SECONDS = "EXTRA_INPUT_MAX_SECONDS"
        internal const val EXTRA_OUTPUT_FILE = "EXTRA_OUTPUT_FILE"
//        private val allowedVideoFileExtensions = arrayOf("mkv", "mp4", "3gp", "mov", "mts")
//        private val videosMimeTypes = ArrayList<String>(allowedVideoFileExtensions.size)
    }

    @RequiresApi(Build.VERSION_CODES.FROYO)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trimmer)
        var  videoTrimingView=findViewById<VideoTrimmerView>(R.id.videoTrimmerView);
        videoTrimingView.onCancelListener={
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        };
        val inputVideoUri: String? = intent?.getStringExtra(EXTRA_INPUT_URI)
        val maxSeconds: Double? = intent?.getDoubleExtra(EXTRA_INPUT_MAX_SECONDS, 15.0) ?: 15.0;
        if (inputVideoUri == null) {
            finish()
            return
        }
        //setting progressbar
        if (maxSeconds != null) {
            videoTrimmerView.setMaxDurationInMs((maxSeconds * 1000).toInt())
        }

        videoTrimmerView.setOnK4LVideoListener(this)
        val parentFolder = getExternalFilesDir(null)!!
        parentFolder.mkdirs()
        val fileName = "trimmedVideo_${System.currentTimeMillis()}.mp4"
        val trimmedVideoFile = File(parentFolder, fileName)
        videoTrimmerView.setDestinationFile(trimmedVideoFile)
        videoTrimmerView.setVideoURI(Uri.fromFile(File(inputVideoUri)))

        videoTrimmerView.setVideoInformationVisibility(true)
    }

    override fun onTrimStarted() {
        trimmingProgressView.visibility = View.VISIBLE
    }

    override fun onFinishedTrimming(uri: Uri?) {
        trimmingProgressView.visibility = View.GONE
        if (uri == null) {
            Toast.makeText(this@TrimmerActivity, "failed trimming", Toast.LENGTH_SHORT).show()
        } else {
            val msg = "saved " + uri.path;
            Toast.makeText(this@TrimmerActivity, msg, Toast.LENGTH_SHORT).show()
            val intent = Intent()
            intent.putExtra(EXTRA_OUTPUT_FILE, uri.path)
            setResult(Activity.RESULT_OK, intent)
            finish()
//            val intent = Intent(Intent.ACTION_VIEW, uri)
//            intent.setDataAndType(uri, "video/mp4")
//            startActivity(intent)
        }
        //finish()
    }

    override fun onErrorWhileViewingVideo(what: Int, extra: Int) {
        trimmingProgressView.visibility = View.GONE
        Toast.makeText(this@TrimmerActivity, "error while previewing video", Toast.LENGTH_SHORT).show()
    }

    override fun onVideoPrepared() {
        //        Toast.makeText(TrimmerActivity.this, "onVideoPrepared", Toast.LENGTH_SHORT).show();
    }
}
