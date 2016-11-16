<?php 
include_once 'Autor.class.php';

class Praga{
	
	public $id;		
    public $autor;
    public $categorias;
    public $nome;
    public $nomeCientifico;
    public $versaoPublicacao;
    public $dataCriacao;
    public $dataAlteracao;
    public $descricao;
    public $biologia;
    public $comportamento;
    public $danos;
    public $localizacao;
    public $controle;
    public $bibliografia;
    public $strThumbnail;
    public $descricao_1foto;
    public $pragaFotos = array();


    function mapear($args){
    	if(!isset($args))
            return;	
    	//inicializa valores, a chave de $args são os nome da coluna do Banco de Dados
        if(is_array($args)){
        	$this->id 				=(int)$args['id'];
        	$this->autor 			=new Autor($args['autor']);
    		$this->nome 			=$args['nome'];
    		$this->nomeCientifico	=$args['nome_cientifico'];
    		$this->versaoPublicacao	=(int)$args['versao_publicacao'];
    		$this->dataCriacao		=$args['data_criacao'];
    		$this->dataAlteracao	=$args['data_alteracao'];
    		$this->descricao		=$args['descricao'];
    		$this->biologia			=$args['biologia'];
    		$this->comportamento	=$args['comportamento'];
    		$this->danos 			=$args['danos'];
    		$this->localizacao		=$args['localizacao'];
    		$this->controle 		=$args['controle'];
    		$this->bibliografia		=$args['bibliografia'];
            $this->descricao_1foto  =$args['descricao_1foto'];
        }
        if($args instanceof stdClass){
            $this->id               =(int)$args->id;
            $this->autor            =new Autor($args->autor);//passa id do autor
            $this->nome             =$args->nome;
            $this->nomeCientifico   =$args->nome_cientifico;
            $this->versaoPublicacao =(int)$args->versao_publicacao;
            $this->dataCriacao      =$args->data_criacao;
            $this->dataAlteracao    =$args->data_alteracao;
            $this->descricao        =$args->descricao;
            $this->biologia         =$args->biologia;
            $this->comportamento    =$args->comportamento;
            $this->danos            =$args->danos;
            $this->localizacao      =$args->localizacao;
            $this->controle         =$args->controle;
            $this->bibliografia     =$args->bibliografia;
            $this->pragaFotos       =$args->fotos;
            $this->descricao_1foto  =$args->descricao_1foto;
        }

    }

    public function __set($atrib, $value){
        $this->$atrib = $value;
    }
    
    public function __get($atrib){
        return $this->$atrib;
    }
    
}

?>