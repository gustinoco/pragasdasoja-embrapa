<?php

include_once __DIR__.'/../conf/connect.php';
include_once __DIR__.'/../model/Categoria.class.php';


class CategoriaDAO{

	function listar(){
		$conn = getConexao();
		$sql = "SELECT id, nome FROM Categoria";
		$result =  $conn->query($sql);
		while($row = $result->fetch(PDO::FETCH_OBJ)){
			$cat = new Categoria();
			$cat->mapear($row);
			$categorias[] = $cat;

		}
		return $categorias;
	}

}