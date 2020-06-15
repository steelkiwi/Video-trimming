package com.steelkiwi.videotrimming

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat.startActivityForResult
import com.steelkiwi.videotrimming.trim.TrimmerActivity
import com.steelkiwi.videotrimming.trim.TrimmerActivity.Companion.EXTRA_INPUT_URI
import com.steelkiwi.videotrimming.trim.TrimmerActivity.Companion.REQUEST_VIDEO_TRIMMER

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import java.io.File

public class VideotrimmingPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    private lateinit var channel: MethodChannel
    private var activityPluginBinding: ActivityPluginBinding? = null

    override fun onAttachedToEngine(@NonNull plugin: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(plugin.flutterEngine.dartExecutor, "videotrimming")
        channel.setMethodCallHandler(this);
    }

    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "videotrimming")
            channel.setMethodCallHandler(VideotrimmingPlugin())
        }
    }


    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (call.method == "trim_video") {
            val sourcePath = call.argument<String>("source_path")
            startTrimActivity(Uri.fromFile(File(sourcePath)));
        } else {
            result.notImplemented()
        }
    }

    //
    private fun startTrimActivity(uri: Uri) {
        val intent = Intent(activityPluginBinding!!.activity, TrimmerActivity::class.java)
        intent.putExtra(EXTRA_INPUT_URI, uri)
        activityPluginBinding?.activity?.startActivityForResult(intent, REQUEST_VIDEO_TRIMMER)
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onDetachedFromActivity() {
        TODO("Not yet implemented")
    }

    override fun onReattachedToActivityForConfigChanges(p0: ActivityPluginBinding) {
        TODO("Not yet implemented")
    }

    override fun onAttachedToActivity(p0: ActivityPluginBinding) {
        this.activityPluginBinding = p0; }

    override fun onDetachedFromActivityForConfigChanges() {
        TODO("Not yet implemented")
    }
}
