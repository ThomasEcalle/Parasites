"use strict";

const express = require("express");
const session = require("express-session");
let router = express.Router();

router.get("/", function(req, res, next){
    var sess = req.session;
    var connected = sess.user ? true : false;
    res.render('template_commun.ejs', { page : "ACCUEIL", user : sess.user, connected : connected });
});

router.get("/inscription", function(req, res, next){
    var sess = req.session;
    var connected = sess.user ? true : false;
    res.render('template_commun.ejs', { page : "ACCUEIL", user : sess.user, connected : connected, param : "inscription" });
});

router.get("/reinit", function(req, res, next){
    var sess = req.session;
    var connected = sess.user ? true : false;
    res.render('template_commun.ejs', { page : "ACCUEIL", user : sess.user, connected : connected, param : "reinit" });
});

module.exports = router;