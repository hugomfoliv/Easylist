
var express = require('express');
var router = express.Router();
var connection = require('../db/dbconnection');

/* GET home page. */
router.get('/', function(req, response, next) {

	connection.query('select * from Produtos where data_validade >=cast(now() as date) and data_validade and data_validade <((now() + Interval 3 day));', function (error, rows, fields) {
   	 response.writeHead(200, {
     	'Content-Type': 'x-application/json'
    	});
   	 // Send data as JSON string.
   	 // Rows variable holds the result of the query.
    	response.end(JSON.stringify(rows));
  	});
});

module.exports = router;
