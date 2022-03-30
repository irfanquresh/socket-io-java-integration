const app = require("express")();
const http = require("http").createServer(app);
const io = require("socket.io")(http);
const cors = require("cors");
const PORT = process.env.PORT || 5000;

app.use(cors());

io.on("connection", (socket) => {
  socket.on("join", ({ channelName }) => {
    console.log(socket.id + "-" + channelName);
    socket.join(channelName);
    socket.in(channelName).emit("depth-update", {
      description: `${socket.id} just entered the room`,
    });
    io.in(channelName).emit("depth-update", "Hello world from " + channelName);
  });

  socket.on("disconnect", () => {
    io.in("B-BTC_USDT").emit("depth-update", {
      description: `${socket.id} just left the room`,
    });
  });
});

app.get("/", (req, res) => {
  res.send("Server is up and running");
});

http.listen(PORT, () => {
  console.log(`Listening to ${PORT}`);
});
