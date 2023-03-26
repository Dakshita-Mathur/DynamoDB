package com.dynamodb

import android.content.Context
import android.util.Log
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.model.*

class DynamoDatabase(context: Context) {

    private var credentialsProvider : CognitoCachingCredentialsProvider ?= null
    private var dynamoDBClient : AmazonDynamoDBClient ?= null
    private val TAG = "DynamoDB_TAG"
    private val identityPoolId: String = "<Enter your identity pool Id>"

    init {
        // Create a credentials provider with your AWS Cognito credentials
        credentialsProvider = CognitoCachingCredentialsProvider(
            context,
            identityPoolId,
            Regions.US_EAST_1
        )

        // Create a DynamoDB client instance
        dynamoDBClient = AmazonDynamoDBClient(credentialsProvider)
    }

    /**
     * This methode is use to create new Table and also insert data into that table
     */
    suspend fun createNewTableAndInsertData(
        tableName: String,
        readCapacityUnits: Long,
        writeCapacityUnits: Long,
        putItems: Map<String, AttributeValue>,
        primaryKeyOfTable: String
    ) {
        try {
            // Define the table schema
            // An array of attributes that describe the key schema for the table and indexes.
            val tableAttributeDefinitions = mutableListOf(
                AttributeDefinition(primaryKeyOfTable, ScalarAttributeType.S)
            )

            // Specifies the attributes that make up the primary key for a table or an index.
            val tableKeySchema = mutableListOf(
                KeySchemaElement(primaryKeyOfTable, KeyType.HASH)
            )

            val createTableRequest = CreateTableRequest()
                .withTableName(tableName)
                .withAttributeDefinitions(tableAttributeDefinitions)
                .withKeySchema(tableKeySchema)
                .withProvisionedThroughput(
                    ProvisionedThroughput(readCapacityUnits, writeCapacityUnits)
                )

            // Create the table
            val createTableResult = dynamoDBClient?.createTable(createTableRequest)

            // Insert data into the table
            val putItemRequest: PutItemRequest = PutItemRequest()
                .withTableName(tableName)
                .withItem(putItems)
            val putItemResult: PutItemResult? = dynamoDBClient?.putItem(putItemRequest)
        } catch (ex: java.lang.Exception) {
            Log.d(TAG, "createNewTableAndInsertData Exception: $ex")
            ex.printStackTrace()
        }
    }

    /**
     * This method is responsible to create new table only
     */
    suspend fun createNewTable(
        tableName: String,
        readCapacityUnits: Long,
        writeCapacityUnits: Long,
        primaryKeyOfTable: String
    ) {
        try {
            // Define the table schema
            // An array of attributes that describe the key schema for the table and indexes.
            val tableAttributeDefinitions = mutableListOf(
                AttributeDefinition(primaryKeyOfTable, ScalarAttributeType.S)
            )

            // Specifies the attributes that make up the primary key for a table or an index.
            val tableKeySchema = mutableListOf(
                KeySchemaElement(primaryKeyOfTable, KeyType.HASH)
            )

            val createTableRequest = CreateTableRequest()
                .withTableName(tableName)
                .withAttributeDefinitions(tableAttributeDefinitions)
                .withKeySchema(tableKeySchema)
                .withProvisionedThroughput(
                    ProvisionedThroughput(readCapacityUnits, writeCapacityUnits)
                )

            val createTableResult = dynamoDBClient?.createTable(createTableRequest)
        }catch (ex: java.lang.Exception) {
            Log.d(TAG, "createNewTable exception: $ex")
        }
    }

    /**
     * This method is responsible to insert item in given table name
     */
    suspend fun insertData(tableName: String, putItems: HashMap<String, AttributeValue>) {
        val putItemRequest: PutItemRequest = PutItemRequest()
            .withTableName(tableName)
            .withItem(putItems)

        val putItemResult: PutItemResult? = dynamoDBClient?.putItem(putItemRequest)
    }
}