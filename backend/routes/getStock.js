var express = require('express');
var router = express.Router();
var connection = require('../db/dbconnection');

/* GET home page. */
router.get('/', function(req, response, next) {
  connection.query('SELECT nome,COUNT(*) as quantidade FROM Produtos GROUP BY nome ORDER BY quantidade DESC;', function (error, rows, fields) {
    response.writeHead(200, {
     'Content-Type': 'x-application/json'
    });
    // Send data as JSON string.
    // Rows variable holds the result of the query.
    response.end(JSON.stringify(rows));
  });



});

module.exports = router;


