<?php

    $response = array();
    $flow_id = $_POST['flow_id'];
    $material_id = $_POST['material_id'];
    $date = $_POST['date'];
    $temperature = $_POST['temperature'];
    $humidity = $_POST['humidity'];
    $operator = $_POST['operator'];
    $powder_condition = $_POST['powder_condition'];
    $reused_time = $_POST['reused_time'];
    $rumbling = $_POST['rumbling'];
    $rumbling_times = $_POST['rumbling_times'];
    $bfe_mj = $_POST['bfe_mj'];
    $si = $_POST['si'];
    $fri = $_POST['fri'];
    $se_mj_g = $_POST['se_mj_g'];
    $cbd_g_ml = $_POST['cbd_g_ml'];
    $cetap50_mj = $_POST['cetap50_mj'];
    $bdtap50_g_ml = $_POST['bdtap50_g_ml'];

    require_once __DIR__ . '/db_connect.php';
    $db = new DB_CONNECT();
    $insert_to_flow_energy = "INSERT INTO printbook.flow_energy(flow_id, material_id, date, temperature, humidity, operator, powder_condition, reused_time," +
                          + "rumbling, rumbling_times, bfe_mj, si, fri, se_mj_g, cbd_g_ml, cetap50_mj,bdtap50_g_ml)" + 
                          + "VALUES('$flow_id', '$material_id', '$date', '$temperature', '$humidity', '$operator', '$powder_condition', '$reused_time'," +
                          + "'$rumbling', '$rumbling_times', '$bfe_mj', '$si', '$fri', '$se_mj_g', '$cbd_g_ml'," +
                          + "'$cetap50_mj','$bdtap50_g_ml')";

    if(!$db) die('Something went wrong while connecting to database');
    $result = mysqli_query($db->connect(), $insert_to_flow_energy) or die('unable to insert to flow_energy');
    if ($result) {
        $response["success"] = 1;
        $response["message"] = "flow_energy successfully inserted.";
        echo json_encode($response);
    } else {
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred while inserting to flow_energy";
        echo json_encode($response);
    }
?>
