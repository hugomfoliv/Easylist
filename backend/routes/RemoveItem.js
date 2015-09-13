var express = require('express');
var router = express.Router();
var connection = require('../db/dbconnection');

/* GET home page. */
router.get('/', function(req, response, next) {
	var id=req.query.codigo_barras;
	connection.query('DELETE FROM Produtos where cod_Barras='+id+';', function (error, rows, fields) {
    	response.writeHead(200, {
     	'Content-Type': 'x-application/json'
    	});
    	// Send data as JSON string.
    	// Rows variable holds the result of the query.
   	 response.end("ok");
  	});

});

module.exports = router;
