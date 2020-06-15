import 'dart:async';
import 'dart:io';

import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';

class VideoTrimming {
  static const MethodChannel _channel = const MethodChannel('plugins.steelkiwi.com/trimmer_video');
  static const KEY_NATIVE = "trim_video";

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<File> trimVideo({
    @required String sourcePath,
  }) async {
    assert(sourcePath != null);
    assert(await File(sourcePath).exists());

    final arguments = <String, dynamic>{
      'source_path': sourcePath,
    };
    final String resultPath =
        await _channel.invokeMethod(KEY_NATIVE, arguments);
    return resultPath == null ? null : new File(resultPath);
  }

  Future<dynamic> _handleMethod(MethodCall call) async {
    switch (call.method) {
      case "message":
        debugPrint(call.arguments);
        return new Future.value("");
    }
  }
}
