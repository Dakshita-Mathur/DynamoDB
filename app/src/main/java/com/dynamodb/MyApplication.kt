package com.dynamodb

import android.app.Application
import android.util.Log
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserStateDetails
import java.lang.Exception

class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        AWSMobileClient.getInstance().initialize(applicationContext, object : Callback<UserStateDetails>{
            override fun onResult(result: UserStateDetails?) {
                Log.d(TAG, "AWSMobileClient init onResult: $result")
            }

            override fun onError(e: Exception?) {
                Log.d(TAG, "AWSMobileClient init onError: $e")
            }
        })
    }

    companion object {
        val TAG = "DynamoDB_TAG"
    }
}