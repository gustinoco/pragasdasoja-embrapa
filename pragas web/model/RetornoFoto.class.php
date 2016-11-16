<?php
class RetornoFoto{
	public $id;
	public $usuarioRetorno; //id do usuario retorno
	public $foto;

	function mapear($args){
		if(is_array($args)){
			$this->id=(int)$args['id'];
			//$this->usuarioRetorno=$args['usuarioRetorno'];
			$this->foto=base64_decode($args['strFoto']);
		}
		if($args instanceof stdClass){
			$this->id=(int)$args->id;
			//$this->usuarioRetorno=$args->usuarioRetorno;
			$this->foto=base64_decode($args->strFoto);
		}
	}
}
?>