#import "VideotrimmingPlugin.h"
#if __has_include(<videotrimming/videotrimming-Swift.h>)
#import <videotrimming/videotrimming-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "videotrimming-Swift.h"
#endif

@implementation VideotrimmingPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftVideotrimmingPlugin registerWithRegistrar:registrar];
}
@end
