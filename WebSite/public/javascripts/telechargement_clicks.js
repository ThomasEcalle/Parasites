$(document).ready(function() {
    $("#get_application").click(function(){
       window.location.href = "/download/setup.exe";
    });
    $("#get_plugin").click(function(){
       window.location.href = "/download/plugin.jar";
    });
    $("#biblio_plugin").click(function(){
       window.location.href = "/download/biblio_plugin.jar";
    })
});