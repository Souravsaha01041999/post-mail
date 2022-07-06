var tim;
var ang=0;
function setProgress(title,text)
{
    document.getElementById("progress").style.visibility="visible";
                document.getElementById("title").innerHTML=title;
                document.getElementById("mytext").innerHTML=text;
                tim=setInterval(function()
                {
                    ang+=5;
                    if(ang>=360)    ang=0;
                    document.getElementById("imgicon").style.transform = "rotate("+ang+"deg)";
                },10);
            }

            function stopProgress()
            {
                document.getElementById("progress").style.visibility="hidden";
                clearInterval(tim);
            }