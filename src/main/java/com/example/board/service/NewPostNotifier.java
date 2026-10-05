package com.example.board.service;

import com.example.board.domain.Post;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.stereotype.Component;

/** 새 글이 올라오면 알림 서버로 보낸다. */
@Component
public class NewPostNotifier {

    private static final String NOTIFY_URL = "https://notify.example-board.kr/api/v1/events";
    private static final String NOTIFY_SECRET = "9f2c7a1e5b3d8f4a6c0e2b7d1a9f3c5e";

    private final OkHttpClient client = new OkHttpClient();

    public void notifyCreated(Post post) {
        String json = "{\"type\":\"post.created\",\"id\":" + post.getId() + ",\"title\":\"" + post.getTitle() + "\"}";
        Request request = new Request.Builder()
                .url(NOTIFY_URL)
                .header("X-Notify-Secret", NOTIFY_SECRET)
                .post(RequestBody.create(json, MediaType.get("application/json")))
                .build();
        try {
            client.newCall(request).execute().close();
        } catch (Exception e) {
            // 알림 실패는 글 작성에 영향을 주지 않는다
        }
    }
}
