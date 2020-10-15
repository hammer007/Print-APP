<?php

    $response = array();
    $solution_id = $_POST['solution_id'];
    $temperature = $_POST['temperature'];
    $time = $_POST['time'];
    $comment = $_POST['comment'];

    require_once __DIR__ . '/db_connect.php';
    $db = new DB_CONNECT();
    $insert_to_solution_treatment = "INSERT INTO printbook.solution_treatment(solution_id, temperature, time, comment) VALUES('$solution_id', '$temperature', '$time', '$comment')";

    if(!$db) die('Something went wrong while connecting to database');
    $result = mysqli_query($db->connect(), $insert_to_solution_treatment) or die('unable to insert to printing');
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
