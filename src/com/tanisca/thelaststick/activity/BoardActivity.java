package com.tanisca.thelaststick.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.InterstitialAd;
import com.google.analytics.tracking.android.EasyTracker;
import com.tanisca.thelaststick.Game;
import com.tanisca.thelaststick.GameForOnePlayer;
import com.tanisca.thelaststick.GameForTwoPlayers;
import com.tanisca.thelaststick.R;
import com.tanisca.thelaststick.model.Achievement;
import com.tanisca.thelaststick.model.AchievementLocalized;
import com.tanisca.thelaststick.model.AchievementManager;
import com.tanisca.thelaststick.model.GameMode;
import com.tanisca.thelaststick.model.Player;
import com.tanisca.thelaststick.tool.VerticalTextView;

public class BoardActivity extends Activity {

    private static final String    TAG           = "Board2Activity";

    /**
     * Détermine le nombre de joueur (determiné en MenuPlayerActivity)
     */
    public static boolean          TwoPlayerMode = false;

    /**
     * Détermine le type de partie (classic, original)
     */
    public static GameMode         GameTypeMode  = GameMode.CLASSIC;

    private Game                   game;
    private VerticalTextView       stickCounter;
    private LinearLayout           stickBoard;
    private LinearLayout           deckPlayer1;
    private LinearLayout           deckPlayer2;
    private LinearLayout           deckPlayer1Num;
    private LinearLayout           deckPlayer2Num;
    private LinearLayout           deckPlayer1Draw;
    private LinearLayout           deckPlayer2Draw;
    private HashMap<Integer, View> lastMoveSticks;
    private Button                 lastMove1;
    private Button                 lastMove2;
    private LinearLayout           scoreBoard;
    private TextView               menuButton;
    private TextView               instructionText;
    private TextView               adPlaceHolder;
    private InterstitialAd         interstitialAd;
    private int                    gameCount;
    private boolean                showAd;
    private boolean                useNumDecks;

    /**
     * Sauvegarde les references de chaque elements graphiques interactifs,
     * attaches les listeners sur les boutons interactifs et démarre une partie
     */
    @SuppressLint("UseSparseArrays")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        this.useNumDecks = BoardActivity.GameTypeMode != GameMode.CLASSIC;

        this.stickBoard = (LinearLayout) findViewById(R.id.drawStickBoard);
        this.stickCounter = (VerticalTextView) findViewById(R.id.stickCounter);

        this.deckPlayer1Draw = (LinearLayout) findViewById(R.id.deckPlayer1Draw);
        this.deckPlayer2Draw = (LinearLayout) findViewById(R.id.deckPlayer2Draw);
        this.deckPlayer1Num = (LinearLayout) findViewById(R.id.deckPlayer1Num);
        this.deckPlayer2Num = (LinearLayout) findViewById(R.id.deckPlayer2Num);

        if (useNumDecks) {
            this.deckPlayer1 = this.deckPlayer1Num;
            this.deckPlayer2 = this.deckPlayer2Num;
        }
        else {
            this.deckPlayer1 = this.deckPlayer1Draw;
            this.deckPlayer2 = this.deckPlayer2Draw;
        }

        this.scoreBoard = (LinearLayout) findViewById(R.id.scoreBoard);
        this.menuButton = (TextView) findViewById(R.id.menuButton);

        this.instructionText = (TextView) findViewById(R.id.instructionText);
        this.adPlaceHolder = (TextView) findViewById(R.id.adPlaceHolder);

        this.lastMoveSticks = new HashMap<Integer, View>();
        if (useNumDecks) {
            this.lastMove1 = (Button) findViewById(R.id.lastMove1Num);
            this.lastMove2 = (Button) findViewById(R.id.lastMove2Num);
        }
        else {
            this.lastMoveSticks.put(R.id.lastMove1a,
                    findViewById(R.id.lastMove1a));
            this.lastMoveSticks.put(R.id.lastMove1b,
                    findViewById(R.id.lastMove1b));
            this.lastMoveSticks.put(R.id.lastMove1c,
                    findViewById(R.id.lastMove1c));
            this.lastMoveSticks.put(R.id.lastMove2a,
                    findViewById(R.id.lastMove2a));
            this.lastMoveSticks.put(R.id.lastMove2b,
                    findViewById(R.id.lastMove2b));
            this.lastMoveSticks.put(R.id.lastMove2c,
                    findViewById(R.id.lastMove2c));
        }
        this.manageScoreButtons();

