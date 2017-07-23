// Clic sur le bouton valider de la fenêtre modale inscription

$(document).ready(function() {
    $("#valider_inscription").click(function(){
        $.post('/inscription', 
        {
            pseudo: $("#pseudo").val(),
            password: $("#password").val(),
            verif_password: $("#verif_password").val(),
            lastname: $("#lastname").val(),
            firstname: $("#firstname").val(),
            phone: $("#phone").val(),
            email: $("#email").val()
        }).done(function(data){
            if(data.statusCode != 200){
                $("#error_box_inscription").attr('class', 'alert alert-danger');
                $("#error_box_inscription").css('visibility', 'visible');
                $("#error_box_inscription").text(data.body.errors[0].split("Validation error: ")[1]
                                                 + (data.body.errors.length > 1 ? " + " + (data.body.errors.length - 1) + " error(s)." : ""));
            }
            else{
                $("#error_box_inscription").attr('class', 'alert alert-success');
                $("#error_box_inscription").css('visibility', 'visible');
                $("#error_box_inscription").text("The user had been succefully added to the database. Wait for the redirect.");
                setTimeout(location.reload.bind(location), 3000);  
            }
        });
    });
});