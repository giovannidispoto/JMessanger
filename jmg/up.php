<?php

    $objFile = & $_FILES["file_upload"];
    $o

    if(!move_uploaded_file($_FILES['file_upload']['tmp_name'], 'uploads/' . $_FILES['file_upload']['name'])){
    die('Error uploading file - check destination is writeable.');
}
    } else {
        print "There was an error uploading the file, please try again!";
    }