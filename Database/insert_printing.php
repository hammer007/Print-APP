<?php
    $response = array();
    $slm_id = $_POST['slm_id'];
    $start_time = $_POST['start_time'];
    $end_time = $_POST['end_time'];
    $date = '2017-11-10';
    $operator = $_POST['operator'];
    $machine_type = $_POST['machine_type'];
    $powder_weight_start = $_POST['powder_weight_start'];
    $powder_weight_end = $_POST['powder_weight_end'];
    $powder_waste_weight = $_POST['powder_waste_weight'];
    $powder_used = $_POST['powder_used'];
    $material_id = '1';
    $build_platform_weight = $_POST['build_platform_weight'];
    $print_time = $_POST['print_time'];
    $powder_condition = "new";
    $reused_times = '5';
    $number_of_layers = $_POST['number_of_layers'];
    $dpc_factor = $_POST['dpc_factor'];
    $exposure_time = $_POST['exposure_time'];
    $comments = $_POST['comments'];
    require_once __DIR__ . '/db_connect.php';
    $db = new DB_CONNECT();
    $insert_to_rinting = "INSERT INTO printbook.printing(slm_id,start_time,end_time,date,operator,machine_type,powder_weight_start,powder_weight_end,powder_waste_weight,powder_used,material_id,build_platform_weight,print_time,powder_condition,reused_times,number_of_layers,dpc_factor,exposure_time,comments)VALUES('$slm_id','$start_time','$end_time','$date','$operator','$machine_type','$powder_weight_start','$powder_weight_end','$powder_waste_weight','$powder_used','$material_id','$build_platform_weight', '$print_time', '$powder_condition', '$reused_times','$number_of_layers','$dpc_factor', '$exposure_time', '$comments')";
    echo $insert_to_rinting;
    if(!$db) die('Something went wrong while connecting to database');
    $result = mysqli_query($db->connect(), $insert_to_rinting) or die('unable to insert to printing');
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