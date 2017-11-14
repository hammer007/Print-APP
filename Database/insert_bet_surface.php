<?php

    $response = array();
    $bet_id = $_POST['bet_id'];
    $material_id = $_POST['material_id'];
    $URL_Photo = $_POST['URL_Photo'];

    require_once __DIR__ . '/db_connect.php';
    $db = new DB_CONNECT();
    $insert_to_bet_surface = "INSERT INTO printbook.bet_surface(bet_id, material_id, URL_Photo) VALUES('$bet_id', '$material_id', '$URL_Photo)";

    if(!$db) die('Something went wrong while connecting to database');
    $result = mysqli_query($db->connect(), $insert_to_bet_surface) or die('unable to insert to bet_surface');
    if ($result) {
        $response["success"] = 1;
        $response["message"] = "bet_surface successfully inserted.";
        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred while inserting to bet_surface";
        echo json_encode($response);
    }
?>
