package com.sender.postmail;

public class CaptureDatas
{
    private String msgid,userid,subject,date,time;
    String seen;
    CaptureDatas(String mid,String uid,String sub,String dt,String sn,String time)
    {
        this.msgid=mid;
        this.userid=uid;
        this.subject=sub;
        this.date=dt;
        this.time=time;
        this.seen=sn;
    }
    String getMsgid()
    {
        return this.msgid;
    }
    String getUserid()
    {
        return this.userid;
    }
    String getSubject()
    {
        return this.subject;
    }
    String getDate()
    {
        return this.date;
    }
    String getSeen()
    {
        return this.seen;
    }
    String getTime(){
        return this.time;
    }
}
