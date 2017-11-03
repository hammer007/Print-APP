<?php

    $response = array();
    $cad_id = $_POST['cad_id'];
    $url = $_POST['url'];
    $project_id = $_POST['project_id'];
	
    require_once __DIR__ . '/db_connect.php';
    $db = new DB_CONNECT();
    $insert_to_cad = "INSERT INTO printbook.cad(cad_id, url, project_id)" +
                          + "VALUES('$cad_id', '$url', '$project_id)";

    if(!$db) die('Something went wrong while connecting to database');
    $result = mysqli_query($db->connect(), $insert_to_cad) or die('unable to insert to cad');
    if ($result) {
        $response["success"] = 1;
        $response["message"] = "cad successfully inserted.";
        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred while inserting to cad";
        echo json_encode($response);
    }
?>
