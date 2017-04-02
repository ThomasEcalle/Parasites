var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');

/******************************************
*            Tokens utils                 *
*******************************************/
var moment = require('moment');
var constants = require('./constants');
var jwt = require('jwt-simple');


var index = require('./routes/index');
var users = require('./routes/users');

var models = require("./models");
models.sequelize.sync();

const User = models.User;
var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
//app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));


/******************************************
*      Without token security calls       *
*******************************************/

/*
* Connect a user
*/
app.post("/users/connect", function(req,res,next){
  models.User.find({
    where: {
      pseudo: req.body.pseudo,
      password: req.body.password
    }
  }).then(function(user){
    if(user){

      // We set a token's duration's time of 1 day
      var expires = moment().add('d', 1).valueOf();

      //We encode the token here (using our secret)
      var token = jwt.encode({
        iss: user.id,
        exp: expires
      }, constants.secret);

      res.status(200).send({
        "result": 1,
        "message": "Athentification is a success",
        "expires in": "1 day",
        "token": token
      })
    }else{
      res.status(400).send({
        "result": 0,
        "message": "Athentification failed",
      })
    }
  })
})

/*
* Create a user (inscription)
*/

app.post("/users/create", function(req,res,next){
  let lastname = req.body.lastname;
  let firstname = req.body.firstname;
  let email = req.body.email;
  let pseudo = req.body.pseudo;
  let password = req.body.password;
  let phone = req.body.phone;

    models.User.create({
      lastname: lastname,
      firstname: firstname,
      email: email,
      pseudo: pseudo,
      password: password,
      phone_number: phone
    }).then(function(user){
      res.json(user);
    }).catch(next);

});


/******************************************
*      End of securityless calls           *
*******************************************/




/******************************************
*      Security token check middleware    *
*******************************************/
app.use(function(req,res,next){
  let token =req.body.token || req.query.token;
  if (token){
    // verifies secret and checks exp
    try {
       var decoded = jwt.decode(token, constants.secret);
      //Is the token expired ?
       if (decoded.exp <= Date.now()) {
          return res.end('Access token has expired', 400);
        }

        // We put the User object on every req, for every routes !
        User.findById(decoded.iss).then(function(user) {
          console.log(user);
          req.user = user;
          return next();
        })

     } catch (err) {
       err.message = "Bad token"
       return next(err);
     }
  }
  else{
    // if there is no token
   // return an error
   return res.status(403).send({
       result: 0,
       message: 'No token provided.'
   });
 }
});


app.use('/', index);
app.use('/users', users);


// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  //res.locals.message = err.message;
  //res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  ///res.render('error');

  let json = {result : 0};
  let array = err.message.split(/\n/);

  console.log("error message : " +err.message);
  json.errors = [];
  for (let e of array){
    json.errors.push(e);
  }
  res.send(json);
});

module.exports = app;
