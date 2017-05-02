/**
 * Created by Robin on 06/04/2017.
 */
"use strict";
const express = require("express")
const jwt = require('jsonwebtoken');

//const models = require("../models");
// let router = express.Router();
// const Colony = models.Colony;
// const Defenser = models.Defenser;
// const Fighter = models.Fighter;
// const Queen = models.Queen;
// const Worker = models.Worker;

module.exports = function(app)
{
    // var requirejs = require('requirejs');
    //
    // requirejs.config({
    //     //Pass the top-level main.js/index.js require
    //     //function to requirejs so that node modules
    //     //are loaded relative to the top-level JS file.
    //     baseUrl: "./public/js/",
    //     nodeRequire: require
    // });
    app.get("/tutorial", function(req, res, next){

        res.type('html');


        var html = '<html>';

        html += '<head>'
        html += '<title>Parasite Tutorial</title>'
        html += '<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>'
        html += '<script type="text/javascript" src="./public/test.js"></script>'


        html += '<script type="text/javascript" src="./public/classes/Tileset.js"></script>'
        html += '<script type="text/javascript" src="./public/classes/Board.js"></script>'
        html += '<script type="text/javascript" src="./public/classes/Pawn.js"></script>'
        html += '<script type="text/javascript" src="./public/classes/Queen.js"></script>'
        html += '<script type="text/javascript" src="./public/classes/Worker.js"></script>'
        html += '<script type="text/javascript" src="./public/classes/Fighter.js"></script>'
        html += '<script type="text/javascript" src="./public/classes/Colony.js"></script>'
        html += '<script type="text/javascript" src="./public/classes/Defenser.js"></script>'
        html += '<script type="text/javascript" src="./public/index.js"></script>'





        html += '</head>'
        html += '<body>'
        html += '<canvas id="canvas" onmouseover="can()" ></canvas>'
        html += '<label id="annoncer">d</label>'
        //html += '<button id="debug" >debug</button>'


        html += '</body>'

        html += '</html>';

        res.send(html);
    });

}

