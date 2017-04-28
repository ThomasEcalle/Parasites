"use strict";

const fs = require("fs");
const express = require("express");
const session = require("express-session")
const request = require("request");
let router = express.Router();

router.get("/", function(req, res, next){
    var page = req.query.page;
    if(page == 'inscription'){
        res.render('inscription.ejs', {alert : "", info : ""});
    }
    else{
        res.render('connexion.ejs', {});
    }
});	


router.post("/", function(req, res, next){
    var page = req.query.page;
    if(page == 'inscription'){
        request.post(
        'http://localhost:3000/users/create',
        { json: { 
                "lastname": req.body.lastname,
                "firstname": req.body.firstname,
                "pseudo": req.body.pseudo,
                "password": req.body.password,
                "email": req.body.email,
                "phone": req.body.phone
            } 
        }, function (error, response, body) {
                if(response.statusCode != 200){
                    /*
                    var errors = "";
                    for(var i = 0; i < response.body.errors.length; i++){
                        errors += response.body.errors[i];
                    }*/
                    var othersErrors = response.body.errors.length > 1 ? " + " + (response.body.errors.length - 1) + " other error(s)." : "";
                    res.render('inscription.ejs', {alert : response.body.errors[0] + othersErrors, info : "", req : req});
                }
                res.render('inscription.ejs', {alert : "", info : "The user " + req.body.pseudo + " has been succefully added to the database."});
            }
        );
    }
    else{
        var sess = req.session;
        sess.email = "ok";
        res.redirect('/accueil');
    }
});

module.exports = router;