<?php
class UsuarioSistema{
	public $id;
	public $login;
	public $senha;
	public $data_ultimo_acesso;
	public $data_cadastro;
	public $permissao;

	function mapear($args){
		if(is_array($args)){
			$this->id= $args['id'];
			$this->login=$args['login'];
			$this->senha=$args['senha'];
			$this->data_ultimo_acesso=$args['data_ultimo_acesso'];
			$this->data_cadastro=$args->$args['data_cadastro'];
			$this->permissao=$args['permissao'];
		}
		if($args instanceof stdClass){
			$this->id=$args->id;
			$this->login=$args->login;
			$this->senha=$args->senha;
			$this->data_ultimo_acesso=$args->data_ultimo_acesso;
			$this->data_cadastro=$args->data_cadastro;
			$this->permissao=$args->permissao;
		}
	}
}
?>
