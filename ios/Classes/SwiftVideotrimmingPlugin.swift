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
            let minDurationSeconds = arguments["min_seconds"] as? Double,
            let maxDurationSeconds = arguments["max_seconds"] as? Double else {
                result(FlutterError(code: "SOURCE_PATH_MISSING", message: "Missing arguments", details: nil))
                return
        }
        let screenTitle = arguments["screen_title"] as? String
        guard let rootVC = UIApplication.shared.keyWindow?.rootViewController else { return }
        NavigationManager.showVideoEditorVC(fromVC: rootVC,
                                            sourcePathURL: URL(fileURLWithPath: sourcePath),
                                            minDurationSeconds: minDurationSeconds,
                                            maxDurationSeconds: maxDurationSeconds,
                                            screenTitle: screenTitle) { (outputVideoPath) in
                                                result(outputVideoPath)
        }
    }
}
