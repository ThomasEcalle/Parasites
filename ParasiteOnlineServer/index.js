var express = require('express')
  , app = express()
  , http = require('http')
  , server = http.createServer(app)
  , io = require('socket.io').listen(server);
var port = 3080;
server.listen(port, "0.0.0.0");
console.log("listening on port " + port);

// players which are currently connected to the server
var userList = {};

// games which are currently available on server
var gamesList = {};




var request = require('request');


io.sockets.on('connection', function (socket) {

    var user = JSON.parse(socket.handshake.query.register);
    if (user){
      userList[user.pseudo] = user;
      socket.userPseudo = user.pseudo;
	  socket.actualToken = user.token;

      socket.broadcast.emit('updateServer', user.pseudo + ' has connected');

      updateServerState();

      cleanLog("\na new player has connected : " + socket.userPseudo+"\n");
      cleanLog("\nconnected players are : " + JSON.stringify(userList)+"\n");
    }

	
  	// when the client emits 'createGame', this listens and executes
  	socket.on('createGame', function(gameInJson){

    // A user can only create one game at a time
    if (socket.game){
      socket.emit('updateServer', 'You cannot create two games at a time');
    }
    else {
      var game = JSON.parse(gameInJson);

  		// store the game in the socket session for this client
  		socket.gameID = game.id;

  		// add the client's username to the global list
  		gamesList[game.id] = game;

      cleanLog("\n"+socket.userPseudo +" has created a game : " +  JSON.stringify(socket.gameID)+"\n");
  		// // echo to room 1 that a person has connected to their room
  		// socket.broadcast.to(game.name).emit('updateServer', 'SERVER', username + ' has connected to this room');
      socket.join(game.id);

      cleanLog("\n\ngames : " + JSON.stringify(gamesList) +"\nusers" +JSON.stringify(userList)+"\n\n");
      updateServerState();
	  
	  request({
		url: "http://localhost:3000/games/create?token=" + game.creator.token,
		method: "POST",
		headers: {
			"content-type": "application/json",
			},
		json: {
					"id": game.id,
				  "user_id": game.creator.id,
				  "nb_player": game.playersList.length,
				  "size": game.size
			}
		}, function (error, resp, body) { 
			//console.log(resp)
		})
	  
    }
	});

  socket.on('joinGame', function(game){
    var game = JSON.parse(game);

    cleanLog("\n"+socket.userPseudo +" try to join game : " + game.id+"\n");

    game.playersList.push(userList[socket.userPseudo]);

    //delete gamesList[game.id];
    gamesList[game.id] = game;

    socket.join(game.id);
    socket.gameID = game.id;

    socket.broadcast.to(socket.gameID).emit('updateServer', socket.userPseudo+' has join the game');
    updateServerState();
	
	
	request({
		url: "http://localhost:3000/games/join/?token=" + socket.actualToken,
		method: "POST",
		headers: {
			"content-type": "application/json",
			},
		json: {
				"id": game.id,
			}
		}, function (error, resp, body) { 
			//console.log(resp)
		})

  });
  
  socket.on('winner', function(){
	  
	  
	  request({
		url: "http://localhost:3000/games/winner/?token=" + socket.actualToken,
		method: "PUT",
		headers: {
			"content-type": "application/json",
			},
		json: {
				"id": socket.gameID,
			}
		}, function (error, resp, body) { 
			//console.log(resp)
		})
		
		delete gamesList[socket.gameID];
		updateServerState();
		
		io.in(socket.gameID).emit('winner', socket.userPseudo);
		
		
  });

	socket.on('leaveGame', function(){
		socket.emit('updateServer', 'you have left the game '+ socket.gameID);
		socket.broadcast.to(socket.gameID).emit('updateServer', socket.userPseudo+' has left this game');

    var newGame = gamesList[socket.gameID];

    socket.leave(socket.gameID);

    var indexOfUser;
    for(var i = 0; i < newGame.playersList.length; i++) {
      if (newGame.playersList[i].pseudo == socket.userPseudo){
        indexOfUser = i;
      }
    }

    newGame.playersList.splice(indexOfUser, 1);

    //Adding the new game configuration
    gamesList[socket.gameID] = newGame;

    // cleanLog("\n**********\nAfter leaving game : \nnewGame : "
    // + JSON.stringify(newGame) +"\n**********\nGames : " + JSON.stringify(gamesList)+"\n\n");

    if (gamesList[socket.gameID].playersList.length <= 0){
      delete gamesList[socket.gameID];
    }

    socket.gameID = null;


    updateServerState();
		// // update socket session room title
		// socket.room = newroom;
		// socket.broadcast.to(newroom).emit('updatechat', 'SERVER', socket.username+' has joined this room');
	});

  socket.on('sendGameChatMessage', function(chatMessage){
    io.in(socket.gameID).emit('receivingGameChatMessage', chatMessage);
  });

  socket.on('launchGame', function(){

    var game = gamesList[socket.gameID];
    game.launched = true;
    gamesList[socket.gameID] = game;


    updateServerState();
    io.in(socket.gameID).emit('launchingGame');
	
  });
  
  
  socket.on('playMove', function(origin, isTileLocked, chosenParasiteName, savedBoard){

	cleanLog("\n" +  "voici le mouvement envoyé ! : "+ origin+ ", " + isTileLocked+ ", "  + chosenParasiteName +  "\n");
	
	
	
	io.in(socket.gameID).emit('playMove', origin, isTileLocked, chosenParasiteName);

  });
  
  socket.on('saveTurn', function(turn){
	if (typeof turn != 'undefined')
	{
		request({
		url: "http://localhost:3000/turns/create/?token=" + socket.actualToken,
		method: "POST",
		headers: {
			"content-type": "application/json",
			},
		json: {
				"game_id": socket.gameID,
				"board": turn
			}
		}, function (error, resp, body) { 
			//console.log(resp)
		})
	} 
  });
  
  socket.on('passTurn', function(){

	cleanLog("pass turn");
	
	
	io.in(socket.gameID).emit('passTurn');

  });

	// when the user disconnects.. perform this
	socket.on('disconnect', function(){
		// remove the player from global players list
    delete userList[socket.userPseudo];


    if (socket.gameID){
      var newGame = gamesList[socket.gameID];

	  if (typeof newGame != 'undefined'){
		   var indexOfUser;
		  for(var i = 0; i < newGame.playersList.length; i++) {
			if (newGame.playersList[i].pseudo == socket.userPseudo){
			  indexOfUser = i;
			}
		  }

		  newGame.playersList.splice(indexOfUser, 1);

		  //Adding the new game configuration
		  gamesList[socket.gameID] = newGame;

		  if (gamesList[socket.gameID].playersList.length <= 0){
			delete gamesList[socket.gameID];
		  }
	  }
   
    }


		// // update list of users in chat, client-side
		// io.sockets.emit('updateusers', usernames);

		// echo globally that this client has left
		socket.broadcast.emit('updateServer', socket.userPseudo + ' has disconnected');
    cleanLog(socket.userPseudo + " has disconnected");

    updateServerState();
	});

  function updateServerState(){
    io.emit('currentServerState',JSON.stringify(userList), JSON.stringify(gamesList));
  }

  function cleanLog(data){
    console.log("\n\n"+ data + "\n\n");
  }
});
