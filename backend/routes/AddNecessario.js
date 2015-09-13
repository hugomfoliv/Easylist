var express = require('express');
var router = express.Router();
var connection = require('../db/dbconnection');

/* GET home page. */
router.get('/', function(req, response, next) {
  var nome = req.query.nome;
  var qt = req.query.quantidade;
  console.log(nome);
  console.log(qt);
  var q='insert into Necessarios (nome,quantidade) values("'+nome+'",'+qt+');';
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

