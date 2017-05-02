// Permet d'effacer les données des formulaires et de réinitialiser le message d'erreur des fenêtres modales

$(document).ready(function() {
    $('.modal').on('hidden.bs.modal', function(){
        $(this).find('form')[0].reset();
        $("#error_box_inscription").css('visibility', 'hidden');
        $("#error_box_connexion").css('visibility', 'hidden');
    });
});