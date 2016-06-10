var express = require("express");
var request = require('request');
var exphbs = require('express-handlebars');
var helpers = require('./public/js/helpers.js');
var melee = require('./public/js/melee.js');
var app = express();
var router = express.Router();
var htmlpath = __dirname + '/views/';
var csspath = __dirname + '/css/';
var api = 'http://meleeauthority.com:8080/';

var hbs = exphbs.create({extname: '.html', defaultLayout: 'default.html'});

hbs.helpers = helpers.get(hbs);

app.set('views', __dirname + '/views');
app.engine('html', hbs.engine);
app.set('view engine', 'html');
app.set('port', 80);

router.use(function (req,res,next) {
  console.log(req.path);
  next();
});

router.get("/",function(req,res){
  res.render('index', {title: 'Melee Authority'});
});

router.get("/characters",function(req,res){
  request(api + '/character',
    function (error, response, body) {
    if (!error && response.statusCode == 200) {
      var settings = {normaltd: JSON.parse(response.body)};
      settings.title = 'Characters';
      settings.character = true;
      settings.base = true;
      console.log(req.originalUrl);
      settings.url = req.originalUrl;
      for (var a in req.params) { settings[a] = req.params[a]; }
      res.render("allcharacters", settings);
    } else {
      response = response || {statusCode: 404};
      console.log(error);
      res.status(response.statusCode).render(
        '404',
        {title: 'API Call Failed',
        body: api + ' returned error code ' + response.statusCode});
    }
  });
});

router.get("/characters/:character",function(req,res){
  request(api + '/character?charId=' + melee.getCharId(req.params.character),
    function (error1, response1, body1) {
    request(api + '/move?charId=' + melee.getCharId(req.params.character), 
      function (error2, response2, body2) {
        if (!error1 && response1.statusCode == 200 &&
            !error2 && response2.statusCode == 200) { 
          var settings = {verticaltd: JSON.parse(response1.body)};
          settings.normaltd = JSON.parse(response2.body);
          settings.title = settings.verticaltd[0].name;
          settings.url = req.originalUrl;
          for (var a in req.params) { settings[a] = req.params[a]; }
          res.render("character", settings);
        } else {
          console.log(error);
          res.status(404).render("404", {title: 'Page Not Found'});
        }
      });
    });
});

router.get("/characters/:character/moves",function(req,res){
  request(api + '/move?charId=' + melee.getCharId(req.params.character),
    function (error, response, body) {
    if (!error && response.statusCode == 200) {
      var settings = {td: JSON.parse(response.body)};
      settings.title = req.params.character + ' - Moves';
      for (var a in req.params) { settings[a] = req.params[a]; }
      res.render("allmoves", settings);
    } else {
      console.log(error);
      res.status(404).render("404", {title: 'Page Not Found'});
    }
  });
});

router.get("/characters/:character/:move",function(req,res){
  request(api + '/move?charId=' + melee.getCharId(req.params.character) +
    '&animation=' + req.params.move,
    function (error, response, body) {
    if (!error && response.statusCode == 200) {
      var settings = {verticaltd: melee.getFrameStrip(JSON.parse(response.body))};
      settings.title = req.params.character + ' - ' + req.params.move;
      for (var a in req.params) { settings[a] = req.params[a]; }
      res.render("move", settings);
    } else {
      console.log(error);
      res.status(404).render("404", {title: 'Page Not Found'});
    }
  });
});

router.get("/moves",function(req,res){
  request(api + '/character',
    function (error, response, body) {
    if (!error && response.statusCode == 200) {
      var settings = {td: JSON.parse(response.body)};
      settings.title = 'Moves';
      settings.move = true;
      settings.base = true;
      for (var a in req.params) { settings[a] = req.params[a]; }
      res.render("allmoves", settings);
    } else {
      console.log(error);
      res.status(404).render("404", {title: 'Page Not Found'});
    }
  });
});

router.get("/moves/:move",function(req,res){
  request(api + '/character',
    function (error, response, body) {
    if (!error && response.statusCode == 200) {
      var settings = {td: JSON.parse(response.body)};
      settings.title = req.params.move;
      for (var a in req.params) { settings[a] = req.params[a]; }
      res.render("move", settings);
    } else {
      console.log(error);
      res.status(404).render("404", {title: 'Page Not Found'});
    }
  });
});

router.get("/contact",function(req,res){
  res.render("contact", {title: 'Contact Us', contact: true});
});

app.use("/",router);
app.use('/public/*.js', express.static(__dirname + '/public/js/custom.js'));
app.use('/public/*.css', express.static(__dirname + '/public/css/custom.css'));
app.use('/scripts', express.static(__dirname + '/node_modules/bootstrap/dist/js/'));
app.use('/css', express.static(__dirname + '/node_modules/bootstrap/dist/css/'));

app.use("*",function(req,res){
  res.status(404).render("404", {title: 'Page Not Found'});
});

app.listen(80,function(){
  console.log("Live at Port 80");
});


