<?php

// Evita que usuários acesse este arquivo diretamente
//if (!defined('ABSPATH')) exit;

//Constantes para conexão

define("PORT","3306");
define("DB","PRAGAS_DO_CAMPO");
define("END","mysql.cpao.embrapa.br");
define("USER","pragas");
define("PASS","pragas15cpao");

//Conexão com o servidor
function getConexao(){
	$conn = new PDO( 'mysql:host='.END.';port='.PORT.';dbname='.DB.';charset=utf8',
								USER,
								PASS);
	$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	
	return $conn;
}

function tratar_entrada($entrada){
	return mysql_escape_string($entrada);
}


?>
