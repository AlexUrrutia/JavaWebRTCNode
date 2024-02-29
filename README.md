
## webrtc sample using WebSocket Node.js Signaling

* Node.js signaling service: [https://github.com/linpengtian/NativeAndroidWebRTC/blob/master/webrtc%20server/index.js](https://github.com/linpengtian/AndroidWebRTCNode/blob/main/webrtc%20server/index.js)


## How to run
* start node server (npm install / npm start)
* Change the server ip(localhost) with your real server ip address in Android code(`SocketClient.java`).
  ```
  Request request = new Request.Builder()
                .url("ws://localhost:3000")
                .build();
  ```
* Build and Run the Android app
