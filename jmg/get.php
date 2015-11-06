<?php
  if(isset($_GET['photo']) && $_GET['photo'] != ""){
    $photo = $_GET["photo"];
    $char_to_escape= array("\\","/");
    $photo = str_replace($char_to_escape, "", $photo);
    $photo_p =  "img/".htmlspecialchars($photo,ENT_QUOTES,'utf-8').".jpg";
  }
?>
<!DOCTYPE html>
<html>
<head>
  <title>Prova</title>
</head>
<body>
<?php
    if(isset($photo_p) && file_exists($photo_p)){
       echo "<img src='".$photo_p."' name='photo'/>";
  }else{
    echo "File Not Found On Server\n".$photo_;
  }
  ?>
</body>
</html>