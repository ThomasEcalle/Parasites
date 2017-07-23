"use strict";

const express = require("express");
const session = require("express-session");
const mailer = require("nodemailer");
let router = express.Router();
const smtpTransport = mailer.createTransport({
                    tls: { rejectUnauthorized: false },
					service: "Gmail",
					auth: {
						user: "parasitesgame@gmail.com",
						pass: "1234pgPG"
					}
				});

router.get("/", function(req, res, next){
    var sess = req.session;
    var connected = sess.user ? true : false;
    res.render('template_commun.ejs', { page : "CONTACT", user : sess.user, connected : connected });
});	

router.post("/", function(req, res, next){
    var mail = {
					from: "parasitesgame@gmail.com",
					to: "spyroo913@hotmail.com",
					subject: req.body.title,
					html: "Author :<br>" + req.body.author + "<br><br>Message :<br>"  + req.body.text.replace(/\r\n|\r|\n/g,"<br />")
				}
    
    smtpTransport.sendMail(mail, function(error, response){
					if(error){
						console.log("Erreur lors de l'envoie du mail!");
						console.log(error);
					}else{
						console.log("Mail envoyé avec succès!")
					}
					smtpTransport.close();
				});
    
    var sess = req.session;
    var connected = sess.user ? true : false;
    res.render('template_commun.ejs', { page : "CONTACT", user : sess.user, connected : connected });
});	

module.exports = router;