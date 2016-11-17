<div id="menu">
	<ul>
		<span id="titulo-menu"><b>Menu</b></span>
		<li><a href="/views/admin/consultar/praga.php">Gerenciar Pragas</a></li>
		<li><a href="../../../views/admin/consultar/autor.php">Gerenciar Autores</a></li>
		<li><a href="../../../views/admin/cadastrar/apresentacaoapp.php">Gerenciar Apresentação Mobile</a></li>
		<?php 
		if($_SESSION['usuarioPermissao']==1){ ?>
		<li><a href="../../../views/admin/consultar/usuario.php">Gerenciar Usuarios do Sistema</a></li>
		<?php }?>
	</ul>
	<ul>
		<li><a href="../../../views/admin/consultar/feedback.php">Visualizar feedback</a></li>
		<li><a href="../../../views/admin/consultar/praga_desconhecida.php">Visualizar envio de praga deconhecida</a></li>
	</ul>
	<center><a href="../../../views/admin/logout.php">Sair</a></center>

</div>