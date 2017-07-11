"use strict";
const express = require("express");
const jwt = require('jsonwebtoken');

const models = require("../models");
let router = express.Router();
const User = models.User;
const Game = models.Game;
const User_play_game = models.User_play_game;

/********************************
* 		 Create a Game			*
*********************************/

router.post("/create", function(req,res,next){
	if (req.user){
		console.log("\nyoooooooooooooooooo\n")
		var id_user = req.user.id;
		var idGame = req.body.id;
		var nb_player = req.body.nb_player;
		var size = req.body.size;
		
		console.log("creation of a game : " + id_user + ", " + nb_player + ", " + size);
		
		Game.create({
			id: idGame,
			  user_id: id_user,
			  nb_player: nb_player,
			  size: size
		}).then(function(game){
			
			
		User_play_game.create({
			game_id: game.id,
			user_id: id_user
		}).then(function(user_game){
		  res.json(game);
		}).catch(next);
			
		  
		}).catch(next);
	}

});

/********************************
* 		 Join a Game			*
*********************************/

router.post("/join/", function(req,res,next){
	if (req.user){
		
		var id_user = req.user.id;
		var idGame = req.body.id;
		
		console.log("yohoho : " + id_user +", " + idGame);
		
		User_play_game.create({
			game_id: idGame,
		  user_id: id_user
		}).then(function(user_game){
		  res.json(user_game);
		}).catch(next);
	}

});

module.exports = router;