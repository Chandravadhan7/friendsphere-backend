package com.xyz.social_media.models;

import javax.persistence.*;

@Entity
@Table(name = "`user`")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;
   private String name;
   private String email;
   private String password;
   private Long dob;
   private String profile_img_url;
   private String cover_pic_url;
   
   @Column(name = "is_online")
   private Boolean isOnline;
   
   @Column(name = "last_seen")
   private Long lastSeen;

    public User(Long id, String name, String email, String password, String profile_img_url, Long dob, String cover_pic_url) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.profile_img_url = profile_img_url;
        this.dob = dob;
        this.cover_pic_url = cover_pic_url;
        this.isOnline = false;
        this.lastSeen = System.currentTimeMillis();
    }

    public User(){

    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getDob() {
        return dob;
    }

    public void setDob(Long dob) {
        this.dob = dob;
    }

    public String getProfile_img_url() {
        return profile_img_url;
    }

    public void setProfile_img_url(String profile_img_url) {
        this.profile_img_url = profile_img_url;
    }

    public String getCover_pic_url() {
        return cover_pic_url;
    }

    public void setCover_pic_url(String cover_pic_url) {
        this.cover_pic_url = cover_pic_url;
    }

    public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

    public Long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Long lastSeen) {
        this.lastSeen = lastSeen;
    }
}
