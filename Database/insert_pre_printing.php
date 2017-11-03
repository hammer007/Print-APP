<?php

    $response = array();
    $pre_printing_id = $_POST['pre_printing_id'];
    $project_id = $_POST['project_id'];
    $build_id = $_POST['build_id'];
    $no_parts = $_POST['no_parts'];
	$printing_parameter = $_POST['printing_parameter'];
    $comment = $_POST['comment'];
	
    require_once __DIR__ . '/db_connect.php';
    $db = new DB_CONNECT();
    $insert_to_pre_printing = "INSERT INTO printbook.pre_printing(pre_printing_id, project_id, build_id, no_parts, printing_parameter, comment)" +
                          + "VALUES('$pre_printing_id', '$project_id', '$build_id', '$no_parts', '$printing_parameter', '$comment')";

    if(!$db) die('Something went wrong while connecting to database');
    $result = mysqli_query($db->connect(), $insert_to_pre_printing) or die('unable to insert to pre_printing');
    if ($result) {
        $response["success"] = 1;
        $response["message"] = "Pre_printing successfully inserted.";
        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred while inserting to pre_printing";
        echo json_encode($response);
    }
?>
