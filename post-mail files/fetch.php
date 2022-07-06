<?php
require_once('concention.php');
if($_SERVER["REQUEST_METHOD"]=="POST")
{
    $sendval="";
    $sql="";
    $action="";
    $temp;
    $cata=$_POST['val'];
    $idval=$_POST['idv'];
    
    if($cata=="outbox")
    {
        $sql="SELECT * FROM `post-mail-msg` WHERE from_msg='$idval' AND del_val<>'$idval' ORDER BY msg_id DESC";
        $action="to_msg";
    }
    else if($cata=="inbox")
    {
        $sql="SELECT * FROM `post-mail-msg` WHERE to_msg='$idval' AND del_val<>'$idval' ORDER BY msg_id DESC";
        $action="from_msg";
    }
    
    
    $rt=mysqli_query($con,$sql);
    
    while($rw=mysqli_fetch_assoc($rt))
    {
        $temp=$rw['seen'];
        if($cata=="outbox")
        {
            $temp="1";
        }
        $sendval=$sendval.$rw['msg_id']."`".$rw[$action]."`".$rw['subject']."`".$rw['date']."`".$temp."`".$rw['time']."`";
    }
    echo $sendval;
}
?>