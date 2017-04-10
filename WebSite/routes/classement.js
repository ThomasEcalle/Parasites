"use strict";

const express = require("express");
let router = express.Router();

router.get("/", function(req, res, next){
    res.render('classement.ejs', {});
});	

module.exports = router;