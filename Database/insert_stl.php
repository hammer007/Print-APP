<?php

    $response = array();
    $stl_id = $_POST['STL_ID'];
    $url = $_POST['URL'];
    $project_id = $_POST['Project_ID'];

    require_once __DIR__ . '/db_connect.php';
    $db = new DB_CONNECT();
    $insert_to_stl = "INSERT INTO printbook.stl(STL_ID, URL, Project_ID) VALUES('$stl_id', '$url', '$project_id')";

    if(!$db) die('Something went wrong while connecting to database');
    $result = mysqli_query($db->connect(), $insert_to_stl) or die('unable to insert to printing');
    if ($result) {
        $response["success"] = 1;
        $response["message"] = "printing successfully inserted.";
        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred while inserting to printing";
        echo json_encode($response);
    }
?>
