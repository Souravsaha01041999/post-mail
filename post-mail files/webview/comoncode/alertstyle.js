function setAlert(text)
            {
                var tm;
                document.getElementById("alertStyle").style.visibility="visible";
                document.getElementById("text").innerHTML=text;
                tm=setInterval(function()
                {
                    clearInterval(tm);
                    document.getElementById("alertStyle").style.visibility="hidden";
                    document.getElementById("text").innerHTML="";
                },3000);
            }