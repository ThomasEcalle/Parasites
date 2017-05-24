package com.parasites.controllers;

import com.parasites.annotations.ColumnFieldTarget;
import com.parasites.network.OnlineServerManager;
import com.parasites.network.bo.ChatMessage;
import com.parasites.network.bo.Game;
import com.parasites.network.bo.User;
import com.parasites.utils.ParasitesUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;

import javax.xml.soap.Text;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Created by spyro on 11/05/2017.
 */
public final class PrincipalWindowController extends ParasitesFXController implements Initializable {

    /*******************************************
     *                                         *
     *                  Partie                 *
     *                                         *
     *******************************************/
    @FXML
    private Tab game_tab;
    @FXML
    private Button create_game;
    @FXML
    private TableView<Game> games;
    @FXML
    @ColumnFieldTarget("creatorPseudo")
    private TableColumn<Game, String> gameCreatorNameColumn;
    @FXML
    @ColumnFieldTarget("actualPlayersCount")
    private TableColumn<Game, Integer> nbPlayersColumn;
    @FXML
    @ColumnFieldTarget("nbPlayerMax")
    private TableColumn<Game, Integer> nbMaxPlayersColumn;
    @FXML
    @ColumnFieldTarget("state")
    private TableColumn<Game, String> stateColumn;
    @FXML
    private Label connected_as;


    /*******************************************
     *                                         *
     *                  Salon                  *
     *                                         *
     *******************************************/
    @FXML
    private Tab waiting_tab;
    @FXML
    private TableView<User> game_players;
    @FXML
    @ColumnFieldTarget("pseudo")
    private TableColumn<User, String> gameMembersColumn;
    @FXML
    private Label owner;
    @FXML
    private Label max_players;
    @FXML
    private Label map_size;
    @FXML
    private Label actual_players;
    @FXML
    private Label creation;
    @FXML
    private Label expiry;
    @FXML
    private Button launch;
    @FXML
    private Button quit;

    /*******************************************
     *                                         *
     *                  Profil                 *
     *                                         *
     *******************************************/

    @FXML
    private PasswordField password;
    @FXML
    private PasswordField password_confirming;
    @FXML
    private Label actual_mail;
    @FXML
    private TextField mail;
    @FXML
    private TextField mail_confirming;
    @FXML
    private TextField lastname;
    @FXML
    private Label actual_lastname;
    @FXML
    private TextField firstname;
    @FXML
    private Label actual_firstname;
    @FXML
    private TextField phone;
    @FXML
    private Label actual_phone;
    @FXML
    private Label password_error;
    @FXML
    private Label mail_error;
    @FXML
    private Label firstname_error;
    @FXML
    private Label lastname_error;
    @FXML
    private Label phone_error;

    private Stage stage;

    public void setToken(String token) {
        this.token = token;
    }

    private String token;
    private PopOver pop;
    private List<Game> actualGamesList;
    private List<User> actualUserList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.parseAnnotations(this);
        actualGamesList = new ArrayList<>();
        actualUserList = new ArrayList<>();

        OnlineServerManager.getInstance().addObserver(this);

        waiting_tab.setDisable(true);

        updateListOfGames(OnlineServerManager.getInstance().getGameList());
        updateListOfUsers(OnlineServerManager.getInstance().getUserList());

