

var express = require('express');
var router = express.Router();
var connection = require('../db/dbconnection');

/* GET home page. */
router.get('/', function(req, response, next) {
  var nome = req.query.nome;
  var cod = req.query.quantidade;
  var q = 'insert into lista (nome,quantidade) values("'+nome+'",'+cod+')';
  console.log(q);
  connection.query(q, function (error, rows, fields) {
    response.writeHead(200, {
     'Content-Type': 'x-application/json'
    });
    // Send data as JSON string.
    // Rows variable holds the result of the query.
    response.end("ok");
  });



});

module.exports = router;

