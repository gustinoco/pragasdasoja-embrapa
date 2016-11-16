<?php
  /**
  * PAGINA RESPOSÁVEL POR MOSTRAR CONTEÚDO DE UMA PRAGA
  */
  include_once __DIR__.'/template/cabecalho.php';
	include_once __DIR__.'/../conf/connect.php';
  include_once __DIR__.'/../dao/PragaDAO.class.php';
  include_once __DIR__.'/../dao/AutorDAO.class.php';
  include_once __DIR__.'/../resource/libs/wideimage/WideImage.php';
  define('DIR_T','../resource/img/thumbnail/');
  define('DIR_F','../resource/img/fotos/');
  define('DIR_A','../resource/img/thumbnail/autor/');

  $id_praga= (int)$_GET['id'];
  if($id_praga==0){
    die('<center><h1>Ops :(</h1><h2>Nenhuma praga foi encontrada!</h2></center>');
  }

  $daoPraga = new PragaDAO();
  $daoAutor = new AutorDAO();
  $praga = $daoPraga->buscar_praga($id_praga);
  $fotos = $daoPraga->buscar_fotos($id_praga);
  $autor = $daoAutor->buscarAutorPorPraga($id_praga);  
  $src_a = DIR_A.$autor->id.'.jpeg';
  if($praga){
    //GERA FOTOS
    $imagens="";
    //GERA GALERIA DE FOTOS DA PRAGA
    if($fotos){
      foreach ($fotos as $foto) {
          //recupera caminho da foto
          $nome_img = $praga->id.'-'.$foto->id.'.jpg';
          $src_t = DIR_T.$nome_img;
          $src_f = DIR_F.$nome_img;
          $imagens .= "<li style='line-height: 0.8; padding-bottom:5px;width: 9.5em;'><a href='$src_f'>";
          $imagens .= "<img class='foto_do_menu' src= '$src_t'/></a>";    
          $imagens .= "<br><span style='font-size:11px;'>
            $foto->descricao  <br>
            Foto: $foto->fotografo</span> </li>";
      }//FIM WHILE
    }//FIM IF
    ?>
    <!-- SCRIPT DA GALERIA DE FOTOS -->
    <script src="../resource/js/foundation/foundation.clearing.js"></script>
    <script src="../resource/js/vendor/modernizr.js"></script>

    <div class="row">
      <div class="large-12 columns">
        <!-- GALERIA -->
        <ul style=" margin:0 auto;" data-clearing> 
          <?php echo $imagens;?>
        </ul>
      </div>
    </div>
    <div class="row">
      <div class="large-12 columns">
        <!-- DADOS DA PRAGA -->
        <section>
          <center><h4 class="titulo"><strong><?php echo $praga->nome; ?></strong></h4></center>
        </section>
        <section>
          <a id="a_descricao"><h4><strong><span class="corTema">O que &eacute;?</span></strong></h4></a>
          <p id="p_descricao"><?php echo $praga->descricao; ?></p>
        </section>
        <HR WIDTH=100% ALIGN=RIGHT NOSHADE>
        
        <section>
          <a id="a_biologia"><h4><strong><span class="corTema">Biologia</span></strong></h4></a>
          
          <label id="p_biologia" style="display: none;">
            <p><?php echo $praga->biologia; ?></p>
          </label>
          
        </section>
        <HR WIDTH=100% ALIGN=RIGHT NOSHADE>
        
        <section>
          <a id="a_comportamento"><h4><strong><span class="corTema">Comportamento</span></strong></h4></a>
          <label id="p_comportamento" style="display: none;">
            <p><?php echo $praga->comportamento; ?></p>
          </label>
        </section>
        <HR WIDTH=100% ALIGN=RIGHT NOSHADE>

        <section>
          <a id="a_danos"><h4><strong><span class="corTema">Danos</span></strong></h4></a>
          <label id="p_danos" style="display: none;">
            <p><?php echo $praga->danos; ?></p>
          </label>
        </section>
        <HR WIDTH=100% ALIGN=RIGHT NOSHADE>

        <section>
          <a id="a_localizacao"><h4><strong><span class="corTema">Localiza&ccedil;&atilde;o</span></strong></h4></a>
          <label id="p_localizacao" style="display: none;">
            <p><?php echo $praga->localizacao; ?></p>
          </label>
        </section>
        <HR WIDTH=100% ALIGN=RIGHT NOSHADE>

        <section>
          <a id="a_controle"><h4><strong><span class="corTema">Controle</span></strong></h4></a>
          <label id="p_controle" style="display: none;">
            <p><?php echo $praga->controle; ?></p>
          </label>
        </section>
        <HR WIDTH=100% ALIGN=RIGHT NOSHADE>

          <section>
          <a id="a_bibliografia"><h4><strong><span class="corTema">Literatura recomendada</span></strong></h4></a>
          <label id="p_bibliografia" style="display: none;">
            <p><?php echo $praga->bibliografia; ?></p>
          </label>
        </section>
        <HR WIDTH=100% ALIGN=RIGHT NOSHADE>
      </div>
      <!--DADOS DO AUTOR-->
      <div class="large-9 columns small-6"></div>
      <div class="large-3 columns panel small-6">
        <p style="color:#666;text-align:center;margin:0;">
          <b>Pesquisador</b>
        </p>
        <div class="large-5 columns small-5">
          <center><img src='../resource/img/Crebio.jpeg'/></center>
        </div>
        <div class="large-7 columns small-7">
          <p style="color:#666;margin:0;">
            Crébio José Ávila
          </p>
        </div>
      </div>
      <!--FIM DADOS DO AUTOR-->
    </div>

    <script type="text/javascript">
      $('#a_biologia').click(function(){
        $('#p_biologia').toggle();
      });
      $('#a_bibliografia').click(function(){
        $('#p_bibliografia').toggle();
      });
      $('#a_controle').click(function(){
        $('#p_controle').toggle();
      });
      $('#a_localizacao').click(function(){
        $('#p_localizacao').toggle();
      });
      $('#a_danos').click(function(){
        $('#p_danos').toggle();
      });
      $('#a_comportamento').click(function(){
        $('#p_comportamento').toggle();
      });
    </script>
  <?php 
  }//FECHA IF
  else{
    echo '<center><h1>Ops :(</h1><h2>Nenhuma praga foi encontrada!</h2></center>';
  }
  include 'template/rodape.php';
  ?>
        

