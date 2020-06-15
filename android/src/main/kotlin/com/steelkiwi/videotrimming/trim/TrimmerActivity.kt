//package com.steelkiwi.videotrimming.trim
//
//import android.app.Activity
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.view.View
//import android.widget.Toast
//import io.flutter.app.FlutterActivity
//import kotlinx.android.synthetic.main.activity_trimmer.*
//import java.io.File
//import java.util.*
//
//class TrimmerActivity : FlutterActivity(), VideoTrimmingListener {
////    private var progressDialog: ProgressDialog? = null
//
//
//    companion object {
//            private const val REQUEST_VIDEO_TRIMMER = 1
//        private const val REQUEST_STORAGE_READ_ACCESS_PERMISSION = 2
//        internal const val EXTRA_INPUT_URI = "EXTRA_INPUT_URI"
//        private val allowedVideoFileExtensions = arrayOf("mkv", "mp4", "3gp", "mov", "mts")
//        private val videosMimeTypes = ArrayList<String>(allowedVideoFileExtensions.size)
//    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_trimmer)
//        val inputVideoUri: Uri? = intent?.getParcelableExtra(EXTRA_INPUT_URI)
//        if (inputVideoUri == null) {
//            finish()
//            return
//        }
//        //setting progressbar
////        progressDialog = ProgressDialog(this)
////        progressDialog!!.setCancelable(false)
////        progressDialog!!.setMessage(getString(R.string.trimming_progress))
//        videoTrimmerView.setMaxDurationInMs(15 * 1000)
//
//        videoTrimmerView.setOnK4LVideoListener(this)
//        val parentFolder = getExternalFilesDir(null)!!
//        parentFolder.mkdirs()
//        val fileName = "trimmedVideo_${System.currentTimeMillis()}.mp4"
//        val trimmedVideoFile = File(parentFolder, fileName)
//        videoTrimmerView.setDestinationFile(trimmedVideoFile)
//        videoTrimmerView.setVideoURI(inputVideoUri)
//
//        videoTrimmerView.setVideoInformationVisibility(true)
//    }
//
//    override fun onTrimStarted() {
//        trimmingProgressView.visibility = View.VISIBLE
//    }
//
//    override fun onFinishedTrimming(uri: Uri?) {
//        trimmingProgressView.visibility = View.GONE
//        if (uri == null) {
//            Toast.makeText(this@TrimmerActivity, "failed trimming", Toast.LENGTH_SHORT).show()
//        } else {
//            val msg = "saved "+ uri.path;
//            Toast.makeText(this@TrimmerActivity, msg, Toast.LENGTH_SHORT).show()
//            val intent = Intent()
//            intent.putExtra("trim_video", uri.path)
//            setResult(Activity.RESULT_OK, intent)
//            finish()
////            val intent = Intent(Intent.ACTION_VIEW, uri)
////            intent.setDataAndType(uri, "video/mp4")
////            startActivity(intent)
//        }
//        //finish()
//    }
//
//    override fun onErrorWhileViewingVideo(what: Int, extra: Int) {
//        trimmingProgressView.visibility = View.GONE
//        Toast.makeText(this@TrimmerActivity, "error while previewing video", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onVideoPrepared() {
//        //        Toast.makeText(TrimmerActivity.this, "onVideoPrepared", Toast.LENGTH_SHORT).show();
//    }
//}
