//
// Created by 21shi on 09-03-2021.
//

#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_aapanavyapar_aapanavyapar_MainActivity_getNativeKey(JNIEnv *env, jobject instance) {
    return (*env)->NewStringUTF(env, "ZmRmZHNiJipoM3VoZmRza2p3cmh1ZmRzOTk4QWlod2lodmJqZmpoaXVyMjczMndlZml1aHNkN2U5OGZkc2E=");
}

JNIEXPORT jstring JNICALL
Java_com_aapanavyapar_aapanavyapar_BuyingActivity_getNativeKeyRazorPay(JNIEnv *env, jobject thiz) {
    return (*env)->NewStringUTF(env, "cnpwX3Rlc3RfQnhDWTRJV0k3bGJEQlA=");
}

JNIEXPORT jstring JNICALL
Java_com_aapanavyapar_aapanavyapar_BuyingActivity_getNativeAPIKey(JNIEnv *env, jobject thiz) {
    return (*env)->NewStringUTF(env, "ZmRmZHNiJipoM3VoZmRza2p3cmh1ZmRzOTk4QWlod2lodmJqZmpoaXVyMjczMndlZml1aHNkN2U5OGZkc2E=");
}

JNIEXPORT jstring JNICALL
Java_com_aapanavyapar_aapanavyapar_ViewProvider_getNativeKeyRazorPay(JNIEnv *env, jobject thiz) {
    return (*env)->NewStringUTF(env, "cnpwX3Rlc3RfQnhDWTRJV0k3bGJEQlA=");
}