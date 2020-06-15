package com.steelkiwi.videotrimming

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.annotation.NonNull
import com.steelkiwi.videotrimming.trim.TrimmerActivity
import com.steelkiwi.videotrimming.trim.TrimmerActivity.Companion.EXTRA_INPUT_URI
import com.steelkiwi.videotrimming.trim.TrimmerActivity.Companion.REQUEST_VIDEO_TRIMMER
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import java.io.File


public class VideotrimmingPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    private lateinit var channel: MethodChannel
    private var activityPluginBinding: ActivityPluginBinding? = null

    private var delegate: VideoTrimDelegate? = null


    override fun onAttachedToEngine(@NonNull plugin: FlutterPlugin.FlutterPluginBinding) {


        setupEngine(plugin.binaryMessenger);

    }

    companion object {
        private val CHANNEL = "plugins.steelkiwi.com/trimmer_video"
        private val ACTION_CHANEL_TRIM_VIDEO = "trim_video"

        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), CHANNEL)
            channel.setMethodCallHandler(VideotrimmingPlugin())


        }
    }


    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (call.method == ACTION_CHANEL_TRIM_VIDEO) {
            delegate?.startTrim(call, result);
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
        delegate?.let { activityPluginBinding?.removeActivityResultListener(it) };
        activityPluginBinding = null;
        delegate = null; }

    override fun onReattachedToActivityForConfigChanges(p0: ActivityPluginBinding) {
        onAttachedToActivity(p0);
    }

    private fun setupActivity(activity: Activity): VideoTrimDelegate? {
        delegate = VideoTrimDelegate(activity)
        return delegate
    }

    override fun onAttachedToActivity(activityPluginBinding: ActivityPluginBinding) {
        setupActivity(activityPluginBinding.activity);
        this.activityPluginBinding = activityPluginBinding;
        delegate?.let { activityPluginBinding.addActivityResultListener(it) }; }

    private fun setupEngine(messenger: BinaryMessenger) {
        val channel = MethodChannel(messenger, CHANNEL)
        channel.setMethodCallHandler(this)
//        channel = MethodChannel(plugin.flutterEngine.dartExecutor, "videotrimming")
//        channel.setMethodCallHandler(this);

    }

    override fun onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity();
    }
}
