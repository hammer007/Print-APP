<?php

    $response = array();
    $hardening_id = $_POST['hardening_id'];
    $temperature = $_POST['temperature'];
    $time = $_POST['time'];
    $comment = $_POST['comment'];

    require_once __DIR__ . '/db_connect.php';
    $db = new DB_CONNECT();
    $insert_to_hardening = "INSERT INTO printbook.hardening(hardening_id, temperature, time, comment) VALUES('$hardening_id', '$temperature', '$time', '$comment')";
    if(!$db) die('Something went wrong while connecting to database');
    $result = mysqli_query($db->connect(), $insert_to_hardening) or die('unable to insert to hardening');
    if ($result) {
        $response["success"] = 1;
        $response["message"] = "hardening successfully inserted.";
        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred while inserting to hardening";
        echo json_encode($response);
    }
?>
