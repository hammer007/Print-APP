<?php

    $response = array();
    $hallflow_id = $_POST['hallflow_id'];
    $material_id = $_POST['material_id'];
    $project_id = $_POST['project_id'];
    $operator = $_POST['operator'];
    $date = $_POST['date'];
    $humidity = $_POST['humidity'];
    $temperature = $_POST['temperature'];
    $value_1 = $_POST['value_1'];
    $tap_1 = $_POST['tap_1'];
    $value_2 = $_POST['value_2'];
    $tap_2 = $_POST['tap_2'];
    $value_3 = $_POST['value_3'];
    $tap_3 = $_POST['tap_3'];
    $average = $_POST['average'];
	
    require_once __DIR__ . '/db_connect.php';
    $db = new DB_CONNECT();
    $insert_to_hall_flow = "INSERT INTO printbook.hallflow(hallflow_id, material_id, project_id, operator, date, humidity, temperature, value_1," +
                          + "tap_1, value_2, tap_2, value_3, tap_3, average) " +
                          + "VALUES('$hallflow_id', '$material_id', '$project_id', '$operator', '$date', '$humidity', '$temperature', '$value_1'," +
                          + "'$tap_1', '$value_2', '$tap_2', '$value_3', '$tap_3', '$average')";

    if(!$db) die('Something went wrong while connecting to database');
    $result = mysqli_query($db->connect(), $insert_to_hall_flow) or die('unable to insert to hallflow');
    if ($result) {
        $response["success"] = 1;
        $response["message"] = "hallflow successfully inserted.";
        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred while inserting to hallflow";
        echo json_encode($response);
    }
?>
