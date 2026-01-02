package com.xyz.social_media.response;

public class UserResponseDto {

    private Long userId;
    private String name;
    private Long dob;
    private String profile_img_url;
    private String cover_pic_url;
    private Boolean isOnline;
    private Long lastSeen;

    public UserResponseDto(Long id, String name, Long dob, String profileImgUrl, String coverPicUrl) {
        this.userId = id;
        this.name = name;
        this.dob = dob;
        this.profile_img_url = profileImgUrl;
        this.cover_pic_url = coverPicUrl;
    }
    public UserResponseDto(){

    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_img_url() {
        return profile_img_url;
    }

    public void setProfile_img_url(String profile_img_url) {
        this.profile_img_url = profile_img_url;
    }

    public Long getDob() {
        return dob;
    }

    public void setDob(Long dob) {
        this.dob = dob;
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
