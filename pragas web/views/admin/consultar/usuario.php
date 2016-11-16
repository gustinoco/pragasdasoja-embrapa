<?php 

include '../restrito.php';
include '../../template/cabecalho.php';
include $_SERVER['DOCUMENT_ROOT'].'/dao/UsuarioSistemaDAO.class.php';

if($_SESSION['usuarioPermissao']!=1){
	header('Location: ../admin.php');
	exit;
}
$dao = new UsuarioSistemaDAO();

//se houver id no POST significa que usuario deseja deletar outro Usuario
if($_POST['id']>0){
	$dao->deletar($_POST['id']);
}
//lista todos os usuarios
$usuarios = $dao->listar();

?>


<!-- CRIA MENU LATERAL -->
<div class="large-3 columns">
	<div class="">
		<?php include '../../plugin/menu_lateral_admin.php'; ?>
	</div>
</div>
<!-- FIM MENU LATERAL -->

<!-- PARTE DE CADASTRO -->
<div class="large-9 columns"><div class="panel">
	<center><h4 id="titulo" class="panel">Consultando Usuarios do Sistema</h4></center>
	<a class="bt-cadastrar" href="../cadastrar/usuario.php"> + Cadastrar novo Usuario</a>
	<table style="width:100%;">
		<tr>
			<th id="col-tab">id</th>
			<th id="col-tab">nome</th>	
			<th id="col-tab">permissao</th>	
			<th id="col-tab">Ultimo acesso</th>	
			<th id="col-tab"><center>editar</center></th>	
			<th id="col-tab"><center>remover</center></th>	
		</tr>
		<?php
		if($usuarios)
			foreach ($usuarios as $usr) {?>
		<tr>
			<td><?php echo $usr->id ;?></td>
			<td><?php echo $usr->login ;?></td>
			<!--PERMISSAO-->
			<?php 
			if($usr->permissao==1)
				echo '<td>administrador</td>';
			else
				echo '<td>normal</td>';?>
			<td><?php echo $usr->data_ultimo_acesso;?></td>
			<!--FORMULARIO EDITAR-->
			<form action="../cadastrar/usuario.php" method="post" >
				<input type="hidden" name="id" value=<?php echo "'$usr->id'";?>>
				<td><center><input type="submit" class="button bt-editar" value=">"></input></center></td>
			</form>
			<!--FORMULARIO REMOVER-->
			<form action="" method="post" onSubmit="return deletar()">
				<input type="hidden" name="id" value=<?php echo "'$usr->id'";?>>
				<td><center><input type="submit" class="button bt-remover" value="x"></input></center></td>
			</form>
		</tr>
		<?php }?>
	</table>
</div></div>
<?php
include '../../template/rodape.php';
?>	
