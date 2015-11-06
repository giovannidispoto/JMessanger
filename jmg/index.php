<?php
  switch($_GET["action"]){
        case "up": include("up.php");
                      break;

        case "get": include("get.php");
                    break;
  }
?>