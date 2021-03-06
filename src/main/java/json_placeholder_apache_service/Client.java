package json_placeholder_apache_service;

import gson_json.json.JsonHelper;
import json_placeholder_model.request.CommentRequest;
import json_placeholder_model.request.PostRequest;
import json_placeholder_model.response.CommentResponse;
import json_placeholder_model.response.PhotoResponse;
import json_placeholder_model.response.PostResponse;
import json_placeholder_model.response.ToDo;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

public class Client {

    private static final String HOST = "jsonplaceholder.typicode.com";
    private static final String TO_DO_RESOURCE_URL = "/todos/";
    private static final String POST_RESOURCE_URL = "/posts";
    private static final String COMMENT_RESOURCE_URL = "/comments/";
    private static final String PHOTO_RESOURCE_URL = "/photos";
    private HttpHost host = new HttpHost(HOST);
    private JsonHelper jsonHelper = new JsonHelper();

    private String sendRequest(HttpRequest request) {

        String json = null;

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {

            HttpResponse response = client.execute(host, request);
            HttpEntity entity = response.getEntity();
            json = EntityUtils.toString(entity);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;

    }

    public ToDo getToDosResponse(String toDoItemIndex) {
        HttpGet getRequest = new HttpGet(TO_DO_RESOURCE_URL + toDoItemIndex);
        String response = sendRequest(getRequest);
        return jsonHelper.parseJsonToObject(response, ToDo.class);
    }

    public List<ToDo> getToDosResponse() {
        HttpGet getRequest = new HttpGet(TO_DO_RESOURCE_URL);
        String response = sendRequest(getRequest);
        ToDo[] toDos = jsonHelper.parseJsonToObject(response, ToDo[].class);
        return Arrays.asList(toDos);
    }

    public PostResponse createNewPost(PostRequest body) {
        String json = jsonHelper.parseObjectToJson(body);
        String response = null;
        try {

            StringEntity entity = new StringEntity(json);
            HttpPost postRequest = new HttpPost();
            postRequest.setEntity(entity);
            postRequest.setURI(URI.create(POST_RESOURCE_URL));
            postRequest.setHeader("Content-type", "application/json; charset=UTF-8");
            response = sendRequest(postRequest);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return jsonHelper.parseJsonToObject(response, PostResponse.class);
    }

    public List<PostResponse> getPostsForUserId(String userId) {
        HttpGet getRequest = new HttpGet();
        getRequest.setURI(URI.create(POST_RESOURCE_URL + "?userId=" + userId));
        String response = sendRequest(getRequest);
        PostResponse[] posts = jsonHelper.parseJsonToObject(response, PostResponse[].class);
        return Arrays.asList(posts);
    }

    public CommentResponse updateComment(CommentRequest request) {
        String json = jsonHelper.parseObjectToJson(request);
        String response = null;

        try {

            StringEntity entity = new StringEntity(json);
            HttpPut putRequest = new HttpPut();
            putRequest.setEntity(entity);
            putRequest.setURI(URI.create(COMMENT_RESOURCE_URL + request.getId()));
            putRequest.setHeader("Content-type", "application/json; charset=UTF-8");
            response = sendRequest(putRequest);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return jsonHelper.parseJsonToObject(response, CommentResponse.class);
    }

    public String deleteComment(String commentId) {
        HttpDelete deleteRequest = new HttpDelete();
        deleteRequest.setURI(URI.create(COMMENT_RESOURCE_URL + commentId));
        deleteRequest.setHeader("Content-type", "application/json; charset=UTF-8");
        return sendRequest(deleteRequest);
    }

    public List<CommentResponse> getCommentsForPost(String postId) {
        HttpGet getRequest = new HttpGet(POST_RESOURCE_URL + "/" + postId + "/comments");
        String response = sendRequest(getRequest);
        CommentResponse[] comments = jsonHelper.parseJsonToObject(response, CommentResponse[].class);
        return Arrays.asList(comments);
    }

    public PhotoResponse getPhoto(String photoId) {
        HttpGet getRequest = new HttpGet(PHOTO_RESOURCE_URL + "/" + photoId);
        String response = sendRequest(getRequest);
        return jsonHelper.parseJsonToObject(response, PhotoResponse.class);
    }

}
