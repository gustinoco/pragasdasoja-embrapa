<?php

include_once __DIR__.'/../conf/connect.php';
include_once __DIR__.'/../model/Apresentacao.class.php';


class ApresentacaoDAO{

	function listar(){
		$conn = getConexao();
		$sql = "SELECT texto FROM Apresentacao";
		$result =  $conn->query($sql);
		while($row = $result->fetch(PDO::FETCH_OBJ)){
			$cat = new Apresentacao();
			$cat->mapear($row);
			$apresentacao = $cat;

		}
		return $apresentacao;
	}

}