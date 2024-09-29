#import <jni.h>
#import <Cocoa/Cocoa.h>
#import <ServiceManagement/ServiceManagement.h>

// 检查是否获得辅助功能权限
JNIEXPORT jboolean Java_com_cw_automaster_permission_PermissionHelper_hasAccessibilityPermission(JNIEnv *env, jobject obj) {
    AXUIElementRef systemWideElement = AXUIElementCreateSystemWide();
    CFErrorRef error = NULL;
    BOOL isAuthorized = AXIsProcessTrustedWithOptions((__bridge CFDictionaryRef)@{(NSString *)kAXTrustedCheckOptionPrompt: @NO});
    CFRelease(systemWideElement);
    return (jboolean)isAuthorized;
}

// 打开辅助功能设置
JNIEXPORT void JNICALL Java_com_cw_automaster_permission_PermissionHelper_openAccessibilitySettings(JNIEnv *env, jobject obj) {
    NSURL *url = [NSURL URLWithString:@"x-apple.systempreferences:com.apple.preference.security?Privacy_Accessibility"];
    if ([[NSWorkspace sharedWorkspace] openURL:url]) {
        NSLog(@"Successfully opened Accessibility Settings.");
    } else {
        NSLog(@"Failed to open Accessibility Settings.");
    }
}

// 添加辅助应用到登录项
JNIEXPORT void JNICALL Java_com_cw_automaster_permission_PermissionHelper_addLoginItem(JNIEnv *env, jobject obj) {
    if (@available(macOS 13.0, *)) {
        NSError *error=nil;
        // 获取 loginItem 服务
        [SMAppService.mainAppService registerAndReturnError:&error];
        if (error) {
            NSLog(@"Error setting registerAndReturnError : %@", error);
        }
    } else {
        NSLog(@"macOS version is lower than 13.0. SMAppService is unavailable.");
    }
}

// 从登录项中移除辅助应用
JNIEXPORT void JNICALL Java_com_cw_automaster_permission_PermissionHelper_removeLoginItem(JNIEnv *env, jobject obj) {
    if (@available(macOS 13.0, *)) {
        NSError *error=nil;
        // 获取 loginItem 服务
        [SMAppService.mainAppService unregisterAndReturnError:&error];
        if (error) {
            NSLog(@"Error setting unregisterAndReturnError : %@", error);
        }
    } else {
        NSLog(@"macOS version is lower than 13.0. SMAppService is unavailable.");
    }
}

/*
clang -dynamiclib permission_helper.m -o permission_helper.dylib \
  -I"$JAVA_HOME/include" \
  -I"$JAVA_HOME/include/darwin" \
  -framework Cocoa
  -framework ServiceManagement
*/