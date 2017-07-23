$(document).ready(function() {
    $("#send").click(function(){
        $.post('/connexion', 
        {
            pseudo: $("#pseudo_connexion").val(),
            password: $("#password_connexion").val()
        }).done(function(data){
            if(data.statusCode != 200){
                $("#error_box_connexion").attr('class', 'alert alert-danger');
                $("#error_box_connexion").css('visibility', 'visible');
                $("#error_box_connexion").text("This combination of pseudo and password does not exists.");
            }
            else{
                $("#error_box_connexion").attr('class', 'alert alert-success');
                $("#error_box_connexion").css('visibility', 'visible');
                $("#error_box_connexion").text("Success, wait for the redirect !");
                setTimeout(location.reload.bind(location), 2000);                              
            }
        });
    });
});