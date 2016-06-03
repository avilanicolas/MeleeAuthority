var express = require("express");
var request = require('request');
var exphbs = require('express-handlebars');
var helpers = require('./public/js/helpers.js');
var app = express();
var router = express.Router();
var htmlpath = __dirname + '/views/';
var csspath = __dirname + '/css/';

var hbs = exphbs.create({extname: '.html', defaultLayout: 'default.html'});

hbs.helpers = helpers.get(hbs);

app.set('views', __dirname + '/views');
app.engine('html', hbs.engine);
app.set('view engine', 'html');
app.set('port', process.env.PORT || 3000);

router.use(function (req,res,next) {
  console.log("req baseUrl: " + req.baseUrl);
  console.log("req body: " + req.body);
  console.log("req originalUrl: " + req.originalUrl);
  console.log("req params: " + req.params);
  console.log("req path: " + req.path);
  console.log("req query: " + req.query);
  console.log("/" + req.method);
  next();
});

router.get("/",function(req,res){
  res.render('index', {title: 'Melee Authority'});
});

router.get("/characters",function(req,res){
  request('http://jsonplaceholder.typicode.com/posts?userId=1', // API CALL GOES HERE
    function (error, response, body) {
    if (!error && response.statusCode == 200) {
      var settings = {td: JSON.parse(response.body)};
      settings.title = 'Characters';
      settings.character = true;
      settings.base = true;
      for (var a in req.params) { settings[a] = req.params[a]; }
      res.render("allcharacters", settings);
    } else {
      console.log("ErroR: " + error + " status " + response.statusCode);
    }
  });
});

router.get("/characters/:character",function(req,res){
  request('http://jsonplaceholder.typicode.com/posts?userId=1',
    function (error, response, body) {
    if (!error && response.statusCode == 200) {
      var settings = {td: JSON.parse(response.body)};
      settings.title = req.params.character;
      for (var a in req.params) { settings[a] = req.params[a]; }
      res.render("character", settings);
    }
  });
});

router.get("/characters/:character/moves",function(req,res){
  request('http://jsonplaceholder.typicode.com/posts?userId=1',
    function (error, response, body) {
    if (!error && response.statusCode == 200) {
      var settings = {td: JSON.parse(response.body)};
      settings.title = req.params.character + ' - Moves';
      for (var a in req.params) { settings[a] = req.params[a]; }
      res.render("allmoves", settings);
    }
  });
});

router.get("/characters/:character/:move",function(req,res){
  request('http://jsonplaceholder.typicode.com/posts?userId=1',
    function (error, response, body) {
    if (!error && response.statusCode == 200) {
      var settings = {td: JSON.parse(response.body)};
      settings.title = req.params.character + ' - ' + req.params.move;
      for (var a in req.params) { settings[a] = req.params[a]; }
      res.render("move", settings);
    }
  });
});

router.get("/moves",function(req,res){
  request('http://jsonplaceholder.typicode.com/posts?userId=1',
    function (error, response, body) {
    if (!error && response.statusCode == 200) {
      var settings = {td: JSON.parse(response.body)};
      settings.title = 'Moves';
      settings.move = true;
      settings.base = true;
      for (var a in req.params) { settings[a] = req.params[a]; }
      res.render("allmoves", settings);
    }
  });
});

router.get("/moves/:move",function(req,res){
  request('http://jsonplaceholder.typicode.com/posts?userId=1',
    function (error, response, body) {
    if (!error && response.statusCode == 200) {
      var settings = {td: JSON.parse(response.body)};
      settings.title = req.params.move;
      for (var a in req.params) { settings[a] = req.params[a]; }
      res.render("move", settings);
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

app.listen(3000,function(){
  console.log("Live at Port 3000");
});


