"use strict";

const express = require("express");
const session = require("express-session");
let router = express.Router();

router.get("/", function(req, res, next){
    var sess = req.session;
    var connected = sess.user ? true : false;
    res.render('template_commun.ejs', { page : "CLASSEMENT", user : sess.user, connected : connected });
});	

module.exports = router;