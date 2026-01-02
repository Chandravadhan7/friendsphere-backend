package com.xyz.social_media.models;

import javax.persistence.*;

@Entity
@Table(name = "user_keys")
public class UserKey {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Lob
    @Column(name = "public_key", nullable = false, columnDefinition = "TEXT")
    private String publicKey; // base64 encoded SPKI

    public UserKey() {}

    public UserKey(Long userId, String publicKey) {
        this.userId = userId;
        this.publicKey = publicKey;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
