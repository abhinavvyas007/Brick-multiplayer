var app = require('express');
var server = require('http').Server(app);
var io = require('socket.io')(server);
var players = [];

server.listen(8080, function() {
    console.log("server is now running");
});

function player(id, x, y) {
    this.id = id;
    this.x = x;
    this.y = y;
}

io.on('connection', function(socket) {
    console.log("player connected");
    socket.emit('socketID', { id: socket.id});
    socket.broadcast.emit('newPlayer', { id: socket.id});
    players.push(player(socket.id, 80/100, 140/100));
    socket.emit('getPlayers', players);
    socket.on('disconnect', function() {
        socket.broadcast.emit('playerDisconnected', { id: socket.id });
        console.log("player disconnected");
        for (var i = 0; i < players.length; i++) {
            if (players[i].id == socket.id) {
                players.splice(i, 1);
            }
        }
    });
});

