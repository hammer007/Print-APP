<?php

    $response = array();
    $post_id = $_POST['post_id'];
    $project_id = $_POST['project_id'];
    $url_photo = $_POST['url_photo'];
    $support_removal = $_POST['support_removal'];
    $wedm = $_POST['wedm'];
    $wedm_comment = $_POST['wedm_comment'];
    $blasting = $_POST['blasting'];
    $blasting_time = $_POST['blasting_time'];
    $blasting_type = $_POST['blasting_type'];
    $blasting_comment = $_POST['blasting_comment'];
    $stress_id = $_POST['stress_id'];
    $hardening_id = $_POST['hardening_id'];
    $tempering_id = $_POST['tempering_id'];
    $solution_id = $_POST['solution_id'];
    $aging_id = $_POST['aging_id'];

    require_once __DIR__ . '/db_connect.php';
    $db = new DB_CONNECT();
    $insert_to_post_printing = "INSERT INTO printbook.post_printing(post_id, project_id, url_photo, support_removal, wedm, wedm_comment, blasting, blasting_time," +
                          + "blasting_type, blasting_comment, stress_id, hardening_id, tempering_id, solution_id, aging_id)" +
                          + "VALUES('$post_id', '$project_id', '$url_photo', '$support_removal', '$wedm', '$wedm_comment', '$blasting', '$blasting_time'," +
                          + "'$blasting_type', '$blasting_comment', '$stress_id', '$hardening_id', '$tempering_id', '$solution_id', '$aging_id')";

    if(!$db) die('Something went wrong while connecting to database');
    $result = mysqli_query($db->connect(), $insert_to_post_printing) or die('unable to insert to printing');
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
