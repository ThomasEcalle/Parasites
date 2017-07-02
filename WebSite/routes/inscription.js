"use strict";

const express = require("express");
const session = require("express-session");
const request = require("request");
let router = express.Router();

router.post("/", function(req, res, next){
    if(req.body.password != req.body.verif_password){
        var data_res = {
          "statusCode": 400,
          "result": 0,
          "body": {
              "errors": [
                "Validation error: Confirmation password and password does not matches."
              ]
          }
        };
        getRes(res, data_res);
    }
    else{
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
                getRes(res, response);
                console.log("okokokokok");
            });
    }
});

function getRes(res, data){
    res.setHeader('Content-Type', 'application/json');
    res.send(data);
    res.end();
}

module.exports = router;
