<?php
$titulo = 'Login';
include __DIR__.'/../template/cabecalho.php';
?>
<div class="row large-4">
	<form action="validacao.php" method="post">
		<center>
			<fieldset class="panel">
				<legend>Login para área administrativa</legend>
				<label for="txtUsuario">Usuario:</label>
				<input id="txtUsuario" name="usuario" type="text" maxlength="25"/>
				<label for="txtSenha">Senha:</label>
				<input id="txtSenha" name="senha" type="password" />

				<input class="button" type="submit" value="Entrar"/>
			</fieldset>
		</center>
	</form>
</div>
<?php
include __DIR__.'/../template/rodape.php';
?>


