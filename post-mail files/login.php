<?php
require_once('concention.php');
clearstatcache();
if($_SERVER["REQUEST_METHOD"]=="POST")
{
    $userid=$_POST['uid'];
    $pass=$_POST['pass'];
    $sql="SELECT * FROM `post-mail-reg` WHERE user_id='$userid'";
    $rt=mysqli_query($con,$sql);
$rw=mysqli_fetch_assoc($rt);
if(strlen($rw['user_id'])>0)
{
    if($pass==$rw['user_pass'])
    {
        echo "Success";
    }
    else{
        echo "Error Password";
    }
}
else
{
    echo "User Not Found";
}

}
?>