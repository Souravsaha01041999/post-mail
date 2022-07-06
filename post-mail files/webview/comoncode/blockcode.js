window.addEventListener('contextmenu', function (e) {
    e.preventDefault();
    
}); 

window.addEventListener("keydown",function(e)
{
    //alert(e.keyCode);
    if((e.ctrlKey && e.shiftKey && e.keyCode == 73)||(e.keyCode == 123)||(e.ctrlKey&& e.keyCode == 85)||(e.ctrlKey && e.shiftKey && e.keyCode == 74))
    {
        e.preventDefault();
    }
    else if((e.keyCode == 83 && (navigator.platform.match("Mac") ? e.metaKey : e.ctrlKey)))
    {
        e.preventDefault();
    }
});