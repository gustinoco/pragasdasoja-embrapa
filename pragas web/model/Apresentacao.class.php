<?php 
class Apresentacao{
	public $texto;


	function __construct($args){
		if(!isset($args)) 
			return;
		$this->texto 	= $args['texto'];		
	}
	function mapear($args){
		if (is_array($args)){
			$this->texto 	= $args['texto'];			
		}
		if($args instanceof stdClass){		
			$this->texto = $args->texto;			
		}
	}
}

?>