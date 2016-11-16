<?php
/**CLASSE RESPONSÁVEL POR  ABSTRAIR ENVIO DE PRAGAS DECONHECIDAS
*/
include 'RetornoFoto.class.php';

class UsuarioRetorno{
	public $id;
	public $nomeUsuario;
	public $email;
	public $data;
	public $descricao;
	public $fotos;

     //MAPEIA ARRAY PARA OBJETO 
	function mapear($args){
          if(is_array($args)){
            $this->id 		=(int)$args['id'];
          	$this->nomeUsuario 	=$args['nome'];
          	$this->email 		=$args['email'];
          	$this->data 		=$args['data'];
          	$this->descricao 	=$args['descricao'];
               $this->fotos        =$this->mapearFotos($args['fotos']);

          }
          if($args instanceof stdClass){
               $this->id           =(int)$args->id;
               $this->nomeUsuario  =$args->nome;
               $this->email        =$args->email;
               $this->data         =$args->data;
               $this->descricao    =$args->descricao;
               $this->fotos        =$this->mapearFotos($args->fotos);
          }
     }
     function mapearFotos($args){
          $array = array();
          foreach ($args as $value) {
               $foto = new RetornoFoto();
               $foto->mapear($value);
               $array[] = $foto;
          }
          return $array;
     }
     function antSQLInjection(){
          $this->id =(int)mysql_escape_string($this->id);
          $this->nomeUsuario =mysql_escape_string($this->nomeUsuario);
          $this->email =mysql_escape_string($this->email);
          $this->data =mysql_escape_string($this->data);
          $this->descricao =mysql_escape_string($this->descricao);
     }
}

?>