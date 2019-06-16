package json_placeholder_spring_service;

import json_placeholder_model.request.PostRequest;
import json_placeholder_model.response.PostResponse;
import json_placeholder_model.response.PostsResponse;

public class Main {

    public static void main(String[] args) {

        JSONPlaceholderService service = new JSONPlaceholderService();

        // get one post
        PostResponse post = service.getPost("2");

        // get all posts
        PostsResponse posts = service.getAllPosts();

        // create new post
        PostRequest newPost = new PostRequest.Builder()
                .withBody("new post")
                .withTitle("Brand new")
                .withUserId(12)
                .build();
        PostResponse newPostResponse = service.createPost(newPost);

        // update post
        PostRequest updatedPost = new PostRequest.Builder()
                .withBody("updated!")
                .withTitle("updates v2")
                .withUserId(1)
                .build();
        PostResponse updateResponse = service.updatePost("1", updatedPost);

        System.out.println(posts.toString());

    }

}
