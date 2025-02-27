package com.bank.msauthentication.entity;

import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class UserEntity {
    @BsonId
    private String id;
    private String username;
    private String password;
    private String role;
}

