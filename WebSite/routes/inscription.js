"use strict";

const express = require("express");
const session = require("express-session");
const request = require("request");
let router = express.Router();

router.post("/", function(req, res, next){
    request.post(
       'http://localhost:3000/users/create',
        { json : {
                lastname: req.body.lastname,
                firstname: req.body.firstname,
                pseudo: req.body.pseudo,
                password: req.body.password,
                email: req.body.email,
                phone: req.body.phone
            }
        }, function(error, response, body){ 
            res.setHeader('Content-Type', 'application/json');
            res.send(JSON.stringify(response)); 
            res.end();
        });
});	

module.exports = router;