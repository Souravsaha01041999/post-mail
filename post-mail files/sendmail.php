<?php
require_once('concention.php');
if($_SERVER["REQUEST_METHOD"]=="POST")
{
    $to=$_POST['toval'];
    $frm=$_POST['frmval'];
    $msg=$_POST['msgval'];
    $date=$_POST['dateval'];
    $sub=$_POST['subval'];
    $tm=$_POST['timeval'];
    
    $sqltest="SELECT * FROM `post-mail-reg` WHERE user_id='$to'";
    $check=mysqli_query($con,$sqltest);
    $cid=mysqli_fetch_assoc($check);
    if(strlen($cid['user_id'])>0)
    {
        $sql="INSERT INTO `post-mail-msg` (`msg_id`, `from_msg`, `to_msg`, `subject`, `msg`, `date`,`seen`,`time`,`image`,`del_val`) VALUES (NULL, '$frm', '$to', '$sub', '$msg', '$date','0','$tm','none','none')";
    $rt=mysqli_query($con,$sql);
    if($rt)
    {
        echo "Message has been send";
    }
    else
    {
        echo "Failed to send massege";
    }
    }
    else
    {
        echo "User is Incorrect";
    }
    
    
}
?>