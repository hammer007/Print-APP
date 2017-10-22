<?php

/*
 * Following code will create a new product row
 * All product details are read from HTTP Post Request
 */

// array for JSON response
$response = array();
// check for required fields
if (isset($_POST['id']) && isset($_POST['name'])) {

    $id = $_POST['id'];
    $name = $_POST['name'];
    // include db connect class
    require_once __DIR__ . '/db_connect.php';

    // connecting to db
    $db = new DB_CONNECT();
    $insert_to_project = "INSERT INTO printerbook.project(project_name) VALUES('$name')";
    // mysql inserting a new row
    if(!$db) die('Something went wrong while connecting to database');
    $result = mysqli_query($db->connect(), $insert_to_project) or die('unable to insert to project');;

    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Project successfully created.";

        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";

        // echoing JSON response
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field is missing";

    // echoing JSON response
    echo json_encode($response);
}
?>
