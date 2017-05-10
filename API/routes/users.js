"use strict";
const express = require("express")
const jwt = require('jsonwebtoken');

const models = require("../models");
let router = express.Router();
const User = models.User;
const Friendship = models.Friendship;


/******************************************
*             get all Users               *
*******************************************/
// correspond to the URL /users
router.get("/",function(req, res, next){
  let l = parseInt(req.query.limit) || 20;
  let o = parseInt(req.query.offset) || 0;
  let options = {
    limit: l,
    offset: o
  }

  let s = req.query.search;
  if(s != undefined){
    let where = {
      $or:{
        lastname: {
          $like: "%" + s + "%"
        },
        firstname: {
          $like: "%" + s + "%"
        }
      }

    }
    options.where = where
  }
  User.findAll(options).then(function(users){
    for (let i in users){
      users[i] = users[i].responsify();
    }
    res.status(200);
    return res.json(users);
  }).catch(next);
})



/******************************************
*             get one User                *
*******************************************/
// correspond to the URL /users/3 for exemple
router.get("/:user_id", function(req,res,next){
  User.find({
    where: {
      id: req.params.user_id
    }
  }).then(function(user){
    if(user){
      res.json(user);
    }
    res.status(400)
    return res.json({
      "result": 0,
      "message": "No user found"
    });
  }).catch(next)
})



/******************************************
*             Update Pseudo               *
*******************************************/
router.put("/update/pseudo/:user_pseudo", function(req,res,next){
  if (req.user){
    let user = req.user;
    let newPseudo =  req.params.user_pseudo
    user.update({
    pseudo: newPseudo
    }).then(function() {
      return res.status(200).send({
        result: 1,
        message: "Pseudo correctly updated"
      })
    }).catch(next);

  }
})


/******************************************
*             Update Email                *
*******************************************/
router.put("/update/email/:user_mail", function(req,res,next){
  if (req.user){
    let user = req.user;
    let newEmail =  req.params.user_mail
    user.update({
    email: newEmail
    }).then(function() {
      return res.status(200).send({
        result: 1,
        message: "Email correctly updated"
      })
    }).catch(next);
  }
})



/******************************************
*             Update First name           *
*******************************************/
router.put("/update/firstname/:user_firstname", function(req,res,next){
  if (req.user){
    let user = req.user;
    let newFirstname =  req.params.user_firstname

    user.update({
    firstname: newFirstname
    }).then(function() {
      return res.status(200).send({
        result: 1,
        message: "First name correctly updated"
      })
    }).catch(next);

  }
})



/******************************************
*             Update Last name            *
*******************************************/
router.put("/update/lastname/:user_lastname", function(req,res,next){
  if (req.user){
    let user = req.user;
    let newLastname =  req.params.user_lastname

    user.update({
    lastname: newLastname
    }).then(function() {
      return res.status(200).send({
        result: 1,
        message: "Last name correctly updated"
      })
    }).catch(next);

  }
})



/******************************************
*             Update Phone number         *
*******************************************/
router.put("/update/phone/:user_phone", function(req,res,next){
  if (req.user){
    let user = req.user;
    let newPhone =  req.params.user_phone

    user.update({
    phone_number: newPhone
    }).then(function() {
      return res.status(200).send({
        result: 1,
        message: "Phone number correctly updated"
      })
    }).catch(next);

  }
})


/******************************************
*             Update Password             *
*******************************************/
router.put("/update/password/:user_password", function(req,res,next){
  if (req.user){
    let user = req.user;
    let newPassword =  req.params.user_password

    user.update({
    password: newPassword
    }).then(function() {
      return res.status(200).send({
        result: 1,
        message: "Password correctly updated"
      })
    }).catch(next);

  }
})

/******************************************
*             Deconnect User              *
*******************************************/
router.put("/deconnect", function(req,res,next){
  if (req.user){
    let user = req.user;

    user.update({
    token_available: null
    }).then(function() {
      return res.status(200).send({
        result: 1,
        message: "Successfully deconnected"
      })
    }).catch(next);

  }
})

/********************************************************************************************
*                                      FRIEND ZONE                                          *
********************************************************************************************/

/******************************************
*               Add friend                *
*******************************************/
router.post("/friends/add/:friend_id", function(req, res, next){
  if (req.user){
    let answer = {};
    let user = req.user;
    let friend_id = req.params.friend_id
    User.find({
      where: {
        id: friend_id
      }
    }).then(function(friend){
      if (friend){
        if(user.id == friend.id){
          return res.status(400).json({
            result: 0,
            message: "You can't add yourself as a friend... it is a little bit ackward !"
          })
        }
        else {
          Friendship.find({
            where: {
              friend_id: user.id,
                  $and: {user_id: friend.id}
                }
          }).then(function(relation){
            if(relation){
              relation.update({
              accepted: 1
              }).then(function() {
                return res.status(200).send({
                  result: 1,
                  message: "You accepted friendship"
                })
              }).catch(next);
            }else{
              user.addFriend(friend).then(function(){
                return res.send({
                  result: 1,
                  message: "Friend correctly added"
                })
              })
            }
          }).catch(next);

        }
      }
      else {
        return res.status(400).json({
          result: 0,
          message: "You try to add as a friend a user that does not exist"
        })
      }

    }).catch(next);
  }
})

/******************************************
*               Remove Friend             *
*******************************************/
router.delete("/friends/remove/:friend_id", function(req,res,next){
  if (req.user){
    let user = req.user;
    let friend_id = req.params.friend_id
    Friendship.find({
      where: {
        $or: [{friend_id: user.id,
                $and: {user_id: friend_id} }
            , {user_id: user.id,
                $and: {friend_id: friend_id}}],
        $and: {accepted: 1}
      }
    }).then(function(relation){
      if (relation){
        return relation.destroy({where: {}}).then(function(){
          res.json({
            result: 1,
            message: "Friendship correctly removed"
          })
        }).catch(next)
      }
      else{
        return res.status(400).json({
          result: 0,
          message: "Users are not friends"
        })
      }
    }).catch(next)
  }

})

/******************************************
*               get friends               *
*******************************************/
router.get("/friends/all", function(req, res, next){
  if(req.user){
    let user = req.user;

    Friendship.findAll({
      where:
      {
        $or: [{user_id: user.id}, {friend_id: user.id}],
        accepted: 1
      }
    }).then(function(friendships){
      let result = [];
      for (let i in friendships){
        if (friendships[i].user_id == user.id){
          result.push(friendships[i].friend_id);
        }else {
          result.push(friendships[i].user_id);
        }
      }
      res.status(200).send(result);
    }).catch(next);
  }
})

module.exports = router;
