<?php

    $response = array();
    $project_id = $_POST['project_id'];
    $project_name = $_POST['project_name'];
    require_once __DIR__ . '/db_connect.php';

    $db = new DB_CONNECT();
    $insert_to_project = "INSERT INTO printbook.project(project_id, project_name) VALUES('$project_id', '$project_name')";
    if(!$db) die('Something went wrong while connecting to database');
    $result = mysqli_query($db->connect(), $insert_to_project) or die('unable to insert to project');;

    if ($result) {
        $response["success"] = 1;
        $response["message"] = "Project successfully created.";
        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
        echo json_encode($response);
    }
?>
