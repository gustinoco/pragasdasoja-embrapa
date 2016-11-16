<?php 

class Categoria{
	public $id;
	public $nome;

	function __construct($args){
		if(!isset($args)) 
			return;
		$this->id 	= (int)$args['id'];
		$this->nome = $args['nome'];
	}
	function mapear($args){
		if (is_array($args)){
			$this->id 	= (int)$args['id'];
			$this->nome = $args['nome'];		
		}
		if($args instanceof stdClass){
			$this->id 	= (int)$args->id;
			$this->nome = $args->nome;			
		}
	}
}

?>