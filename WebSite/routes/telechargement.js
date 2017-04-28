"use strict";

const express = require("express");
const session = require('express-session');
let router = express.Router();

router.get("/", function(req, res, next){
    res.render('telechargement.ejs', {});    
});	

module.exports = router;