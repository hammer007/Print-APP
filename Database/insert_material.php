<?php

    $response = array();
    $Material_ID = $_POST['Material_ID'];
    $URL_Image = $_POST['URL_Image'];
    $Project_ID = $_POST['Project_ID'];

    require_once __DIR__ . '/db_connect.php';
    $db = new DB_CONNECT();
    $insert_to_material = "INSERT INTO printbook.material(Material_ID, URL_Image, Project_ID) VALUES('$Material_ID', '$URL_Image', '$Project_ID)";
    if(!$db) die('Something went wrong while connecting to database');
    $result = mysqli_query($db->connect(), $insert_to_material) or die('unable to insert to material');
    if ($result) {
        $response["success"] = 1;
        $response["message"] = "material successfully inserted.";
        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred while inserting to material";
        echo json_encode($response);
    }
?>
