<?php
require_once('concention.php');
if($_SERVER["REQUEST_METHOD"]=="POST")
{
    $msgid=$_POST['msgid'];
    $user_id=$_POST['user_id'];
    
    $sqltest="SELECT * FROM `post-mail-msg` WHERE msg_id='$msgid'";
    $check=mysqli_query($con,$sqltest);
    $cid=mysqli_fetch_assoc($check);
    
    if($cid['del_val']=="none")
    {
        //SET ONLY USER ID
        $sql="UPDATE `post-mail-msg` SET del_val = '$user_id' WHERE msg_id='$msgid'";
        mysqli_query($con,$sql);
    }
    else{
        
        //DELETE MSG WITH PICTURE IF AVAIL
        $dt=explode("/",$cid['image']);
        if($dt[count($dt)-1]!="none")
        {
            unlink($dt[count($dt)-1]);
        }

        $sql="DELETE FROM `post-mail-msg` WHERE msg_id='$msgid'";
        mysqli_query($con,$sql);

    }


}
?>