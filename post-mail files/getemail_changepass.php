<?php
require_once('concention.php');
if($_SERVER["REQUEST_METHOD"]=="POST")
{
    $uid=$_POST['uid'];
    $sql="SELECT * FROM `post-mail-reg` WHERE user_id='$uid'";
    
    
    $rt=mysqli_query($con,$sql);
    $rw=mysqli_fetch_assoc($rt);
    
    echo $rw['email'];
}
?>