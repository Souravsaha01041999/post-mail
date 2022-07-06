<?php
require_once('concention.php');
if($_SERVER["REQUEST_METHOD"]=="POST")
{
    $userid=$_POST['uid'];
    $pass=$_POST['password'];
    $email=$_POST['mail'];
    
    
    $sql="SELECT * FROM `post-mail-reg` WHERE user_id='$userid'";
$rt=mysqli_query($con,$sql);
$rw=mysqli_fetch_assoc($rt);
if(strlen($rw['user_id'])>0)
{
    echo "1";  // 1 for already exist
}
else
{
    $sql="INSERT INTO `post-mail-reg` (`user_id`, `user_pass`, `email`) VALUES ('$userid', '$pass', '$email');";
    $rt=mysqli_query($con,$sql);
    echo "2";   //2 for succes
}
}
?>