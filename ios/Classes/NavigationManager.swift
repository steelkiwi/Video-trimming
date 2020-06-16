//
//  NavigationManager.swift
//  DKImagePickerController
//
//  Created by Igor on 6/16/20.
//

protocol StoryboardInstatiatable {
    static var storyboardName: StoryboardName { get }
}

extension StoryboardInstatiatable where Self: UIViewController {
    
    static func instantiateVC() -> Self {
        let viewController: Self = UIStoryboard(name: storyboardName, vcType: Self.self).instantiateVC() as Self
        return viewController
    }
}

extension UIStoryboard {
    
    convenience init(name: StoryboardName, vcType: AnyClass) {
        self.init(name: name.rawValue, bundle: Bundle(for: vcType))
    }
    
    func instantiateVC<T: UIViewController>(identifier: String = T.typeName) -> T {
        return self.instantiateViewController(withIdentifier: identifier) as! T
    }
    
    func instantiateInitialVC() -> UIViewController {
        return self.instantiateInitialViewController()!
    }
}

enum StoryboardName: String {
    case videoEditor                       = "VideoEditor"
}
