package com.codewithkael.javawebrtcyoutube.remote;

import android.util.Log;

import com.codewithkael.javawebrtcyoutube.utils.DataModel;
import com.codewithkael.javawebrtcyoutube.utils.ErrorCallBack;
import com.codewithkael.javawebrtcyoutube.utils.NewEventCallBack;
import com.codewithkael.javawebrtcyoutube.utils.SuccessCallBack;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;


public class SocketClient extends WebSocketListener {

    private final Gson gson = new Gson();
    private String currentUsername;

    WebSocket webSocket;
    NewEventCallBack callBack;
    private static final String TAG = "SocketClient";

    public SocketClient() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("ws://localhost:3000")
                .build();
        webSocket = client.newWebSocket(request, this);

    }

    public void login(String username, SuccessCallBack callBack){
        // Construct a JSON object containing the offer
        try {
            JSONObject loginJson = new JSONObject();
            loginJson.put("type", "store_user");
            loginJson.put("name", username);

            webSocket.send(loginJson.toString());

            currentUsername = username;
            callBack.onSuccess();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void sendMessageToOtherUser(DataModel dataModel, ErrorCallBack errorCallBack){
            String startCallJson = gson.toJson(dataModel);
            webSocket.send(startCallJson);
    }

    public void observeIncomingLatestEvent(NewEventCallBack callBack){
        this.callBack = callBack;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        // WebSocket connection established
        Log.d(TAG, "WebSocket connection opened");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        // Received a text message from the server
        Log.d(TAG, "Received message from server: " + text);

        DataModel dataModel = gson.fromJson(text, DataModel.class);
        callBack.onNewEventReceived(dataModel);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        // Received a binary message from the server
        Log.d(TAG, "Received binary message from server: " + bytes.hex());
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        // WebSocket connection closed
        Log.d(TAG, "WebSocket connection closed");
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        // WebSocket connection failure
        Log.e(TAG, "WebSocket connection failure: " + t.getMessage());
    }
}
