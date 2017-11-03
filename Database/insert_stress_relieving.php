<?php

    $response = array();
    $stress_id = $_POST['stress_id'];
    $temperature = $_POST['temperature'];
    $time = $_POST['time'];
    $shielding_gas = $_POST['shielding_gas'];
    $comment = $_POST['comment'];

    require_once __DIR__ . '/db_connect.php';
    $db = new DB_CONNECT();
    $insert_to_stress_relieving = "INSERT INTO printbook.stress_relieving(stress_id, temperature, time, shielding_gas, comment) " +
                                  + "VALUES('$stress_id', '$temperature', '$time', '$shielding_gas', '$comment')";

    if(!$db) die('Something went wrong while connecting to database');
    $result = mysqli_query($db->connect(), $insert_to_stress_relieving) or die('unable to insert to printing');
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
