<?php
require_once('concention.php');
if($_SERVER["REQUEST_METHOD"]=="POST")
{
    $data="";
    
    $ms=$_POST['msgid'];
    $cata=$_POST['cata'];
    $seen=$_POST['seen'];
    
    if($seen=="0")
    {
        $seensql="UPDATE `post-mail-msg` SET seen = '1' WHERE msg_id='$ms'";
        mysqli_query($con,$seensql);
    }
    
    if($cata=="inbox")
    {
        $data="from_msg";
    }
    else if($cata=="outbox")
    {
        $data="to_msg";
    }
    
        $sql="SELECT * FROM `post-mail-msg` WHERE msg_id='$ms'";
    
    
    $rt=mysqli_query($con,$sql);
    
    $rw=mysqli_fetch_assoc($rt);
    
    echo $rw[$data]."`".$rw['subject']."`".$rw['msg']."`".$rw['image']."`";
}
?>