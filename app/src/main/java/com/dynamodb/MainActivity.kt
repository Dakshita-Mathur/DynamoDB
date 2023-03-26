package com.dynamodb

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.dynamo.db.R
import com.dynamo.db.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.registrationHandler = this
    }

    fun onClick(view: View) {
        when(view.id){
            R.id.btnSubmit -> registerUserOnDynamoDb()
        }
    }

    fun registerUserOnDynamoDb() {
        CoroutineScope(Dispatchers.IO).launch {
            val userDataEntity = mapOf(
                "_id" to AttributeValue("${binding.etName.text}+${System.currentTimeMillis()}"),
                "userName" to AttributeValue("${binding.etName.text}"),
                "email" to AttributeValue("${binding.etEmail.text}"),
                "password" to AttributeValue("${binding.etPassword.text}"),
                "confirmPassword" to AttributeValue("${binding.etConfirmPassword.text}")
            )

            Log.d("DynamoDB_TAG", "onCreate userDataEntity: $userDataEntity")
            val database = DynamoDatabase(application)
            database.createNewTableAndInsertData(tableName = "RegistrationEntity",
                readCapacityUnits = 10,
                writeCapacityUnits= 10,
                putItems = userDataEntity,
                primaryKeyOfTable = "_id"
            )
        }
    }
}