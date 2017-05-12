var express = require('express')
  , app = express()
  , http = require('http')
  , server = http.createServer(app)
  , io = require('socket.io').listen(server);


const bo = require("./bo");
const Game = bo.Game;


server.listen(3080);

// players which are currently connected to the server
var players = {};

// games which are currently available on server
var games = [];

io.sockets.on('connection', function (socket) {

    //When the client register itself
    socket.on('register', function(player){
      players[player] = player;
      socket.player = player;
      socket.broadcast.emit('updateServer', 'SERVER', player.pseudo + ' has connected');
      console.log("a new player has connected : " + player.pseudo);
      console.log("connected players are : " + JSON.stringify(players));
    });


  	// when the client emits 'createGame', this listens and executes
  	socket.on('createGame', function(gameInJson){

    var game = JSON.parse( gameInJson );
		// store the game in the socket session for this client
		socket.game = game;
		// add the client's username to the global list
		games.push(game);
		// send client to room 1
		socket.join(game.id);
		// echo to client they've connected
		socket.emit('updateServer', 'SERVER', 'you have created the game : ' + game.id);
    console.log(socket.player.pseudo +" has created a game : " +  JSON.stringify(game));
		// // echo to room 1 that a person has connected to their room
		// socket.broadcast.to(game.name).emit('updateServer', 'SERVER', username + ' has connected to this room');
		socket.emit('updateGames', JSON.stringify(games), game.name);
	});

	// when the client emits 'sendchat', this listens and executes
	socket.on('sendchat', function (data) {
		// we tell the client to execute 'updatechat' with 2 parameters
		io.sockets.in(socket.room).emit('updatechat', socket.username, data);
	});

	socket.on('switchRoom', function(newroom){
		socket.leave(socket.room);
		socket.join(newroom);
		socket.emit('updatechat', 'SERVER', 'you have connected to '+ newroom);
		// sent message to OLD room
		socket.broadcast.to(socket.room).emit('updatechat', 'SERVER', socket.username+' has left this room');
		// update socket session room title
		socket.room = newroom;
		socket.broadcast.to(newroom).emit('updatechat', 'SERVER', socket.username+' has joined this room');
		socket.emit('updaterooms', rooms, newroom);
	});


	// when the user disconnects.. perform this
	socket.on('disconnect', function(){
		// remove the player from global players list
		delete players[socket.player];
    // remove the game where the player is the host
    delete games[socket.game];
		// // update list of users in chat, client-side
		// io.sockets.emit('updateusers', usernames);

		// echo globally that this client has left
		socket.broadcast.emit('updateServer', 'SERVER', socket.player.pseudo + ' has disconnected');
    console.log(socket.player.pseudo + " has disconnected");
		socket.leave(socket.room);
	});
});
