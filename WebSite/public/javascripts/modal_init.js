// Permet d'effacer les données des formulaires et de réinitialiser le message d'erreur des fenêtres modales

$(document).ready(function() {
    $('.modal').on('hidden.bs.modal', function(){
        $(this).find('#form_connexion')[0].reset();
        $(this).find('#form_inscription')[0].reset();
        $("#error_box_inscription").css('visibility', 'hidden');
        $("#error_box_connexion").css('visibility', 'hidden');
        $("#error_box_reinit").css('visibility', 'hidden');
    });
});