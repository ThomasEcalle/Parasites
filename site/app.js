/**
 * Created by Robin on 07/03/2017.
 */


const express = require('express');
var app = express();
var path = require('path');

const cookieParser = require('cookie-parser');

const bodyParser = require('body-parser');



app.use(cookieParser());

app.use(bodyParser.urlencoded({
    extended: true
}))

app.use(bodyParser.json());
require('./routes')(app);
app.use('/public',express.static(path.join(__dirname, 'public/')));

app.get('/', function(req, res, next){
    res.render('./index.html');
});
// app.get('*', function(req, res)
// {
//     res.type('html');
//     res.status(404)
//     res.send("Brouze est perdu")
// });
app.listen(8080, function()
{
    console.log("Listen port 8080");
});