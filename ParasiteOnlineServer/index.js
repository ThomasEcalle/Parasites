var express = require('express')
  , app = express()
  , http = require('http')
  , server = http.createServer(app)
  , io = require('socket.io').listen(server);


const bo = require("./bo");
const Game = bo.Game;


server.listen(3080);

// players which are currently connected to the server
var players = [];

// games which are currently available on server
var games = [];

io.sockets.on('connection', function (socket) {


    var player = socket.handshake.query.register;
    player = JSON.parse(player);
    if (player){
      players.push(player.pseudo);
      socket.player = player;

      socket.broadcast.emit('updateServer', player.pseudo + ' has connected');
      socket.emit('currentServerState',JSON.stringify(players), JSON.stringify(games));

      console.log("a new player has connected : " + player.pseudo);
      console.log("connected players are : " + JSON.stringify(players));
    }


  	// when the client emits 'createGame', this listens and executes
  	socket.on('createGame', function(gameInJson){

    // A user can only create one game at a time
    if (socket.game){
      socket.emit('updateServer', 'You cannot create two games at a time');
    }
    else {
      var game = JSON.parse( gameInJson );
  		// store the game in the socket session for this client
  		socket.game = game;
  		// add the client's username to the global list
  		games.push(game);
  		// send client to room 1
  		socket.join(game.id);
  		// echo to client they've connected

      console.log(socket.player.pseudo +" has created a game : " +  JSON.stringify(game));
  		// // echo to room 1 that a person has connected to their room
  		// socket.broadcast.to(game.name).emit('updateServer', 'SERVER', username + ' has connected to this room');

      socket.emit('personalGameCreation', JSON.stringify(game));
  		socket.broadcast.emit('generalGameCreation', JSON.stringify(games), JSON.stringify(game));
    }
	});

	// when the client emits 'sendchat', this listens and executes
	socket.on('sendchat', function (data) {
		// we tell the client to execute 'updatechat' with 2 parameters
		io.sockets.in(socket.room).emit('updatechat', socket.username, data);
	});

	socket.on('leaveGame', function(){
		socket.leave(socket.game.id);
		socket.emit('updateServer', 'you have left the game '+ socket.game.id.value);
		// sent message to players in game
		socket.broadcast.to(socket.game.id).emit('updateGame', socket.player.pseudo+' has left this game');

    // If user is the creator of the game
    console.log(JSON.stringify(socket.game));
    if (socket.game.creatorPseudo.value == socket.player.pseudo){
        var index = games.indexOf(socket.game);
        games.splice(index, 1);
        socket.game = null;
    }
		// // update socket session room title
		// socket.room = newroom;
		// socket.broadcast.to(newroom).emit('updatechat', 'SERVER', socket.username+' has joined this room');
	});


	// when the user disconnects.. perform this
	socket.on('disconnect', function(){
		// remove the player from global players list
    var index = players.indexOf(socket.player);
    players.splice(index, 1);
    // remove the game where the player is the host
    var index = games.indexOf(socket.game);
    games.splice(index, 1);
		// // update list of users in chat, client-side
		// io.sockets.emit('updateusers', usernames);

		// echo globally that this client has left
		socket.broadcast.emit('updateServer', socket.player.pseudo + ' has disconnected');
    console.log(socket.player.pseudo + " has disconnected");
		socket.leave(socket.room);
	});
});
