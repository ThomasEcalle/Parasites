"use strict";

const express = require("express");
const session = require("express-session");
const request = require("request");
let router = express.Router();

router.get("/", function(req, res, next){
    var mod = req.param("mod");
    var sess = req.session;
    var connected = sess.user ? true : false;
    res.render('template_commun.ejs', { page : "PROFIL", mod : (mod ? mod : "pseudo"), user : sess.user, connected : connected, error : "", info : "" });
});	

router.post("/", function(req, res, next){
    var mod = req.param("mod");
    var sess = req.session;
    var connected = sess.user ? true : false;
    var options = { page : "PROFIL", mod : (mod ? mod : "pseudo"), user : sess.user, 
                   connected : connected, error : "", info : "" };
    if(req.body.input1 != req.body.input2){
        options.error = "The new value does not matches its confirmation."
        res.render('template_commun.ejs', options);
    }
    var dirPath = "http://localhost:3000/users/update/" + req.body.type_form + "/" + req.body.input1 + "/?token=" + sess.token;
    request.put(dirPath, function(error,response,body){
        if(response.statusCode != 200){
            options.error = response.body.errors[0];
            res.render('template_commun.ejs', options);
        }
        else{
            if(req.body.type_form == "pseudo"){
                sess.user = req.body.input1;
                options.user = req.body.input1;
            }
            options.info = "The " + req.body.type_form + " has been changed.";
            res.render('template_commun.ejs', options);
        }
    });
});

module.exports = router;