package coindcx.socket;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.Polling;
import io.socket.engineio.client.transports.WebSocket;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import static java.util.Collections.singletonMap;

public class SocketIOApps_Client01 {

    public static void main(String[] args) throws InterruptedException, URISyntaxException {

        IO.Options options = IO.Options.builder()
                .setTimeout(60000)
                .setTransports(new String[]{WebSocket.NAME, Polling.NAME})
                .build();

        Socket socket = IO.socket(new URI("http://localhost:5000"), options);

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args1) {
                if (socket.connected()) {
                    System.out.println(socket.connected()); // true
                    System.out.println(socket.id());
                    System.out.println(Arrays.toString(args1));
                }
            }
        });
        socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println(socket.connected()); // false
            }
        });
        socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args1) {
                System.out.println(args1[0]);
            }
        });

        socket.connect();
        while (!socket.connected()) {
            Thread.sleep(1000);
        }

        socket.emit("join", new JSONObject(singletonMap("channelName", "B-BTC_USDT")));

        socket.on("depth-update", data -> {
            System.out.println("depth-update>>>"+data[0]); // "please acknowledge"
        });
    }
}
