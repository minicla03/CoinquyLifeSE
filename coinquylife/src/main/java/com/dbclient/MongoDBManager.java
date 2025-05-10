package com.dbclient;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBManager
{
    private static MongoClient mongoClient = null;

    private static void init()
    {
        if (mongoClient == null)
        {
            mongoClient = MongoClients.create("mongodb://localhost:27017");
        }
    }

    public static MongoDatabase getDatabase(String dbName)
    {
        init();
        return mongoClient.getDatabase(dbName);
    }

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
