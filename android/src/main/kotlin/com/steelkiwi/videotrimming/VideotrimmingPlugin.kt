package com.steelkiwi.videotrimming

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat.startActivityForResult

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import java.io.File

/** VideotrimmingPlugin */
public class VideotrimmingPlugin: FlutterPlugin, MethodCallHandler, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private var activityPluginBinding: ActivityPluginBinding? = null

  override fun onAttachedToEngine(@NonNull plugin: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(plugin.getFlutterEngine().getDartExecutor(), "videotrimming")
    channel.setMethodCallHandler(this);
//    activityPluginBinding.addActivityResultListener(delegate);
  }

  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
  // plugin registration via this function while apps migrate to use the new Android APIs
  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
  //
  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
  // in the same class.
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
//      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    } else {
      result.notImplemented()
    }
  }
//
  private fun startTrimActivity(uri: Uri) {
    val intent = Intent(activityPluginBinding!!.activity, TestActivity::class.java)
    intent.putExtra("path", uri)//        startActivityForResult(intent, 500)
    activityPluginBinding?.activity?.startActivityForResult(intent,500)
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
    this.activityPluginBinding = p0;  }

  override fun onDetachedFromActivityForConfigChanges() {
    TODO("Not yet implemented")
  }
}
