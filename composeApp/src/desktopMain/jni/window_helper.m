#include <jni.h>
#include <Cocoa/Cocoa.h>

// 先声明 dockIconClicked 函数
void dockIconClicked();

JavaVM *jvm;
jobject dockListenerInstance;

// Objective-C 实现的 AppDelegate 类，用于监听 Dock 图标点击
@interface AppDelegate : NSObject <NSApplicationDelegate>
@end

@implementation AppDelegate

// 处理应用通过 Dock 图标被点击时的事件
- (BOOL)applicationShouldHandleReopen:(NSApplication *)sender hasVisibleWindows:(BOOL)flag {
    NSLog(@"Dock icon clicked, application reopened");
    dockIconClicked();  // 调用 JNI 回调函数
    return YES;
}

@end

// 设置 Dock 图标监听器的函数
void setupDockIconListener() {
    // 创建并设置应用委托
    NSApplication *app = [NSApplication sharedApplication];
    AppDelegate *delegate = [[AppDelegate alloc] init];
    [app setDelegate:delegate];

    // 设置应用的激活策略，使其显示在 Dock 中
    [NSApp setActivationPolicy:NSApplicationActivationPolicyRegular];

    // 运行应用循环
    [app run];
}

// 回调函数，调用 Java 方法
void dockIconClicked() {
    if (jvm != NULL) {
        JNIEnv *env;
        jint res = (*jvm)->AttachCurrentThread(jvm, (void**)&env, NULL);
        if (res != JNI_OK) {
            NSLog(@"Failed to attach thread to JVM");
            return;
        }

        // 获取 Java 对象的类和方法
        jclass dockListenerClass = (*env)->GetObjectClass(env, dockListenerInstance);
        if (dockListenerClass == NULL) {
            NSLog(@"Failed to find DockListener class");
            return;
        }

        // 获取 Java 方法 ID
        jmethodID callbackMethod = (*env)->GetMethodID(env, dockListenerClass, "triggerCallback", "()V");
        if (callbackMethod == NULL) {
            NSLog(@"Failed to find triggerCallback method");
            return;
        }

        // 调用 Java 端的回调方法
        (*env)->CallVoidMethod(env, dockListenerInstance, callbackMethod);

        // (*jvm)->DetachCurrentThread(jvm);
    } else {
        NSLog(@"JVM is NULL");
    }
}

// JNI 函数，用于从 Java 代码中初始化监听器
JNIEXPORT void JNICALL Java_com_cw_automaster_dock_WindowHelper_setupDockListener(JNIEnv *env, jobject obj) {
    // 获取当前 JVM 实例
    (*env)->GetJavaVM(env, &jvm);

    // 保存 Java 对象的全局引用，确保在回调时使用
    dockListenerInstance = (*env)->NewGlobalRef(env, obj);

    // 设置 Dock 图标的监听器
    setupDockIconListener();
}

JNIEXPORT void JNICALL Java_com_cw_automaster_dock_WindowHelper_frontWindow(JNIEnv *env, jobject obj) {
    // 获取当前应用实例并激活它
    [NSApp activateIgnoringOtherApps:YES];
}

/*
clang -dynamiclib dock_listener.m -o libdocklistener.dylib \
  -I"$JAVA_HOME/include" \
  -I"$JAVA_HOME/include/darwin" \
  -framework Cocoa
*/
