package com.xyz.social_media.service;

import com.xyz.social_media.models.Post;
import com.xyz.social_media.repository.PostRepo;
import com.xyz.social_media.requestDto.PostRequestDto;
import com.xyz.social_media.utilities.UtilityHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    private final PostRepo postRepo;
    private final FriendShipService friendShipService;

    // ✅ Update to EC2 directory
    private static final String UPLOAD_DIR = "/home/ec2-user/uploads/";

    @Autowired
    public PostService(PostRepo postRepo, FriendShipService friendShipService) {
        this.postRepo = postRepo;
        this.friendShipService = friendShipService;
    }

    public Post createPost(PostRequestDto postRequestDto, MultipartFile file) {
        Post post = new Post();
        post.setUserId(postRequestDto.getUserId());
        post.setContent(postRequestDto.getContent());
        post.setDescription(postRequestDto.getDescription());
        post.setCreatedAt(UtilityHelper.getCurrentMillis());

        if (file != null && !file.isEmpty()) {
            try (InputStream inputStream = file.getInputStream()) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename().replace(" ", "_");
                String fileType = file.getContentType();
                Path filePath = Paths.get(UPLOAD_DIR, fileName);

                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("File uploaded: " + fileName);
                System.out.println("Detected file type: " + fileType);

                if (fileType != null) {
                    if (fileType.startsWith("image/")) {
                        post.setImageUrl("http://ec2-13-203-205-26.ap-south-1.compute.amazonaws.com:8080/uploads/" + fileName);
                        System.out.println("Image URL set: " + post.getImageUrl());
                    } else if (fileType.startsWith("video/")) {
                        post.setVideoUrl("http://ec2-13-203-205-26.ap-south-1.compute.amazonaws.com:8080/uploads/" + fileName);
                        System.out.println("Video URL set: " + post.getVideoUrl());
                    } else {
                        System.out.println("Unsupported file type: " + fileType);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return postRepo.save(post);
    }

    public List<Post> getAllPostsOfUser(Long userId) {
        return postRepo.getPostsByUserId(userId);
    }

    public List<Post> getAllPostsOfFriendsOfUser(Long userId) {
        List<Long> friendsIds = friendShipService.getFriendsUserIdsByUserId(userId);
        List<Post> feed = new ArrayList<>();
        for (Long x : friendsIds) {
            feed.addAll(getAllPostsOfUser(x));
        }
        return feed;
    }

    public void deletePost(Long postId) {
        postRepo.deletePostById(postId);
    }
}
