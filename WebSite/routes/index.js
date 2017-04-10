"use strict";

const fs = require("fs");
const express = require("express");
let router = express.Router();

router.get("/", function(req, res, next){
    var page = req.query.page;
    if(page == 'inscription'){
        res.render('inscription.ejs', {});
    }
    else{
        res.render('connexion.ejs', {});
    }
});	

module.exports = router;