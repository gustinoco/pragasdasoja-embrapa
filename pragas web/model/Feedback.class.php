<?php

class Feedback{

	public $id;
     public $nomeUsuario;
     public $email;
     public $data;
     public $descricao;
     public $classificacao;


     function mapear($args){
          if(is_array($args)){
               $this->id 		=(int)$args['id'];
          	$this->nomeUsuario 	=$args['nomeUsuario'];
          	$this->email 		=$args['email'];
          	$this->data 		=$args['data'];
          	$this->descricao 	=$args['descricao'];
          	$this->classificacao=$args['classificacao'];
          }
          if($args instanceof stdClass){
               $this->id           =(int)$args->id;
               $this->nomeUsuario  =$args->nomeUsuario;
               $this->email        =$args->email;
               $this->data         =$args->data;
               $this->descricao    =$args->descricao;
          }
     }
     function antSQLInjection(){
          $this->id =(int)mysql_escape_string($this->id);
          $this->nomeUsuario =mysql_escape_string($this->nomeUsuario);
          $this->email =mysql_escape_string($this->email);
          $this->data =mysql_escape_string($this->data);
          $this->descricao =mysql_escape_string($this->descricao);
          $this->classificacao =mysql_escape_string($this->classificacao);
     }
}
?>