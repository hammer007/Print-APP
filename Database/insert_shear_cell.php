<?php

    $response = array();
    $shear_id = $_POST['shear_id'];
    $project_id = $_POST['project_id'];
    $material_id = $_POST['material_id'];
    $date = $_POST['date'];
    $operator = $_POST['operator'];
    $temperature = $_POST['temperature'];
    $humidity = $_POST['humidity'];
    $powder_condition = $_POST['powder_condition'];
    $reused_times = $_POST['reused_times'];
    $rumbling = $_POST['rumbling'];
    $rumbling_time = $_POST['rumbling_time'];
    $consolidation_pressure = $_POST['consolidation_pressure'];
    $quantity_of_powder = $_POST['quantity_of_powder'];
    $cohesion = $_POST['cohesion'];
    $uys = $_POST['uys'];
    $mps = $_POST['mps'];
    $ff = $_POST['ff'];
    $aif = $_POST['aif'];
    $bd = $_POST['bd'];

    require_once __DIR__ . '/db_connect.php';
    $db = new DB_CONNECT();
    $insert_to_shear_cell = "INSERT INTO printbook.shear_cell(shear_id, project_id, material_id, date, operator, temperature, humidity, powder_condition," +
                          + "reused_times, rumbling, rumbling_time, consolidation_pressure, quantity_of_powder, cohesion, uys, mps," +
                          + "ff, aif, bd) " +
                          + "VALUES('$shear_id', '$project_id', '$material_id', '$date', '$operator', '$temperature', '$humidity', '$powder_condition'," +
                          + "'$reused_times', '$rumbling', '$rumbling_time', '$consolidation_pressure', '$quantity_of_powder', '$cohesion', '$uys'," +
                          + "'$mps','$ff', '$aif', '$bd')";

    if(!$db) die('Something went wrong while connecting to database');
    $result = mysqli_query($db->connect(), $insert_to_shear_cell) or die('unable to insert to printing');
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
