import 'dart:io';

import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:video_player/video_player.dart';
import 'package:videotrimming/video_trimming.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: 'Flutter-Native Bridging',
      theme: new ThemeData(
        primarySwatch: Colors.green,
      ),
      home: new MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
//  final String title;

//  MyHomePage({Key key, this.title}) : super(key: key) {
//    platform.setMethodCallHandler(_handleMethod);
//  }

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  VideoPlayerController _controller;

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("Trimmer"),
      ),
      body: new Center(
        child: new Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            _controller == null
                ? Text("Video did not select")
                : Container(
                    child: VideoPlayer(_controller),
                    height: 200,
                    width: 200,
                  ),
            new RaisedButton(
              child: new Text('Move to Native World!'),
              onPressed: () {
                _pickVideo();
              },
            ),
          ],
        ),
      ),
    );
  }

  Future<dynamic> _handleMethod(MethodCall call) async {
    switch (call.method) {
      case "message":
        debugPrint(call.arguments);
        return new Future.value("");
    }
  }

  _pickVideo() async {
    if (await Permission.storage.request().isGranted) {
      File file = await FilePicker.getFile(type: FileType.video);

      print("Selected video");
      var fileNew = await VideoTrimming.trimVideo(sourcePath: file.path);

      _controller = VideoPlayerController.file(fileNew);

      _controller.setLooping(true);
      _controller.initialize().then((_) => setState(() {}));
      _controller.play();
      print("Trimed video: " + fileNew.path);
    }

// You can request multiple permissions at once.
    Map<Permission, PermissionStatus> statuses = await [
      Permission.storage,
    ].request();
    print(statuses[Permission.location]);

//    _videoPlayerController = VideoPlayerController.file(_video)..initialize().then((_) {
//      setState(() { });
//      _videoPlayerController.play();
//    });
  }
}
