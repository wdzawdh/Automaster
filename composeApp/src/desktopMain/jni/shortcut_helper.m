#import <Carbon/Carbon.h>
#import <Cocoa/Cocoa.h>
#import <jni.h>

// 保存多个快捷键引用的字典
NSMutableDictionary *hotKeyRefs;
JavaVM *jvm;
jobject globalListenerObject;

// 键盘事件处理回调
OSStatus hotKeyHandler(EventHandlerCallRef nextHandler, EventRef anEvent, void *userData) {
    if (jvm != NULL) {
        EventHotKeyID hotKeyID;
        GetEventParameter(anEvent, kEventParamDirectObject, typeEventHotKeyID, NULL, sizeof(hotKeyID), NULL, &hotKeyID);
        JNIEnv *env;
        jint res = (*jvm)->AttachCurrentThread(jvm, (void **)&env, NULL);
        if (res != JNI_OK) {
            NSLog(@"Failed to attach thread to JVM");
        }
        // 调用 JVM 中的回调方法
        jclass listenerClass = (*env)->GetObjectClass(env, globalListenerObject);
        jmethodID onHotKeyPressed = (*env)->GetMethodID(env, listenerClass, "onHotKeyPressed", "(I)V");
        if (onHotKeyPressed) {
            (*env)->CallVoidMethod(env, globalListenerObject, onHotKeyPressed, hotKeyID.id);
        }
        return noErr;
    } else {
        NSLog(@"JVM is NULL");
        return prInitErr;
    }
}

// 注册快捷键
JNIEXPORT jint JNICALL Java_com_cw_automaster_shortcut_ShortcutHelper_registerHotKey(JNIEnv *env, jobject obj, jint keyCode, jint modifiers) {
    // 获取当前 JVM 实例
    if (jvm == NULL) {
        (*env)->GetJavaVM(env, &jvm);
    }
    // 获取全局 Java 对象引用，以便在回调中使用
    if (globalListenerObject == NULL) {
        globalListenerObject = (*env)->NewGlobalRef(env, obj);
    }
    // 初始化字典
    if (hotKeyRefs == nil) {
        hotKeyRefs = [[NSMutableDictionary alloc] init];
    }

    static int currentHotKeyID = 1;
    OSStatus status;
    EventHotKeyID hotKeyID;
    EventTypeSpec eventType;

    hotKeyID.signature = 'htk1';
    hotKeyID.id = currentHotKeyID++;

    eventType.eventClass = kEventClassKeyboard;
    eventType.eventKind = kEventHotKeyPressed;

    // 安装事件处理器
    InstallApplicationEventHandler(&hotKeyHandler, 1, &eventType, NULL, NULL);

    // 注册快捷键
    EventHotKeyRef hotKeyRef;
    status = RegisterEventHotKey(keyCode, modifiers, hotKeyID, GetApplicationEventTarget(), 0, &hotKeyRef);

    if (status == noErr) {
        [hotKeyRefs setObject:[NSValue valueWithPointer:hotKeyRef] forKey:@(hotKeyID.id)];
        return hotKeyID.id;  // 返回唯一ID
    }
    return -1;  // 注册失败
}

// 注销快捷键
JNIEXPORT void JNICALL Java_com_cw_automaster_shortcut_ShortcutHelper_unregisterHotKey(JNIEnv *env, jobject obj, jint hotKeyID) {
    NSValue *hotKeyRefValue = [hotKeyRefs objectForKey:@(hotKeyID)];
    if (hotKeyRefValue != nil) {
        EventHotKeyRef hotKeyRef = [hotKeyRefValue pointerValue];
        UnregisterEventHotKey(hotKeyRef);
        [hotKeyRefs removeObjectForKey:@(hotKeyID)];
    }
}