<?php


 function arquivoParaBinario($src){
 	//le arquivo
	$arquivo = fopen($src, 'rb');
	//tamanho do arquivo
	$size = filesize($src);
	//ler arquivo , image to binario
	$bin = fread($arquivo, $size);
	//fecha arquivo
	fclose($arquivo);
	//retornar binario
	return $bin;
 }
?>