<?php
require_once('concention.php');
if($_SERVER["REQUEST_METHOD"]=="POST")
{
    $userid=$_POST['uid'];
    $sql="SELECT * FROM `post-mail-reg` WHERE user_id='$userid'";
$rt=mysqli_query($con,$sql);
$rw=mysqli_fetch_assoc($rt);
if(strlen($rw['user_id'])>0)
{
    $to=$rw['email'];
    $subject="YOUR Password";
$headers="From:xlrppt@gmail.com"."\r\n";
$p="Your password: ".$rw['user_pass'];
    
    mail($to,$subject,$p,$headers);
    echo "Your Password Has Been Send In Your Email:$to Check The Password";
}
else
{
    echo "user not found";
}
}
?>