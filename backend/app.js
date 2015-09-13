var express = require('express');
var path = require('path');
var logger = require('morgan');
var bodyParser = require('body-parser');

//LER AQUI!!!!!!!!
//crias o ficheiro .js na pasta das rotas e inicializas aqui
var getProdutosSupermercado = require('./routes/getProdutosSupermercado');
var getStock = require('./routes/getStock');
var getForaValidade = require('./routes/getForaValidade');
var getNecessarios = require('./routes/getNecessario'); /////////////////
var AddItem = require('./routes/AddItem');
var AddNecessario = require('./routes/AddNecessario'); //////////////////
var RemoveItem = require('./routes/RemoveItem');
var UpdateNecessario = require('./routes/UpdateNecessario');////////
var getAlmostForaValidade = require('./routes/getAlmostForaValidade');
var addLista = require('./routes/addLista');
var removeLista = require('./routes/removeLista');

var app = express();



// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));


//LER AQUI!!!!!!!!
//Aqui defines primeiro a rota e passas a variável inicializada em cima
//depois o ficheiro que esta dentro das rotas e sempre a mesma cena
//meti-te a coneção a bd também num ficheiro /db/dbconnection.js) à parte assim é mais fácil
//vê no getprodutossupermercado.js como fiz :)
app.use('/getProdutosSupermercado', getProdutosSupermercado);
app.use('/getStock', getStock);
app.use('/getForaValidade', getForaValidade);
app.use('/AddItem', AddItem);
app.use('/RemoveItem', RemoveItem);
app.use('/getNecessario', getNecessarios);
app.use('/AddNecessario', AddNecessario);
app.use('/UpdateNecessario',UpdateNecessario);
app.use('/GetAlmostForaValidade',getAlmostForaValidade);
app.use('/AddLista',addLista);
app.use('/RemoveLista',removeLista);



// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// error handlers

// development error handler
// will print stacktrace
if (app.get('env') === 'development') {
  app.use(function(err, req, res, next) {
    res.status(err.status || 500);
    res.render('error', {
      message: err.message,
      error: err
    });
  });
}

// production error handler
// no stacktraces leaked to user
app.use(function(err, req, res, next) {
  res.status(err.status || 500);
  res.render('error', {
    message: err.message,
    error: {}
  });
});


module.exports = app;
