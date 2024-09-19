#import <Foundation/Foundation.h>
#import <ApplicationServices/ApplicationServices.h>
#import <Cocoa/Cocoa.h>
#import <jni.h>

// 检查是否获得辅助功能权限
JNIEXPORT jboolean Java_com_cw_automaster_permission_AccessibilityHelper_hasAccessibilityPermission(JNIEnv *env, jclass clazz) {
    AXUIElementRef systemWideElement = AXUIElementCreateSystemWide();
    CFErrorRef error = NULL;
    BOOL isAuthorized = AXIsProcessTrustedWithOptions((__bridge CFDictionaryRef)@{(NSString *)kAXTrustedCheckOptionPrompt: @NO});
    CFRelease(systemWideElement);
    return (jboolean)isAuthorized;
}

// 打开辅助功能设置
JNIEXPORT void JNICALL Java_com_cw_automaster_permission_AccessibilityHelper_openAccessibilitySettings(JNIEnv *env, jclass clazz) {
    NSURL *url = [NSURL URLWithString:@"x-apple.systempreferences:com.apple.preference.security?Privacy_Accessibility"];
    if ([[NSWorkspace sharedWorkspace] openURL:url]) {
        NSLog(@"Successfully opened Accessibility Settings.");
    } else {
        NSLog(@"Failed to open Accessibility Settings.");
    }
}

/*
clang -dynamiclib accessibility_helper.m -o libaccessibility_helper.dylib \
  -I"$JAVA_HOME/include" \
  -I"$JAVA_HOME/include/darwin" \
  -framework Cocoa
*/