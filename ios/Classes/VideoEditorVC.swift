//
//  VideoEditorVC.swift
//  videotrimming
//
//  Created by Igor on 6/16/20.
//

import UIKit
import AVFoundation
import PryntTrimmerView
import Photos

class VideoEditorVC: UIViewController, StoryboardInstatiatable {
    
    @IBOutlet private weak var playerView: UIView!
    @IBOutlet private weak var trimTimeLabelsView: UIStackView!
    @IBOutlet private weak var startTrimTimeLabel: UILabel!
    @IBOutlet private weak var endTrimTimeLabel: UILabel!
    @IBOutlet private weak var trimmerView: TrimmerView!
    @IBOutlet private weak var doneButton: UIBarButtonItem!
    
    static var storyboardName: StoryboardName { .videoEditor }
    
    private var player: AVPlayer?
    
    private var sourceVideoURL: URL!
    private var minDurationSeconds: Double!
    private var maxDurationSeconds: Double!
    private var completion: ((String) -> ())?
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        loadAsset()
    }
    
    static func instantiateVC(sourceVideoURL: URL,
                              minDurationSeconds: Double,
                              maxDurationSeconds: Double,
                              completion: @escaping (String) -> ()) -> VideoEditorVC {
        let vc = VideoEditorVC.instantiateVC()
        vc.sourceVideoURL = sourceVideoURL
        vc.minDurationSeconds = minDurationSeconds
        vc.maxDurationSeconds = maxDurationSeconds
        vc.completion = completion
        return vc
    }
    
    private func loadAsset() {
        guard player == nil else { return }
        DispatchQueue.global().async {
            let asset = AVAsset(url: self.sourceVideoURL)
            DispatchQueue.main.async {
                self.trimmerView.minDuration = self.minDurationSeconds
                self.trimmerView.maxDuration = self.maxDurationSeconds
                self.trimmerView.asset = asset
                self.trimmerView.delegate = self
                self.updateTrimTimeLabelsUI()
                self.addVideoPlayer(with: asset, playerView: self.playerView)
                self.doneButton.isEnabled = true
            }
        }
    }
    
    private func addVideoPlayer(with asset: AVAsset, playerView: UIView) {
        let playerItem = AVPlayerItem(asset: asset)
        player = AVPlayer(playerItem: playerItem)
        let layer: AVPlayerLayer = AVPlayerLayer(player: player)
        layer.backgroundColor = UIColor.white.cgColor
        layer.frame = CGRect(x: 0, y: 0, width: playerView.frame.width, height: playerView.frame.height)
        layer.videoGravity = AVLayerVideoGravity.resizeAspectFill
        playerView.layer.sublayers?.forEach({$0.removeFromSuperlayer()})
        playerView.layer.addSublayer(layer)
    }
    
    private func presentError(message: String) {
        let alert = UIAlertController(title: "Error", message: message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "Close", style: .cancel, handler: nil))
        present(alert, animated: true)
    }
    
    private func presentError(error: Error) {
        presentError(message: error.localizedDescription)
    }
    
    private func trimVideo() {
        guard let sourceURL = (player?.currentItem?.asset as? AVURLAsset)?.url,
            let startTime = trimmerView.startTime,
            let endTime = trimmerView.endTime else { return }
        
        let fileManager = FileManager.default
        let documentDirectory = fileManager.urls(for: .documentDirectory, in: .userDomainMask)[0]

        let asset = AVAsset(url: sourceURL)

        var outputURL = documentDirectory.appendingPathComponent("output")
        do {
            try fileManager.createDirectory(at: outputURL, withIntermediateDirectories: true, attributes: nil)
            outputURL = outputURL.appendingPathComponent("\(sourceURL.lastPathComponent).mp4")
        } catch {
            presentError(error: error)
        }

        // Remove existing file
        try? fileManager.removeItem(at: outputURL)

        guard let exportSession = AVAssetExportSession(asset: asset, presetName: AVAssetExportPresetHighestQuality) else { return }
        exportSession.outputURL = outputURL
        exportSession.outputFileType = .mp4

        let timeRange = CMTimeRange(start: startTime,
                                    end: endTime)
        
        exportSession.timeRange = timeRange
        doneButton.isEnabled = false
        exportSession.exportAsynchronously {
            DispatchQueue.main.async {
                self.doneButton.isEnabled = true
                switch exportSession.status {
                case .completed:
                    self.completion?(outputURL.absoluteString)
                    self.dismiss(animated: true)
                case .failed:
                    self.presentError(message: "Trimming failed")
                default: break
                }
            }
        }
    }
    
    private func updateTrimTimeLabelsUI() {
        trimTimeLabelsView.isHidden = false
        startTrimTimeLabel.text = trimmerView.startTime?.formatted
        endTrimTimeLabel.text = trimmerView.endTime?.formatted
    }
    
    @IBAction private func cancelTapped(_ sender: Any) {
        dismiss(animated: true)
    }
    
    @IBAction private func doneTapped(_ sender: Any) {
        trimVideo()
    }
}

extension VideoEditorVC: TrimmerViewDelegate {
    
    func didChangePositionBar(_ playerTime: CMTime) {
        player?.seek(to: playerTime, toleranceBefore: CMTime.zero, toleranceAfter: CMTime.zero)
        updateTrimTimeLabelsUI()
    }
    
    func positionBarStoppedMoving(_ playerTime: CMTime) {
        player?.seek(to: playerTime, toleranceBefore: CMTime.zero, toleranceAfter: CMTime.zero)
    }
}

extension CMTime {
    
    var formatted: String {
        let totalSeconds = Int(CMTimeGetSeconds(self))
        let hours = totalSeconds / 3600
        let minutes = totalSeconds % 3600 / 60
        let seconds = (totalSeconds % 3600) % 60

        if hours > 0 {
            return String(format: "%i:%02i:%02i", hours, minutes, seconds)
        } else {
            return String(format: "%02i:%02i", minutes, seconds)
        }
    }
}
