<?php
include_once $_SERVER['DOCUMENT_ROOT'].'/conf/connect.php';
include_once $_SERVER['DOCUMENT_ROOT'].'/resource/libs/wideimage/WideImage.php';
include_once $_SERVER['DOCUMENT_ROOT'].'/model/UsuarioRetorno.class.php';
define('DIR_F',$_SERVER['DOCUMENT_ROOT'].'/resource/img/fotos/praga_desconhecida/');
define('DIR_T',$_SERVER['DOCUMENT_ROOT'].'/resource/img/fotos/praga_desconhecida/thumbnail/');
class UsuarioRetornoDAO{
	
	function deletar($id){
		try{
		if(!is_numeric($id))
			return;
		$conn=getConexao();
		$sql = 'DELETE FROM UsuarioRetorno WHERE id=:id';
		$stmt = $conn->prepare($sql);
		$stmt->execute(array(':id'=>$id));
		}catch(PDOException $e){
			echo '   Erro: '.$e->getMessage();	
		}
	}
	function listar(){
		$sql = 'SELECT * FROM UsuarioRetorno ORDER BY data DESC';
		$conn=getConexao();
		$result = $conn->query($sql);
		while($row = $result->fetch(PDO::FETCH_OBJ)){
			$f = new UsuarioRetorno();
			$f->mapear($row);
			$f->nomeUsuario = $row->nome;
			$array[]=$f;
		}
		return $array;
	}

	function salvar($user){

		$user->antSQLInjection();

		
		try{
			//cria conexao
			$conn = getConexao();

			$sql = "INSERT INTO UsuarioRetorno (nome,email,descricao, data) 
					VALUES (:nome, :email, :descricao,:data)";
			//prepara sql
			$stmt = $conn->prepare($sql);
			//INSERE DADOS
			$stmt->execute(array(
				':nome'=>$user->nomeUsuario,
				':email'=>$user->email,
				':data'=>date("Y-m-d H:i:s"),
				':descricao'=>$user->descricao
				));
			if(count($user->fotos)>0){
				$user->id = $conn->lastInsertId();
				$this->salvarFoto($user);
			}


		}catch(PDOException $e) {
		  return false;
		}
		return true;
	}

	function listarFotos($id){
		$sql = "SELECT id FROM UsuarioRetorno_Foto WHERE userRetorno_id=$id";
		$conn=getConexao();
		$result = $conn->query($sql);
		while ($row =  $result->fetch(PDO::FETCH_OBJ)) {
			$fotos[]=$row;
		}
		return $fotos;
	}

	//salva foto, id da praga e nome no campo descricao
	function salvarFoto($user){
		foreach ($user->fotos as $f) {
			$destino = DIR_F.$user->id.'_';
			$destino_t =DIR_T.$user->id.'_';
			if($f->foto!=null){

				//salva dados no BD para fazer recuperação das informaçoes para achar o caminho da foto
				$conn = getConexao();
				$sql = "INSERT INTO UsuarioRetorno_Foto (userRetorno_id) VALUES (:id)";
				$stmt=$conn->prepare($sql);
				$stmt->execute(array(':id'=>$user->id));
				$id_foto=$conn->lastInsertId();

				//salva foto no diretorio com o nome id concatenado com id da foto  que acabou de ser salvo no bd
				$destino.=$id_foto.'.jpeg';
				$destino_t.=$id_foto.'.jpeg';
				$img = WideImage::load($f->foto);
				$img->saveToFile($destino,100);

				//salva thumbnail
				$img = WideImage::load($f->foto);
				$img = $img->resize(50,50,'inside');
				$img->saveToFile($destino_t,90);
			}
		}
	}

}

?>