        // Setting rows onClickListener
        games.setRowFactory(tv ->
        {
            TableRow<Game> row = new TableRow<>();
            row.setOnMouseClicked(event ->
            {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    final Game game = games.getItems().get(row.getIndex());
                    if (game.getActualPlayersCount() < game.getNbPlayerMax() && !game.isLaunched()) {
                        OnlineServerManager.getInstance().joinGame(game);
                        showSalon();
                    }
                }
            });
            return row;
        });
        initTextFields();
        initErrorLabel(null, null, null, null, null);
        connected_as.setText(connected_as.getText() + OnlineServerManager.getInstance().getCurrentUser().getPseudo());
    }

    @FXML
    public void clickOnCreation() {
        pop = new PopOver();
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("ecran_popover.fxml"));
            root = fxmlLoader.load();
            PopoverWindowController controller = fxmlLoader.<PopoverWindowController>getController();
            controller.setParentController(this);
            pop.setContentNode(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        pop.setArrowLocation(PopOver.ArrowLocation.RIGHT_TOP);
        pop.show(create_game,
                create_game.localToScreen(create_game.getBoundsInLocal()).getMinX(),
                create_game.localToScreen(create_game.getBoundsInLocal()).getMinY() + 25);

        pop.setDetachable(false);
    }

    @FXML
    public void clickOnValider(){
        final ProfileController controller = new ProfileController(token,
                password.getText(),
                password_confirming.getText(),
                mail.getText(),
                mail_confirming.getText(),
                firstname.getText(),
                lastname.getText(),
                phone.getText());
        resetTextFields();
        initErrorLabel(controller.getPassword_message(),
                controller.getMail_message(),
                controller.getFirstname_message(),
                controller.getLastname_message(),
                controller.getPhone_message());
        initTextFields();
    }

    @FXML
    public void onLeavingProfile(){
        resetTextFields();
        initErrorLabel(null, null, null, null, null);

    }

    @FXML
    public void clickOnReset(){
        resetTextFields();
        initErrorLabel(null, null, null, null, null);
    }

    @FXML
    public void clickOnLaunch() {
        OnlineServerManager.getInstance().launchGame();
    }

    @FXML
    public void clickOnQuitterSalon() {
        game_tab.setDisable(false);
        game_tab.getTabPane().getSelectionModel().select(game_tab);
        waiting_tab.setDisable(true);

        OnlineServerManager.getInstance().leaveGame();
    }

    private void resetTextFields(){
        password.setText("");
        password_confirming.setText("");
        mail.setText("");
        mail_confirming.setText("");
        firstname.setText("");
        lastname.setText("");
        phone.setText("");
    }

    private void initTextFields(){
        OnlineServerManager server = OnlineServerManager.getInstance();
        User user = server.getCurrentUser();
        actual_mail.setText(user.getEmail());
        actual_firstname.setText(user.getFirstname());
        actual_lastname.setText(user.getLastname());
        actual_phone.setText(user.getPhone_number());
    }

    private void initErrorLabel(Object[] password,
                                Object[] mail,
                                Object[] firstname,
                                Object[] lastname,
                                Object[] phone){
        displayErrorLabel(password, password_error);
        displayErrorLabel(mail, mail_error);
        displayErrorLabel(firstname, firstname_error);
        displayErrorLabel(lastname, lastname_error);
        displayErrorLabel(phone, phone_error);
    }

    private void displayErrorLabel(Object[] arrayMessage, Label label){
        if(arrayMessage == null) label.setVisible(false);
        else{
            label.setVisible(true);
            label.setTextFill((int) arrayMessage[1] == 0 ? Color.RED : Color.GREEN);
            label.setText(arrayMessage[0].toString());
        }
    }

    private void showGame() {
        Platform.runLater(() ->
        {
            final Stage stage = new Stage();
            Parent root = null;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ecran_game.fxml"));
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            final GameWindowController controller = loader.getController();
            stage.setTitle("Parasites");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("game_ico.png")));
            stage.show();
            ((Stage) launch.getScene().getWindow()).close();
        });
    }

    public void popoverValidation(int numb_players, int width, int height) {
        pop.hide();

        showSalon();

        final User currentUser = OnlineServerManager.getInstance().getCurrentUser();
        final Random rand = new Random();
        final int randomId = rand.nextInt(15000);
        final Game game = new Game(currentUser, randomId, numb_players, width);

        OnlineServerManager.getInstance().createGame(game);
    }

    private void updateListOfGames(final List<Game> list) {
        if (actualGamesList.isEmpty() == false) {
            actualGamesList.clear();
        }

        actualGamesList.addAll(list);

        refreshTableView(actualGamesList, games);

        for (Game game : list) {
            if (game.getPlayersList().contains(OnlineServerManager.getInstance().getCurrentUser())) {
                updateSalonList(game.getPlayersList());
                updateSalonInformations(game);
            }
        }
    }

    private void updateListOfUsers(List<User> list) {
        if (actualUserList.isEmpty() == false) {
            actualUserList.clear();
        }

        actualUserList.addAll(list);
    }

    private void showSalon() {
        waiting_tab.setDisable(false);
        waiting_tab.getTabPane().getSelectionModel().select(waiting_tab);
        game_tab.setDisable(true);
    }

    private void updateSalonList(final List<User> users) {
        refreshTableView(users, game_players);
    }

    private void updateSalonInformations(final Game game) {
        setTextInLabel(owner, game.getCreator().getPseudo());
        setTextInLabel(actual_players, String.valueOf(game.getActualPlayersCount()));
        setTextInLabel(max_players, String.valueOf(game.getNbPlayerMax()));
        setTextInLabel(map_size, String.valueOf(game.getSize()));

        if (!game.getCreator().equals(OnlineServerManager.getInstance().getCurrentUser())) {
            launch.setDisable(true);
        }
    }


    /*******************************************
     *                                         *
     *        Server LifeCycle                 *
     *                                         *
     *******************************************/

    @Override
    public void onServerConnectionStart() {
        // NO utility here.
    }

    @Override
    public void onServerConnectionEnd(boolean success) {
        // NO utility here.
    }

    @Override
    public void onMessageFromServer(String message) {

        showNotification(message);
        ParasitesUtils.logError(message);
    }

    @Override
    public void onServerStateChange(List<User> users, List<Game> games) {
        updateListOfUsers(users);
        updateListOfGames(games);
    }

    @Override
    public void onLaunchingGame() {
        showGame();
    }

    @Override
    public void onReceivingGameMessage(final ChatMessage chatMessage) {
        // No utility here.
    }


}
