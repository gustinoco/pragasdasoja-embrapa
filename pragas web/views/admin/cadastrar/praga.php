<?php 
/**
* Página responsável por cadastrar uma nova praga, está faltando implementação melhor 
* no cadastro das fotos, falta implementar uma forma de limitar a qtd de fotos para 10
* não podendo passar disso pois celulares com android 2.3 que irão fazer o download dos dados
* não vão suportar qtd de fotos mt grandes para cada praga.
*/
require_once '../restrito.php';
include_once $_SERVER['DOCUMENT_ROOT'].'/dao/AutorDAO.class.php';
include_once $_SERVER['DOCUMENT_ROOT'].'/dao/PragaDAO.class.php';
include_once $_SERVER['DOCUMENT_ROOT'].'/dao/CategoriaDAO.class.php';
include_once '../../template/cabecalho.php';
define('DIR_T','../../../resource/img/thumbnail/');
define('DIR_F','../../../resource/img/fotos/');

$daoA = new AutorDAO();
//$daoC= new CategoriaDAO();
/**lista autores e categorias para colocar no select box*/
$autores = $daoA->listar_info_basico();
//$categorias = $daoC->listar();

$titulo='Cadastrar Nova Praga';

/** VERIFICA SE FOI PASSADO ALGUM ID POR POST, SE SIM, ENTÃO USUARIO DESEJA ALTERAR O CONTEÚDO*/
if(isset($_GET['id']) AND is_numeric($_GET['id'])){
	$dao=new PragaDAO();
	$praga = $dao->buscar_praga($_GET['id']);
	$praga->id=$_GET['id'];
	$titulo='Editando praga "'.$praga->nome.'"';

	//busca fotos da praga
	$fotos = $dao->buscar_fotos($_GET['id']);
	//GERA FOTOS
    $imagens="";
    //GERA GALERIA DE FOTOS DA PRAGA
    if($fotos){
      
      foreach ($fotos as $foto) {
          //recupera caminho da foto
  		  $nome_img = $praga->id.'-'.$foto->id.'.jpg';
          $src_t = DIR_T.$nome_img;
          $src_f = DIR_F.$nome_img;
          $imagens .= '<li>';
          $imagens .= '<div class="content-foto">';
          $imagens .= '<input type="checkbox" name="fotos[]" value="'.$foto->id.'">' ;
          $imagens .= "<img class='thumb' src= '$src_t'/>";   
          $imagens .= "<input type='text' name='descricao_foto$foto->id' value='$foto->descricao' disabled>";
          $imagens .= "<input type='text' name='fotografo_foto$foto->id' value='$foto->fotografo' disabled>";
          $imagens .= "</div>";
          $imagens .= '</li>';
      }//FIM WHILE
    }//FIM IF
}
?>
<!-- CONFERE CAMPOS VAZIOS -->
<script type="text/javascript">

    function confere() {
    	if ( document.getElementById("nome").value == "")  {      
	        alert("Por favor, preencha o nome."); return false;
	    }
	    return true;
    }
</script>
<!-- SCRIPT VERIFICA CAMPOS VAZIOS -->
<!-- SCRIPT DA GALERIA DE FOTOS -->


<div class="large-3 columns">
	<div class="">
		<?php include '../../plugin/menu_lateral_admin.php'; ?>
	</div>
</div>

