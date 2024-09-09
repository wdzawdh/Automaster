#import <Cocoa/Cocoa.h>
#include <jni.h>

JavaVM *jvm;
jclass cls;
jmethodID methodID;

// 定义一个 NSApplicationDelegate 类
@interface DockDelegate : NSObject <NSApplicationDelegate>
@end

@implementation DockDelegate

// 当 Dock 图标被点击时，这个方法会被调用
- (void)applicationDidBecomeActive:(NSNotification *)notification {
    JNIEnv *env;
    // Attach JVM 到当前线程
    (*jvm)->AttachCurrentThread(jvm, (void**)&env, NULL);

    // 调用 Java 方法
    (*env)->CallStaticVoidMethod(env, cls, methodID);

    // Detach 线程
    (*jvm)->DetachCurrentThread(jvm);
}

@end

// 这个函数用于设置 Dock 栏的监听
void setupDockClickListener() {
    // 获取应用程序实例
    NSApplication *app = [NSApplication sharedApplication];

    // 创建并设置委托
    DockDelegate *delegate = [[DockDelegate alloc] init];
    [app setDelegate:delegate];

    // 激活应用
    [NSApp setActivationPolicy:NSApplicationActivationPolicyRegular];
    [NSApp activateIgnoringOtherApps:YES];
}

JNIEXPORT void JNICALL Java_com_cw_automaster_dock_DockListener_setupDockListener(JNIEnv *env, jclass jCls) {
    // 初始化 JVM 和 Java 类的引用
    (*env)->GetJavaVM(env, &jvm);
    cls = (jclass)(*env)->NewGlobalRef(env, jCls);
    methodID = (*env)->GetStaticMethodID(env, cls, "onDockIconClick", "()V");

    // 调用设置 Dock 监听器的方法
    setupDockClickListener();
}