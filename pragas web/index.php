<?php
?>
<div style="position:absolute; width:500px;  float:left; padding-top:2px; padding-left:5px"> <img src="resource/img/imagem-fina.jpeg" height="920" width="500"> </div>
<?php

include 'views/template/cabecalho.php';
?>
<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-60109081-1', 'auto');
  ga('send', 'pageview');

</script>
<?php

include 'views/plugin/info_site2html.php';

include 'views/plugin/galeria_fotos2html.php';

include 'views/plugin/listar_pragas2html.php';

include 'views/template/rodape.php';
?>
        

