package com.coinquylifeteam.auth.Controller;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import model.User;
import org.bson.Document;
import util.MongoDBManager;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {
    private final MongoCollection<Document> users;

    public UserService() {
        MongoDatabase db = MongoDBManager.getDatabase("miodb");

        if (!db.listCollectionNames().into(new java.util.ArrayList<>()).contains("utenti")) {
            db.createCollection("utenti");
        }

        this.users = db.getCollection("utenti");
    }

    public boolean register(String username, String password) {
        if (users.find(new Document("username", username)).first() != null) {
            return false; // Già registrato
        }

        // Hash password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        Document newUser = new Document("username", username)
                .append("password", hashedPassword);

        users.insertOne(newUser);
        return true;
    }

    public boolean login(String username, String password) {
        Document userDoc = users.find(new Document("username", username)).first();

        if (userDoc == null) return false;

        String hashed = userDoc.getString("password");

        return BCrypt.checkpw(password, hashed);
    }
}
A