<div class="large-9 columns">
	<center><h4 id="titulo" class="panel"><?php echo $titulo?></h4></center>

	<!-- GALERIA -->
	<?php if($praga->id){ //aparecer galeria apenas se estiver editando ?>

	<form action="remover_fotos.php" method="post" onSubmit="return deletarfotos()" enctype="multipart/form-data">
		<h3>Fotos: </h3>
		<input type="hidden" name="id" value=<?php echo "'$praga->id'";?>></input>
		<ul style=" margin:0 auto;" data-clearing> 
	        <?php echo $imagens;?>
	    </ul>
	    <input  class="" type="submit" value="Remover Fotos selecionadas">
    </form>

    <?php } ?>
    <br>
	<form action="salvar_praga.php" method="post" onSubmit="return confere()" enctype="multipart/form-data">
		<input type="hidden" name="id" value=<?php echo "'$praga->id'";?>></input>
		<input type="hidden" name="versao_publicacao" value=<?php echo "'$praga->versaoPublicacao'";?>></input>
		
		<!-- FOTOS -->
		<div class="row">
			<div class="large-12 columns">
				<h3>Adicionar novas fotos (.jpg, .png, .jpeg)</h3>
				<div id="selector">
				    <input type="file" id="fotos" name="fotos[]" multiple/>
			    </div>
			    <br>
				<div id="galeria" class="galeria"></div>
			</div>
		</div>
		<div class="row">
			<div class="large-5 columns">			
				
				<!-- NOME -->
				<label>Nome:
					<input  id="nome" name="nome" type="text" placeholder="nome da praga" 
							value=<?php echo '"'.$praga->nome.'"';?>></input>
				</label>
				<!-- NOME CIENTIFICO -->
				<label>Nome científico:
					<input  id="nome_cientifico" name="nome_cientifico" type="text" placeholder="nome da praga" 
							value=<?php echo '"'.$praga->nomeCientifico.'"';?>></input>
				</label>
				<!-- AUTOR -->
				
				<label>Autor(pesquisador):<br>
					<select name="autor">
						<?php //gera options para select box
						foreach ($autores as $autor) {
							//verifica item se deve ser selecionado
							if($praga->autor->id==$autor->id)
								$select='selected';
							else
								$select='';
							echo "<option value='$autor->id' $select>$autor->id - $autor->nome</option>";
						}?>
					</select>
				</label>
				<?php /**<!-- CATEGORIA -->
				<label>Categoria:<br>
					<select>
						<?php 
						foreach ($categorias as $categoria) {
							echo "<option value='$categoria->id'>$categoria->nome</option>";	
						}?>
					</select>
				</label>*/?>
			</div>
		</div>
		<div class="row">
			<div class="large-12 columns">
				<!-- DESCRICAO -->
				<br><label>Descrição:
				<textarea id="descricao" name="descricao" style="width:100%">
					<?php echo $praga->descricao;?>
				</textarea></label>
				<!-- BIOLOGIA -->
				<br><label>Biologia:
				<textarea id="biologia" name="biologia" style="width:100%">
					<?php echo $praga->biologia;?>
				</textarea></label>
				<!-- COMPORTAMENTO -->
				<br><label>Comportamento:
				<textarea id="comportamento" name="comportamento" style="width:100%">
					<?php echo $praga->comportamento;?>
				</textarea></label>
				<!-- DANOS -->
				<br><label>Danos:
				<textarea id="danos" name="danos" style="width:100%">
					<?php echo $praga->danos;?>
				</textarea></label>
				<!-- LOCALIZACAO -->
				<br><label>Localização:
				<textarea id="localizacao" name="localizacao" style="width:100%">
					<?php echo $praga->localizacao;?>
				</textarea></label>
				<!-- CONTROLE -->
				<br><label>Controle:
				<textarea id="controle" name="controle" style="width:100%">
					 <?php echo $praga->controle;?>
				</textarea></label>
				<!-- LITERATURA RECOMENTADADA -->
				<br><label>Literatura recomendada:
				<textarea id="bibliografia" name="bibliografia" style="width:100%">
					<?php echo $praga->bibliografia;?>
				</textarea></label>
			</div>
		</div>
		
		<center><input  id="btSalvar" class="button bt-salvar" type="submit" value="Salvar"></center>
	</form>
</div>

<?php
include '../../template/rodape.php';
?>

<!-- SCRIPT GERAR EDITORES DE TEXO -->
<script type="text/javascript" src="../../../resource/js/tinymce/tinymce.min.js"></script>
<script type="text/javascript">
//desabilita botao de salvar ao ser clicado
$("#btSalvar").click(function (){
                // desabilita o campo 
        $("#btSalvar").prop("disabled", true);

});
tinymce.init({
	selector: "textarea",
	language: "pt_BR",
	theme: "modern",
	plugins: [
	"advlist autolink lists link charmap print preview hr pagebreak",
	"searchreplace wordcount visualblocks visualchars code fullscreen",
	"insertdatetime  nonbreaking save table contextmenu directionality",
	"emoticons template paste textcolor colorpicker textpattern"
	],
	toolbar1: "insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link | print preview  | forecolor backcolor",
	toolbar2: "",
	image_advtab: true,
	templates: [
	{title: 'Test template 1', content: 'Test 1'},
	{title: 'Test template 2', content: 'Test 2'}
	]
});
</script>
<!-- FIM SCRIPT GERAR EDITOR DE TEXTO-->
<!-- SCRIP ADICIONAR FOTOS -->
<script>
    var qtdFotos=0;
    function handleFileSelect(evt) {
      var files = evt.target.files; // FileList object

      // Loop through the FileList and render image files as thumbnails.
      for (var i = 0, f; f = files[i]; i++) {

        // Only process image files.
        if (!f.type.match('image.*')) {
          continue;
        }

    	
    	f.id=qtdFotos;
        qtdFotos++;

        var reader = new FileReader();

        // Closure to capture the file information.
        reader.onload = (function(theFile) {
          return function(e) {
            var image;
            

            image = '<div class="content-foto">';
               image += '<img id="foto'+theFile.id+'" class="thumb" src="'+e.target.result+'" ></img>';
               image += '<p>Descrição:</p>';
               image += '<input name="descricao_foto'+theFile.id+'" type="text"></input>';
               image += '<p>Fotógrafo:</p>';          
               image += '<input name="fotografo_foto'+theFile.id+'" type="text"></input>';
            image += '</div>';
            $('#galeria').append(image);            
          };
        })(f);

        // Read in the image file as a data URL.
        reader.readAsDataURL(f);
      }
      //esconde input e remove o id dele, pois nao vai mais ser usado
      $('#fotos').hide();
      $('#fotos').removeAttr('id');
      //cria outro input de multiplos arquivos com as msm informações do antigo
      var novoInput = '<input type="file" id="fotos" name="fotos[]" multiple/>';
      $('#selector').append(novoInput);
      //cria o evendo novamente para o input
      document.getElementById('fotos').addEventListener('change', handleFileSelect, false);
    }
    //$('#files').change(handleFileSelect);
    //$('#files').addEventListener('change', handleFileSelect, false);
    document.getElementById('fotos').addEventListener('change', handleFileSelect, false);
  </script>
