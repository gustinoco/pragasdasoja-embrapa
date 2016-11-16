<?php

// Evita que usuários acesse este arquivo diretamente
//if (!defined('ABSPATH')) exit;

//Constantes para conexão

//dev
define("END","localhost");
define("PORT","3306");
define("DB","PRAGAS_DO_CAMPO");
define("USER","root");
define("PASS","bola1993");

//produção
/*define("PORT","3306");
define("DB","PRAGAS_DO_CAMPO");
define("END","mysql.cpao.embrapa.br");
define("USER","pragas");
define("PASS","pragas15cpao");*/



//Conexão com o servidor

function getConexao(){
	$conn = new PDO( 'mysql:host='.END.';port='.PORT.';dbname='.DB.';charset=utf8',
								USER,
								PASS);
	return $conn;
}

function listar_pragas(){
	$conn = getConexao();
	$sql = 'SELECT p.id, p.nome, p.nome_cientifico, f.foto
	                    FROM Praga p
	                      LEFT JOIN Praga_Foto f
	                      ON p.id=f.praga_id
	                    GROUP BY p.nome';
	$result = $conn->query($sql);
	return $result;
}
function filtrar_pragas($strPesquisa){
	$conn = getConexao();
	$strPesquisa = tratar_entrada($strPesquisa);
	$strPesquisa.='%';
	$sql = "SELECT p.id, p.nome, p.descricao, f.foto
	                    FROM Praga p
	                      LEFT JOIN Praga_Foto f
	                      ON p.id=f.praga_id
	                    WHERE p.nome LIKE '$strPesquisa'
	                    GROUP BY p.nome";
	$result = $conn->query($sql);
	return $result;
}
function buscar_praga($id){
	$conn=getConexao();
	$id = tratar_entrada($id);
	$praga = $conn->query("SELECT *
                                FROM Praga
                                WHERE id = $id LIMIT 1
                              ");
	return $praga;
}
function buscar_fotos($id_praga){
	$conn=getConexao();
	$id_praga = tratar_entrada($id_praga);

	$fotos = $conn->query("SELECT f.foto, f.id
                                FROM Praga p, Praga_Foto f 
                                WHERE p.id=f.praga_id AND f.praga_id=$id_praga"
                              );
	return $fotos;
}

//Essa função não faz mais sentido, é usado prepared statement no PDO.
function tratar_entrada($entrada){
	return $entrada;
}


?>
