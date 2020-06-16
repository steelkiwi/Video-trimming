import Flutter
import UIKit

public class SwiftVideotrimmingPlugin: NSObject, FlutterPlugin {
    
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "plugins.steelkiwi.com/trimmer_video", binaryMessenger: registrar.messenger())
        let instance = SwiftVideotrimmingPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
    }
    
    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard call.method == "trim_video" else {
            result(FlutterMethodNotImplemented)
            return
        }
        guard let arguments = call.arguments as? [String: Any],
            let sourcePath = arguments["source_path"] as? String,
            let maxDurationSeconds = arguments["max_seconds"] as? Int else {
                result(FlutterError(code: "SOURCE_PATH_MISSING", message: "Missing arguments", details: nil))
                return
        }
        let sourcePathURL = URL(fileURLWithPath: sourcePath)
        let vc = VideoEditorVC.instantiateVC(sourceVideoURL: sourcePathURL, maxDurationSeconds: maxDurationSeconds, completion: { outputVideoPath in
            result(outputVideoPath)
        })
        vc.modalPresentationStyle = .fullScreen
        UIApplication.shared.keyWindow?.rootViewController?.present(vc, animated: true)
    }
}
