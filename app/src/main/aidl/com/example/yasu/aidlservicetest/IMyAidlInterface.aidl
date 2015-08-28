// IMyAidlInterface.aidl
package com.example.yasu.aidlservicetest;

// Declare any non-default types here with import statements

interface IMyAidlInterface {
    oneway void doSomething(int i);
    void setString(in String s);
}
