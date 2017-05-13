package com.parasites.controllers;

import com.parasites.network.OnlineServerManager;
import com.parasites.network.bo.Game;
import com.parasites.network.bo.User;
import com.parasites.utils.ParasitesUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * Created by spyro on 11/05/2017.
 */
public class PrincipalWindowController extends ParasitesFXController implements Initializable
{

    /*******************************************
     *                                         *
     *                  Partie                 *
     *                                         *
     *******************************************/
    @FXML
    private Tab partie_tab;
    @FXML
    private Button creer_partie;
    @FXML
    private TableView<Game> parties_disponibles;
    @FXML
    private TableColumn<Game, String> gameCreatorNameColumn;
    @FXML
    private TableColumn<Game, Integer> nbPlayersColumn;
    @FXML
    private TableColumn<Game, Integer> nbMaxPlayersColumn;


    /*******************************************
     *                                         *
     *                  Salon                  *
     *                                         *
     *******************************************/
    @FXML
    private Tab salon_tab;
    @FXML
    private TableView<User> membres_partie;
    @FXML
    private TableColumn<User, String> gameMembersColumn;
    @FXML
    private Label proprietaire;
    @FXML
    private Label joueurs_max;
    @FXML
    private Label taille_map;
    @FXML
    private Label joueurs_actuel;
    @FXML
    private Label nombre_cases;
    @FXML
    private Label creation;
    @FXML
    private Label expiration;
    @FXML
    private Button lancer;
    @FXML
    private Button quitter;

    /*******************************************
     *                                         *
     *                  Profil                 *
     *                                         *
     *******************************************/
    @FXML
    private Label pseudo_actuel;
    @FXML
    private TextField pseudo;
    @FXML
    private TextField confirmation_pseudo;
    @FXML
    private Label password_actuel;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmation_password;
    @FXML
    private TextField mail;
    @FXML
    private Label mail_actuel;
    @FXML
    private TextField nom;
    @FXML
    private Label nom_actuel;
    @FXML
    private TextField prenom;
    @FXML
    private Label prenom_actuel;
    @FXML
    private TextField telephone;
    @FXML
    private Label telephone_actuel;

    private PopOver pop;
    private List<Game> actualGamesList;
    private List<User> actualUserList;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        actualGamesList = new ArrayList<>();
        actualUserList = new ArrayList<>();

        OnlineServerManager.getInstance().addObserver(this);

        salon_tab.setDisable(true);

        settingColumns();

        updateListOfGames(OnlineServerManager.getInstance().getGameList());
        updateListOfUsers(OnlineServerManager.getInstance().getUserList());


        // Setting rows onClickListener
        parties_disponibles.setRowFactory(tv ->
        {
            TableRow<Game> row = new TableRow<>();
            row.setOnMouseClicked(event ->
            {
                if (event.getClickCount() == 2 && (!row.isEmpty()))
                {
                    final Game game = parties_disponibles.getItems().get(row.getIndex());
                    if (game.getActualPlayersCount() < game.getNbPlayerMax())
                    {
                        OnlineServerManager.getInstance().joinGame(game);
                        showSalon();
                    }
                }
            });
            return row;
        });

    }

    private void settingColumns()
    {
        // All Games Columns
        gameCreatorNameColumn.setCellValueFactory(new PropertyValueFactory<>("creatorPseudo"));
        nbMaxPlayersColumn.setCellValueFactory(new PropertyValueFactory<>("nbPlayerMax"));
        nbPlayersColumn.setCellValueFactory(new PropertyValueFactory<>("actualPlayersCount"));

        // Salon Column
        gameMembersColumn.setCellValueFactory(new PropertyValueFactory<>("pseudo"));
    }

    @FXML
    public void clickOnCreation()
    {
        pop = new PopOver();
        Parent root;
        try
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("ecran_popover.fxml"));
            root = fxmlLoader.load();
            PopoverWindowController controller = fxmlLoader.<PopoverWindowController>getController();
            controller.setParentController(this);
            pop.setContentNode(root);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        pop.setArrowLocation(PopOver.ArrowLocation.RIGHT_TOP);
        pop.show(creer_partie,
                creer_partie.localToScreen(creer_partie.getBoundsInLocal()).getMinX(),
                creer_partie.localToScreen(creer_partie.getBoundsInLocal()).getMinY() + 25);

        pop.setDetachable(false);
    }

    @FXML
    public void clickOnQuitterSalon()
    {
        partie_tab.setDisable(false);
        partie_tab.getTabPane().getSelectionModel().select(partie_tab);
        salon_tab.setDisable(true);

        OnlineServerManager.getInstance().leaveGame();
    }

    public void popoverValidation(int numb_players, int width, int height)
    {
        pop.hide();
        // ICI ON VA POUVOIR CREER LA PARTIE AVEC DES VALEURS DONNEES

        showSalon();

        final User currentUser = OnlineServerManager.getInstance().getCurrentUser();
        final Random rand = new Random();
        final int randomId = rand.nextInt(15000);
        final Game game = new Game(currentUser, randomId, numb_players, width);

        OnlineServerManager.getInstance().createGame(game);
    }

    private void updateListOfGames(final List<Game> list)
    {
        if (actualGamesList.isEmpty() == false)
        {
            actualGamesList.clear();
        }

        actualGamesList.addAll(list);

        parties_disponibles.setItems(FXCollections.observableArrayList(actualGamesList));
        parties_disponibles.refresh();

        for (Game game : list)
        {
            if (game.getPlayersList().contains(OnlineServerManager.getInstance().getCurrentUser()))
            {
                updateSalonList(game.getPlayersList());
                updateSalonInformations(game);
            }
        }
    }

    private void updateListOfUsers(List<User> list)
    {
        if (actualUserList.isEmpty() == false)
        {
            actualUserList.clear();
        }

        actualUserList.addAll(list);
    }


    private void showSalon()
    {
        salon_tab.setDisable(false);
        salon_tab.getTabPane().getSelectionModel().select(salon_tab);
        partie_tab.setDisable(true);
    }

    private void updateSalonList(final List<User> users)
    {
        membres_partie.setItems(FXCollections.observableArrayList(users));
        membres_partie.refresh();
    }

    private void updateSalonInformations(final Game game)
    {
        setTextInLabel(proprietaire, game.getCreator().getPseudo());
        setTextInLabel(joueurs_actuel, String.valueOf(game.getActualPlayersCount()));
        setTextInLabel(joueurs_max, String.valueOf(game.getNbPlayerMax()));
        setTextInLabel(nombre_cases, String.valueOf(game.getSize()));
    }


    /*******************************************
     *                                         *
     *        Server LifeCycle                 *
     *                                         *
     *******************************************/

    @Override
    public void onPersonalGameCreation(final Game createdGame)
    {
        updateSalonInformations(createdGame);
    }

    @Override
    public void onServerConnectionStart()
    {
        // NO utility here.
    }

    @Override
    public void onServerConnectionEnd(boolean success)
    {
        // NO utility here.
    }

    @Override
    public void onMessageFromServer(String message)
    {
        ParasitesUtils.logError(message);
    }

    @Override
    public void onServerStateChange(List<User> users, List<Game> games)
    {
        updateListOfUsers(users);
        updateListOfGames(games);
        ParasitesUtils.logList(games);
    }

}
