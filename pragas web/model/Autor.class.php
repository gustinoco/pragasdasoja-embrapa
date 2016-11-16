<?php
class Autor{
	public $id;
    public $nome;
    public $email;
    public $telefone;
    public $telefone2;
    public $descricao;
    public $strFoto;

    function __construct($id){
    	$this->id = (int)$id;
    }

    function mapear($args){
        if($args==null) return;
        if(is_array($args)){
            $this->id       = (int)$args['id'];
            $this->nome     = $args['nome'];
            $this->email    = $args['email'];
            $this->telefone = $args['telefone'];
            $this->telefone2= $args['telefone2'];
            $this->descricao= $args['descricao'];
        }
        if($args instanceof stdClass){
            $this->id       = (int)$args->id;
            $this->nome     = $args->nome;
            $this->email    = $args->email;
            $this->telefone = $args->telefone;
            $this->telefone2= $args->telefone2;
            $this->descricao= $args->descricao;
        }
    }

}

?>