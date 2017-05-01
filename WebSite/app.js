var express = require('express');
var path = require('path');
var request = require('request');
var fs = require('fs');
var favicon = require('serve-favicon');
var logger = require('morgan');
var bodyParser = require('body-parser');
var session = require('express-session');
var url = require('url');

var accueil = require('./routes/accueil');
var regles = require('./routes/regles');
var classement = require('./routes/classement');
var tutoriel = require('./routes/tutoriel');
var telechargement = require('./routes/telechargement');
var profil = require('./routes/profil');
var deconnexion = require('./routes/deconnexion');
var connexion = require('./routes/connexion');
var inscription = require('./routes/inscription');


var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(session({secret: 'metpoceblo', cookie: { maxAge: 365 * 24 * 60 * 60 * 6000 }}));
app.use(express.static(__dirname + '/public'));
app.use('/js', express.static(__dirname + '/node_modules/bootstrap/dist/js')); 
app.use('/js', express.static(__dirname + '/node_modules/jquery/dist'));
app.use('/css', express.static(__dirname + '/node_modules/bootstrap/dist/css'));

app.use('/accueil', accueil);
app.use('/regles', regles);
app.use('/classement', classement);
app.use('/tutoriel', tutoriel);
app.use('/telechargement', telechargement);
app.use('/profil', profil);
app.use('/deconnexion', deconnexion);
app.use('/connexion', connexion);
app.use('/inscription', inscription);


// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  // res.locals.message = err.message;
  // res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  // res.status(err.status || 500);
  // res.render('error');
  //  res.redirect('/accueil');
});

module.exports = app;
