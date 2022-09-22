import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
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
  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  VideoPlayerController? _controller;
  String selectedPath = "";
  String trimmedPath = "";

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
            Text("Selected file: $selectedPath"),
            Text("Trimmed file: $trimmedPath"),
            _controller == null
                ? Text("Video did not select")
                : Container(
                    child: VideoPlayer(_controller!),
                    height: 200,
                    width: 200,
                  ),
            new GestureDetector(
              child: new Text('Select video'),
              onTap: () {
                _pickVideo();
              },
            ),
          ],
        ),
      ),
    );
  }

  _pickVideo() async {
    if (await Permission.storage.request().isGranted) {
      FilePickerResult? selectedFile = await FilePicker.platform.pickFiles(type: FileType.video);

      selectedPath = selectedFile?.files.single.path ?? "";
      var trimmedFile = await VideoTrimming.trimVideo(sourcePath: selectedPath);
      trimmedPath = trimmedFile?.path ?? "";

      if (trimmedFile != null) {
        _controller = VideoPlayerController.file(trimmedFile);
        _controller!.setLooping(true);
        _controller!.initialize().then((_) => setState(() {}));
        _controller!.play();
      }

      Map<Permission, PermissionStatus> statuses = await [
        Permission.storage,
      ].request();
      print(statuses[Permission.location]);
    }
  }
}
