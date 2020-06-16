import Flutter
import UIKit

public class SwiftVideotrimmingPlugin: NSObject, FlutterPlugin {
    
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "videotrimming", binaryMessenger: registrar.messenger())
        let instance = SwiftVideotrimmingPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
    }
    
    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard call.method == "trim_video" else {
            result(FlutterMethodNotImplemented)
            return
        }
        let sourcePathArgument = "source_path"
        guard let sourcePath = (call.arguments as? [String: Any])?[sourcePathArgument] as? String else {
            result(FlutterError(code: "SOURCE_PATH_MISSING", message: "Missing \(sourcePathArgument) argument", details: nil))
            return
        }
        let sourcePathURL = URL(fileURLWithPath: sourcePath)
        let vc = VideoEditorVC.instantiateVC(sourceVideoURL: sourcePathURL, completion: { outputVideoPath in
            result(outputVideoPath)
        })
        vc.modalPresentationStyle = .fullScreen
        UIApplication.shared.keyWindow?.rootViewController?.present(vc, animated: true)
    }
}
