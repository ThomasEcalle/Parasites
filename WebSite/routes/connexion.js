"use strict";

const express = require("express");
const session = require("express-session");
const request = require("request");
var constants = require('./../constants');
let router = express.Router();

router.post("/", function(req, res, next){
    var sess = req.session;
    request.post(
       constants.URL_API + "/users/connect",
        { json : {
                pseudo: req.body.pseudo,
                password: req.body.password
            }
        }, function(error, response, body){ 
            if(response.statusCode == 200){
                sess.user = req.body.pseudo;
                sess.token = response.body.token;
            }
            res.setHeader('Content-Type', 'application/json');
            res.send(JSON.stringify(response));
            res.end();
        });
});	

module.exports = router;