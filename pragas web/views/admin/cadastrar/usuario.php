<?php 

require_once '../restrito.php';
include '../../template/cabecalho.php';
include '../../../dao/UsuarioSistemaDAO.class.php';

$dao = new UsuarioSistemaDAO();

$titulo='Cadastrando novo usuario';

/** VERIFICA SE FOI PASSADO ALGUM ID POR POST, SE SIM, ENTÃO USUARIO DESEJA ALTERAR O CONTEÚDO*/
if(isset($_POST['id']) AND is_numeric($_POST['id'])){
	$dao=new UsuarioSistemaDAO();
	$usuario = $dao->buscar($_POST['id']);
	$titulo='Editando usuario "'.$usuario->login.'"';
}

?>
<!-- CONFERE CAMPOS VAZIOS -->
<script type="text/javascript">

    function confere() {
    	if ( document.getElementById("login").value == "")  {      
	        alert("Por favor, preencha o login."); 
	        return false;
	    }
	    if ( document.getElementById("senha").value == "")  {      
	        alert("Por favor, preencha a senha."); 
	        return false;
	    }
	    return true;
    }
</script>
<!-- SCRIPT VERIFICA CAMPOS VAZIOS -->

<!-- CRIA MENU LATERAL -->
<div class="large-3 columns">
	<div class="">
		<?php include '../../plugin/menu_lateral_admin.php'; ?>
	</div>
</div>
<!-- FIM MENU LATERAL -->

<!-- PARTE DE CADASTRO -->
<div class="large-9 columns">
	<center><h4 id="titulo" class="panel"><?php echo $titulo?></h4></center>
	<form action="salvar_usuario.php" method="post" onSubmit="return confere()" >
		<!-- ID, só existirá se estiver editando, portando fica escondido. tipo 'hidden'-->
		<input type="hidden" name="id" value=<?php echo "'$usuario->id'";?>>
		<div class="large-5 columns">
			<!-- LOGIN -->
			<?php 
			if($usuario->login=='admin'){
				$enabled="disabled";
				$chk='disabled ';
				$chk='disabled '; ?>
				<input type="hidden" name="login" value="admin">
				<input type="hidden" name="permissao" value="1">
			<?php }?>
			<label>Login:
			<input  id="login" name="login" type="text" placeholder="nome para login" 
				value=<?php echo "'$usuario->login'"; echo $enabled?> ></input></label>


			<!-- SENHA -->
			<label>Senha:
			<input id="senha" name="senha" type="password" ></input></label>

			<!--PERMISSAO-->
			<label>Nível de permissão</label>
			<?php 
			if($usuario->permissao==1) {
				$chk.=' checked';
			}else
				$chk2.=' checked';
			?>
			<input id="cbox1" name="permissao" type="radio" value="1" <?php echo $chk;?>>
			<label for="cbox1">Administrador</label>
			<input id="cbox2" name="permissao" type="radio" value="0" <?php echo $chk2;?>>
			<label for="cbox2">Normal</label>
			<!--SALVAR-->
			<center><input  class="button bt-salvar" type="submit" value="Salvar"></center>
		</div>	
	</form>
</div>
<?php
include '../../template/rodape.php';
?>