        gameCount = 0;
        showAd = false;
        startGame();
    }

    /**
     * Démarre une partie en mode 1 joueur ou 2 joueurs selon la valeur de
     * TwoPlayerMode
     */
    private void startGame() {

        interstitialAd = new InterstitialAd(this, getResources().getString(
                R.string.ads_board_interstitial));
        interstitialAd.setAdListener(new MyIntersticialAdListener());
        requestIntersticial();

        ++gameCount;

        if (TwoPlayerMode) {
            this.game = new GameForTwoPlayers(this, GameTypeMode, 20);
        }
        else {
            this.game = new GameForOnePlayer(this, GameTypeMode, 20);
        }

        // Gestions des boutons de retraits des decks
        this.manageDeckButtons();

        refreshUI(true);

        // Gestion des toasts selon le type de Game
        this.manageLastMoveToasts();
    }

    /**
     * Actualise l'affichage du dernier coup de chaque joueur
     */
    private void refreshLastMoves() {
        int lastMove1 = this.game.getLastMove(Player.ONE);
        int lastMove2 = this.game.getLastMove(this.game.getPlayer2());
        if (useNumDecks) {
            if (lastMove1 > 0) {
                this.lastMove1.setText(Integer.toString(lastMove1));
                this.lastMove1.setVisibility(View.VISIBLE);
            }
            if (lastMove2 > 0) {
                this.lastMove2.setText(Integer.toString(lastMove2));
                this.lastMove2.setVisibility(View.VISIBLE);
            }
        }
        else {
            this.lastMoveSticks.get(R.id.lastMove1a).setVisibility(
                    lastMove1 > 0 ? View.VISIBLE : View.INVISIBLE);
            this.lastMoveSticks.get(R.id.lastMove1b).setVisibility(
                    lastMove1 > 1 ? View.VISIBLE : View.INVISIBLE);
            this.lastMoveSticks.get(R.id.lastMove1c).setVisibility(
                    lastMove1 > 2 ? View.VISIBLE : View.INVISIBLE);
            this.lastMoveSticks.get(R.id.lastMove2c).setVisibility(
                    lastMove2 > 0 ? View.VISIBLE : View.INVISIBLE);
            this.lastMoveSticks.get(R.id.lastMove2b).setVisibility(
                    lastMove2 > 1 ? View.VISIBLE : View.INVISIBLE);
            this.lastMoveSticks.get(R.id.lastMove2a).setVisibility(
                    lastMove2 > 2 ? View.VISIBLE : View.INVISIBLE);
        }
    }

    /**
     * Affiche les bonnes couleurs (bleu/rouge/gris) selon le tour du joueur en
     * cours et le nombre de batons restants sur le plateau pour les 3 boutons
     * de chaque joueur
     */
    @SuppressWarnings("deprecation")
    private void refreshDecks() {
        int player1ButtonStyle = R.drawable.remove_player1_button;
        int player1StickStyle = R.drawable.stick;
        int player2ButtonStyle = R.drawable.remove_player2_button;;
        int player2StickStyle = R.drawable.stick;
        int playerDisabledButton = R.drawable.remove_disabled_button;
        int playerDisabledStick = R.drawable.stick_disabled;
        int playerDisabledColor = R.color.disabledButtonText;
        int playerEnabledColor = R.color.white;
        int player1ButtonTextColor = playerEnabledColor;
        int player2ButtonTextColor = playerEnabledColor;

        if (!Player.ONE.canPlay(this.game.getPlayerTurn())) {
            player1ButtonStyle = playerDisabledButton;
            player1StickStyle = playerDisabledStick;
            player1ButtonTextColor = playerDisabledColor;
        }

        if (!this.game.getPlayer2().canPlay(this.game.getPlayerTurn())) {
            player2ButtonStyle = playerDisabledButton;
            player2StickStyle = playerDisabledStick;
            player2ButtonTextColor = playerDisabledColor;
        }

        HashMap<Player, LinearLayout> decks = new HashMap<Player, LinearLayout>();
        decks.put(Player.ONE, this.deckPlayer1);
        decks.put(this.game.getPlayer2(), this.deckPlayer2);

        int sticks = this.game.getRemainingSticks();
        int buttonCount = this.deckPlayer1.getChildCount();
        for (Player player : decks.keySet()) {
            LinearLayout deckPlayer = decks.get(player);
            for (int i = 0; i < buttonCount; ++i) {
                View button = deckPlayer.getChildAt(i);
                int stickCount = 0;
                if (!this.useNumDecks) {
                    stickCount = ((RelativeLayout) button).getChildCount();
                }
                else {
                    stickCount = Integer.parseInt((String) ((Button) button)
                            .getText());
                }

                int buttonStyle = player2ButtonStyle;
                int stickStyle = player2StickStyle;
                int buttonColor = player2ButtonTextColor;

                if (player == Player.ONE) {
                    buttonStyle = player1ButtonStyle;
                    stickStyle = player1StickStyle;
                    buttonColor = player1ButtonTextColor;
                }

                if (stickCount > sticks) {
                    buttonStyle = playerDisabledButton;
                    stickStyle = playerDisabledStick;
                    buttonColor = playerDisabledColor;
                }

                button.setBackgroundDrawable(getResources().getDrawable(
                        buttonStyle));
                if (this.useNumDecks) {
                    ((Button) button).setTextColor(getResources().getColor(
                            buttonColor));
                }
                else {

                    for (int j = 0; j < stickCount; ++j) {
                        View stick = ((RelativeLayout) button).getChildAt(j);
                        stick.setBackgroundDrawable(getResources().getDrawable(
                                stickStyle));
                    }
                }
            }
        }
    }

    /**
     * Actualise le compteur de batons
     */
    private void refreshStickCounter() {
        int sticks = this.game.getRemainingSticks();
        String plural = sticks > 1 ? "s" : "";
        this.stickCounter.setText(getString(R.string.remaining_sticks_short,
                sticks, plural));
    }

    /**
     * Actualise le plateau de jeu
     * 
     * @param restartGame
     *            raffiche les batons effacés si true
     */
    private void refreshStickBoard(boolean restartGame) {
        int sticks = this.game.getRemainingSticks();
        int children = this.stickBoard.getChildCount();

        if (restartGame) {
            for (int i = 0; i < sticks; ++i) {
                Button button = (Button) this.stickBoard.getChildAt(i);
                button.setVisibility(View.VISIBLE);
            }
        }

        for (int i = sticks; i < children; ++i) {
            Button button = (Button) this.stickBoard.getChildAt(i);
            button.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Affiche une consigne lors d'un mode 1 joueur pour aider le joueur
     */
    private void refreshInstructionText() {
        int visibility = this.game.isTwoPlayerGame() ? View.GONE : View.VISIBLE;
        if (visibility == View.VISIBLE) {
            int text = this.game.getPlayerTurn() == Player.ONE ? R.string.instruction_player_turn
                    : R.string.instruction_ai_turn;
            this.instructionText.setText(getResources().getString(text));
        }
        this.instructionText.setVisibility(visibility);
    }

    /**
     * Attache les listeners sur les 3 boutons de chaque decks
     */
    private void manageDeckButtons() {
        int[] player1buttonNum = { R.id.pick1Player1ButtonNum,
                R.id.pick2Player1ButtonNum, R.id.pick3Player1ButtonNum };
        int[] player2buttonNum = { R.id.pick1Player2ButtonNum,
                R.id.pick2Player2ButtonNum, R.id.pick3Player2ButtonNum };
        int[] player1buttonDraw = { R.id.pick1Player1ButtonDraw,
                R.id.pick2Player1ButtonDraw, R.id.pick3Player1ButtonDraw };
        int[] player2buttonDraw = { R.id.pick1Player2ButtonDraw,
                R.id.pick2Player2ButtonDraw, R.id.pick3Player2ButtonDraw };
        HashMap<Player, ArrayList<Integer>> playerPawns = this.game.getPawns();

        int[] player1buttons;
        int[] player2buttons;
        if (this.useNumDecks) {
            player1buttons = player1buttonNum;
            player2buttons = player2buttonNum;

            for (int button : player1buttonDraw) {
                findViewById(button).setVisibility(View.INVISIBLE);
            }
            for (int button : player2buttonDraw) {
                findViewById(button).setVisibility(View.INVISIBLE);
            }
        }
        else {
            player1buttons = player1buttonDraw;
            player2buttons = player2buttonDraw;
            for (int button : player1buttonNum) {
                findViewById(button).setVisibility(View.INVISIBLE);
            }
            for (int button : player2buttonNum) {
                findViewById(button).setVisibility(View.INVISIBLE);
            }
        }

        for (Player player : playerPawns.keySet()) {
            ArrayList<Integer> pawns = playerPawns.get(player);
            int[] buttons = (player == Player.ONE) ? player1buttons
                    : player2buttons;
            for (int i = 0; i < buttons.length; ++i) {
                View bt = findViewById(buttons[i]);
                final Integer removeSticks = pawns.get(i);
                final Player finalPlayer = player;
                if (this.useNumDecks) {
                    ((Button) findViewById(buttons[i])).setText(removeSticks
                            .toString());
                }
                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean hasMoved = game.removeSticks(finalPlayer,
                                removeSticks);
                        if (hasMoved) {
                            refreshUI(false);
                        }
                    }
                });
            }
        }
    }

    /**
     * Actualise l'UI
     */
    public void refreshUI() {
        refreshUI(false);
    }

    /**
     * Actualise l'UI
     * 
     * @param restartGame
     *            permet de rafficher en début de partie les batons effacés
     */
    private void refreshUI(boolean restartGame) {
        if (this.game.isDone()) {
            refreshLastMoves();
            hideBoard();
            showScore();
            showAchievements();
        }
        else {
            hideScore();
            refreshStickBoard(restartGame);
            refreshStickCounter();
            refreshDecks();
            refreshLastMoves();
            showBoard();
            refreshInstructionText();

            this.game.playAI();
        }
    }

    private void showAchievements() {
        HashSet<Achievement> achievements = this.game.getUnlockedAchievements();
        if (achievements != null) {
            AchievementManager manager = AchievementManager.getInstance(this);
            for (Achievement achievement : achievements) {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.achievement_toast,
                        (ViewGroup) findViewById(R.id.toast_layout_root));

                AchievementLocalized localizedAchievement = manager
                        .getLocalizedAchievement(achievement);

                TextView title = (TextView) layout
                        .findViewById(R.id.toast_title);
                title.setText(localizedAchievement.getTitle());

                TextView description = (TextView) layout
                        .findViewById(R.id.toast_description);
                description.setText(localizedAchievement.getDescription());

                Toast toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            }
        }
    }

    /**
     * Cache le plateau de jeu (en fin de partie)
     */
    private void hideBoard() {
        this.stickBoard.setVisibility(View.GONE);
        this.deckPlayer1Draw.setVisibility(View.GONE);
        this.deckPlayer1Num.setVisibility(View.GONE);
        this.deckPlayer2Draw.setVisibility(View.GONE);
        this.deckPlayer2Num.setVisibility(View.GONE);
        this.stickCounter.setVisibility(View.GONE);
        this.instructionText.setVisibility(View.GONE);
        if (this.lastMove1 != null) {
            this.lastMove1.setVisibility(View.GONE);
            this.lastMove2.setVisibility(View.GONE);
        }
    }

    /**
     * Affiche le plateau de jeu (en début de partie)
     */
    private void showBoard() {
        this.stickBoard.setVisibility(View.VISIBLE);
        this.deckPlayer1.setVisibility(View.VISIBLE);
        this.deckPlayer2.setVisibility(View.VISIBLE);
        this.deckPlayer2Draw.setVisibility(View.VISIBLE);
        this.stickCounter.setVisibility(View.VISIBLE);
        // Pas de gestion de instructionText ici
    }

    /**
     * Affiche l'écran de fin de jeu
     */
    @SuppressWarnings("deprecation")
    private void showScore() {
        Player winner = this.game.getWinner();
        int background = R.drawable.win_player1;;
        int text = R.string.win_phrase_you;
        if (this.game.isTwoPlayerGame()) {
            text = R.string.win_phrase;
            rotateScoreIfPossible(winner == Player.TWO);
        }
        if (winner != Player.ONE) {
            background = R.drawable.win_player2;
            text = R.string.win_phrase;
        }
        this.scoreBoard.setBackgroundDrawable(getResources().getDrawable(
                background));
        ((TextView) this.scoreBoard.getChildAt(0)).setText(getResources()
                .getString(text, getResources().getString(winner.getName())));

        this.adPlaceHolder.setVisibility(View.GONE);

        this.scoreBoard.setVisibility(View.VISIBLE);
        this.menuButton.setVisibility(View.VISIBLE);
    }

    /**
     * Affiche l'écran de fin de jeu à l'envers lors d'une partie à 2 joueurs si
     * le 2eme joueur gagne la partie. Ne fonctionne que sur API 11+
     * 
     * @param rotate
     *            si true alors affiche à l'envers, à l'endroit sinon
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void rotateScoreIfPossible(boolean rotate) {
        if (canUseRotation()) {
            float angle = rotate ? 180f : 0f;
            this.scoreBoard.setRotation(angle);
            this.menuButton.setRotation(angle);
        }
    }

    /**
     * Cache l'écran de fin de jeu
     */
    private void hideScore() {
        int visibility = this.game.isOnePlayerGame() ? View.INVISIBLE
                : View.GONE;
        this.adPlaceHolder.setVisibility(visibility);
        this.scoreBoard.setVisibility(View.GONE);
        this.menuButton.setVisibility(View.GONE);
    }

    /**
     * Attache les listeners sur les boutons de l'écran de fin de jeu
     */
    private void manageScoreButtons() {
        Button bt = (Button) findViewById(R.id.restartButton);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interstitialAd != null && interstitialAd.isReady()) {
                    interstitialAd.show();
                    showAd = false;
                }
                startGame();
            }
        });

        TextView tv = (TextView) findViewById(R.id.menuButton);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void manageLastMoveToasts() {

        final String toast1;
        final String toast2;
        if (this.game.isOnePlayerGame()) {
            toast1 = getResources().getString(R.string.last_move_toast_you);
            toast2 = getResources().getString(R.string.last_move_toast,
                    getResources().getString(Player.AI.getName()));
        }
        else {
            toast1 = getResources().getString(R.string.last_move_toast,
                    getResources().getString(Player.ONE.getName()));
            toast2 = getResources().getString(R.string.last_move_toast,
                    getResources().getString(Player.TWO.getName()));
        }

        final Game game = this.game;

        int lastMoveId1;
        int lastMoveId2;
        if (useNumDecks) {
            lastMoveId1 = R.id.lastMove1Num;
            lastMoveId2 = R.id.lastMove2Num;
        }
        else {
            lastMoveId1 = R.id.lastMove1Draw;
            lastMoveId2 = R.id.lastMove2Draw;
        }

        View lm1 = findViewById(lastMoveId1);
        lm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (game.getLastMove(Player.ONE) != 0) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            toast1, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.LEFT, 0, 0);
                    toast.show();
                }
            }
        });

        lm1 = findViewById(lastMoveId2);
        lm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (game.getLastMove(game.getPlayer2()) != 0) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            toast2, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP | Gravity.RIGHT, 0, 0);
                    toast.show();
                }
            }
        });
    }

    /**
     * Démarre Google Analytics
     */
    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    /**
     * Termine Google Analytics
     */
    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }

    /**
     * View.setRotation est present sur API 11+ (Honeycomb 3.0)
     * 
     * @return true si on est sur une API 11+
     */
    private static boolean canUseRotation() {
        if (android.os.Build.VERSION.RELEASE.startsWith("1.")
                || android.os.Build.VERSION.RELEASE.startsWith("2.")) {
            return false;
        }
        return true;
    }

    private void requestIntersticial() {
        AdRequest adRequest = new AdRequest();
        adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
        adRequest.addTestDevice("E94B8BE128FA526DD52F79B4963BF468");

        // Display Ad after Game 2, 7, 12, 17, 22, etc.
        if (this.showAd || this.gameCount % 5 == 1) {
            this.showAd = true;
            if (interstitialAd != null) {
                interstitialAd.loadAd(adRequest);
            }
        }
    }

    private class MyIntersticialAdListener implements AdListener {

        @Override
        public void onDismissScreen(Ad ad) {
            requestIntersticial();
        }

        @Override
        public void onFailedToReceiveAd(Ad ad, ErrorCode errorCode) {

            Log.e(TAG, "onFailedToReceiveAd:ad=" + ad + ";errorCode="
                    + errorCode);
        }

        @Override
        public void onLeaveApplication(Ad ad) {
            Log.d(TAG, "onLeaveApplication:ad=" + ad);
        }

        @Override
        public void onPresentScreen(Ad ad) {
            Log.d(TAG, "onPresentScreen:ad=" + ad);
        }

        @Override
        public void onReceiveAd(Ad ad) {
            Log.d(TAG, "onReceiveAd:ad=" + ad);
        }
    }
}
