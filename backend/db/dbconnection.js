var mysql = require("mysql");

var connection = mysql.createConnection({
    user: "root",
    password: "1234",
    database: "easylist"
});

module.exports = connection;
