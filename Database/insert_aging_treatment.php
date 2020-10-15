<?php

    $response = array();
    $aging_id = $_POST['aging_id'];
    $temperature = $_POST['temperature'];
    $time = $_POST['time'];
    $cycles = $_POST['cycles'];
    $comment = $_POST['comment'];

    require_once __DIR__ . '/db_connect.php';
    $db = new DB_CONNECT();
    $insert_to_aging_treatment = "INSERT INTO printbook.aging_treatment(aging_id, temperature, time, cycles, comment) VALUES('$aging_id', '$temperature', '$time', '$cycles', '$comment')";

    if(!$db) die('Something went wrong while connecting to database');
    $result = mysqli_query($db->connect(), $insert_to_aging_treatment) or die('unable to insert to printing');
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
