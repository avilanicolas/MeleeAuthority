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
      var settings = {datatabletd: melee.parseChar(JSON.parse(response.body))};
      settings.title = 'Characters';
      settings.character = true;
      settings.base = true;
      settings.url = melee.parseUrl(req.originalUrl);
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
    request(api + '/hitbox?charId=' + melee.getCharId(req.params.character), 
      function (error2, response2, body2) {
        if (!error1 && response1.statusCode == 200 &&
            !error2 && response2.statusCode == 200) { 
          var settings = {verticaltd: melee.parseChar(JSON.parse(response1.body))};
          settings.hitboxtd = melee.parseMove(new Array(JSON.parse(response2.body)));
          settings.title = settings.verticaltd[0].name;
          settings.url = melee.parseUrl(req.originalUrl);
          for (var a in req.params) { settings[a] = req.params[a]; }
          res.render("character", settings);
        } else {
          if (error1) {
            console.log(error1);
          }
          if (error2) {
            console.log(error2);
          }
          res.status(404).render("404", {title: 'Page Not Found'});
        }
      });
    });
});

router.get("/characters/:character/:move",function(req,res){
  request(api + '/move?charId=' + melee.getCharId(req.params.character) +
    '&animation=' + melee.getMove(req.params.move),
    function (error1, response1, body1) {
    request(api + '/hitbox?charId=' + melee.getCharId(req.params.character) +
      '&animation=' + melee.getMove(req.params.move),
      function (error2, response2, body2) {
        if (!error1 && response1.statusCode == 200 &&
            !error2 && response2.statusCode == 200) { 
          var settings = {verticaltd: melee.getFrameStrip(JSON.parse(response1.body))};
          settings.hitboxtd = melee.parseMove(new Array(JSON.parse(response2.body)));
          settings.title = req.params.character + ' - ' + req.params.move;
          settings.hitboxtd[1] = true;
          for (var a in req.params) { settings[a] = req.params[a]; }
          res.render("move", settings);
        } else {
          if (error1) {
            console.log(error1);
          }
          if (error2) {
            console.log(error2);
          }
          res.status(404).render("404", {title: 'Page Not Found'});
        }
      });
    });
});

router.get("/builder",function(req,res){
  res.render("builder", {title: 'Frame Builder', builder: true});
});

router.get("/analysis",function(req,res){
  res.render("analysis", {title: 'Contact Us', analysis: true});
});

router.get("/contact",function(req,res){
  res.render("contact", {title: 'Contact Us', contact: true});
});

// required for jquery to access api
router.get('/public/json/characterIds.json', function(req, res) {
  res.sendFile(__dirname + '/public/json/characterIds.json');
});

// required for jquery to access api
router.get('/move', function(req, res) {
  request(api + '/move?charId=' + req.query.charId,
    function (error, response, body) {
      res.json(JSON.parse(response.body));
    });
});

app.use("/",router);
app.use('/public/js/custom.js', express.static(__dirname + '/public/js/custom.js'));
app.use('/public/js/helpers.js', express.static(__dirname + '/public/js/helpers.js'));
app.use('/public/css/custom.css', express.static(__dirname + '/public/css/custom.css'));
app.use('/public/json/characterIds.json', express.static(__dirname + '/public/json/characterIds.json'));
app.use('/scripts', express.static(__dirname + '/node_modules/bootstrap/dist/js/'));
app.use('/css', express.static(__dirname + '/node_modules/bootstrap/dist/css/'));

app.use("*",function(req,res){
  res.status(404).render("404", {title: 'Page Not Found'});
});

app.listen(80,function(){
  console.log("Live at Port 80");
});


