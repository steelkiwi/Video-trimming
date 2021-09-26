import 'dart:async';
import 'dart:io';

import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';

import 'dart:async';
import 'dart:io';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
class VideoTrimming {
  static const MethodChannel _channel =
  const MethodChannel('plugins.steelkiwi.com/trimmer_video');
  static const KEY_NATIVE = "trim_video";
  static Future<File?> trimVideo({
    required String sourcePath,
    double minSeconds = 1,
    double maxSeconds = 15,
    String mainColorInHex = "#ff0000",
    String handleColorInHex = "#ffffff",
    String positionBarColorInHex = "#ffffff",
    String processingTitle = "Processing!!!",
    String? screenTitle
  }) async {
    assert(sourcePath != null);
    assert(await File(sourcePath).exists());
    final arguments = <String, dynamic>{
      'source_path': sourcePath,
      'min_seconds': minSeconds,
      'max_seconds': maxSeconds,
      'main_color': mainColorInHex,
      'handle_color': handleColorInHex,
      'position_bar_color': positionBarColorInHex,
      'processing_title': processingTitle,
      'screen_title': screenTitle
    };
    final String? resultPath =
    await _channel.invokeMethod(KEY_NATIVE, arguments);
    return resultPath == null ? null : new File(resultPath);
  }
}