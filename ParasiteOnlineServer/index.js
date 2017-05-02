var http = require('http');
var fs = require('fs');

// Chargement du fichier index.html affiché au client
var server = http.createServer(function(req, res) {});

// Chargement de socket.io
var io = require('socket.io').listen(server);
var socket = io.sockets;

var players = [];

// Quand un client se connecte, on le note dans la console
socket.on('connection', function (socket) {
    console.log('Un client est connecté !');


    // Quand le serveur reçoit un signal de type "message" du client
    socket.on('declaring_identity', function (player) {
        if (player.pseudo){
          socket.pseudo = player.pseudo;
          players.push(player.pseudo);
           console.log("Un client se présente, il d'agit de : " + player.pseudo);

           // Message de Broadcast pour notifier d'un nouvel utilisateur connecté
           socket.broadcast.emit('message',
           "Un nouvel utilisateur vient de se connecter ! Il s'agit de " + socket.pseudo +
            "\nLes utilisateurs en ligne sont " + players);
        }
     });

    setTimeout(function()
    {
      console.log("sending hello");
       socket.emit("message", "Hello");
     }, 3000);
});




server.listen(3880);
