"use strict";
const express = require("express");
const jwt = require('jsonwebtoken');

const models = require("../models");
let router = express.Router();
const User = models.User;
const Turn = models.Turn;
const Game = models.Game;
const User_play_game = models.User_play_game;

/********************************
* 		 Create a Turn			*
*********************************/

router.post("/create", function(req,res,next){
	if (req.user){
		var id_user = req.user.id;
		var game_id = req.body.game_id;
		var board = req.body.board;
		
		Turn.create({
		  user_id: id_user,
		  game_id: game_id,
		  board: board
		}).then(function(turn){
		  res.json(turn);
		}).catch(next);
	}

});

/********************************
* 	  Get all turns Of a Game	*
*********************************/

router.get("/:id_game", function(req,res,next){
	if (req.user){
		
	Turn.findAll({
	  where: {
		game_id: req.params.id_game
	  },
	  order: '"updatedAt" DESC'
	  
	}).then(function(turns){
		for (let i in turns){
			User.find({
			  where: {
				id: turns[i].user_id
			  }
			}).then(function(user){
			  if(user){
				 turns[i].user_id = user.responsify();
			  }
				
			  });
		}
		
		res.status(200);
		return res.json(turns);
		
	}).catch(next);

	}
	
});

module.exports = router;