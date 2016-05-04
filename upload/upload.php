<?php
if($_SERVER['REQUEST_METHOD']=='POST'){

    $empcode = $_POST['empCode'];
    $name =$_FILES["fileToUpload"]["name"];

    $target_dir = "web/uploads/";
    $target_file = $target_dir . basename($_FILES["fileToUpload"]["name"]);
    $target=$target_dir .$empcode.".pdf";
    $uploadOk = 1;
    $imageFileType = pathinfo($target_file,PATHINFO_EXTENSION);

    // Check if file already exists
    if (file_exists($target)) {
        echo "Sorry, file already exists.";
        $uploadOk = 0;
    }
    // Check file size
    if ($_FILES["fileToUpload"]["size"] > 50000000000000) {
        echo "Sorry, your file is too large.";
        $uploadOk = 0;
    }
    // Allow certain file formats
    if($imageFileType != "jpg" && $imageFileType != "png" && $imageFileType != "jpeg"
    && $imageFileType != "pdf" && $imageFileType != "docx" ) {
        echo "Sorry, only JPG, JPEG, PNG & pdf and docx files are allowed.";
        $uploadOk = 0;
    }
    // Check if $uploadOk is set to 0 by an error
    if ($uploadOk == 0) {
        echo "Sorry, your file was not uploaded.";
        // if everything is ok, try to upload file
    } else {
        if (move_uploaded_file($_FILES["fileToUpload"]["tmp_name"], $target)) {

            $con = mysqli_connect("localhost", "root", "", "fractal");

            $path = $target;
            $actualpath = "http://localhost:8080/$path";
            $sql = "INSERT INTO upload (name,empcode,location) VALUES ('$name','$empcode','$actualpath')";

            $statement = mysqli_query($con, $sql) or die(mysqli_error($con));


            echo "The file ". basename( $_FILES["fileToUpload"]["name"]). " has been uploaded and query is".$sql;
        } else {
            echo "Sorry, there was an error uploading your file.";
        }
    }

}else{
    echo "Error";
}
?>
