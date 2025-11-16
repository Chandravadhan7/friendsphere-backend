package com.xyz.social_media.controller;

import com.xyz.social_media.models.UserKey;
import com.xyz.social_media.repository.UserKeyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/keys")
public class KeyController {

    private final UserKeyRepo userKeyRepo;

    @Autowired
    public KeyController(UserKeyRepo userKeyRepo) {
        this.userKeyRepo = userKeyRepo;
    }

    @PostMapping()
    public ResponseEntity<UserKey> uploadPublicKey(@RequestBody UserKey body) {
        // upsert
        UserKey existing = userKeyRepo.findByUserId(body.getUserId());
        if (existing != null) {
            existing.setPublicKey(body.getPublicKey());
            UserKey saved = userKeyRepo.save(existing);
            return new ResponseEntity<>(saved, HttpStatus.OK);
        }
        UserKey saved = userKeyRepo.save(body);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserKey> getPublicKey(@PathVariable Long userId) {
        UserKey key = userKeyRepo.findByUserId(userId);
        if (key == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(key, HttpStatus.OK);
    }
}
