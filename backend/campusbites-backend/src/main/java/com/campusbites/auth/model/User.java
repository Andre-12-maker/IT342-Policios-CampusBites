package com.campusbites.auth.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
    @Id private String id;
    @Indexed(unique = true) private String email;
    private String  password;
    private String  firstname;
    private String  lastname;
    private Role    role    = Role.CUSTOMER;
    private boolean enabled = true;
    @CreatedDate    private Instant createdAt;
    @LastModifiedDate private Instant updatedAt;

    public enum Role { CUSTOMER, ADMIN }
}