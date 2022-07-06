<?php
require_once('concention.php');
if($_SERVER["REQUEST_METHOD"]=="POST")
{
    $pass=$_POST['value'];
    $uid=$_POST['uid'];
    $sql="UPDATE `post-mail-reg` SET user_pass='$pass' WHERE user_id='$uid'";
    
    $rt=mysqli_query($con,$sql);
    if($rt)
    {
        echo "success";
    }
    else
    {
        echo "Faild";
    }
    
}
?>