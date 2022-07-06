<?php
if($_SERVER["REQUEST_METHOD"]=="POST")
{
    $otp=$_POST['rcvdata'];
    $to=$_POST['email'];
    
$subject="YOUR OTP";
$headers="From:xlrppt@gmail.com"."\r\n";
    
    mail($to,$subject,$otp,$headers);
}
?>