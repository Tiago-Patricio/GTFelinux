package org.academiadecodigo.felinux.gtfo.game;

import org.academiadecodigo.felinux.gtfo.characters.Character;
import org.academiadecodigo.felinux.gtfo.characters.CheckpointType;
import org.academiadecodigo.felinux.gtfo.characters.Milk;
import org.academiadecodigo.felinux.gtfo.characters.enemies.Enemy;
import org.academiadecodigo.felinux.gtfo.characters.enemies.EnemyType;
import org.academiadecodigo.felinux.gtfo.characters.npcs.CatProstitute;
import org.academiadecodigo.felinux.gtfo.characters.npcs.Npc;
import org.academiadecodigo.felinux.gtfo.characters.npcs.NpcType;
import org.academiadecodigo.felinux.gtfo.characters.player.Player;
import org.academiadecodigo.felinux.gtfo.characters.player.PlayerKeyboard;
import org.academiadecodigo.felinux.gtfo.field.Area;
import org.academiadecodigo.felinux.gtfo.field.Field;
import org.academiadecodigo.simplegraphics.graphics.Canvas;
import org.academiadecodigo.simplegraphics.pictures.Picture;

import java.util.HashMap;


public class GameHandler implements Runnable {

    private static Player player;
    private static Enemy[] enemies = new Enemy[2];
    private static Npc[] rats = new Npc[5];
    private static Milk milk;
    private static Field field;
    private  static Picture oldLady ;
    private PlayerKeyboard playerKeyboard;
    private static Npc[] assaultableCats = new Npc[13];
    private static Npc[] catProstitute = new Npc[4];
    public static boolean firstMap = true;
    private int oneUpTimer;

    public static HashMap<Area, Character> hashMap;
    private static final int INTERACT_RANGE = 25;
    private static Area mapArea;

    public void init() {

        field = new Field();
        GameSound.BACKMUSIC.sounds.play(true);

        //Creates everything that is visual in the Canvas
        this.milk = new Milk();

        //Player
        this.player = new Player("tobias.png");
        this.playerKeyboard = new PlayerKeyboard(player, enemies[0]);
        this.oldLady = new Picture(120,70,"resources/OldLady.png");

        //interaction
        hashMap = new HashMap<>();
        mapArea = new Area(1169,189,100,100);
    }

    public void startGame() {

        GameSound.BACKMUSIC.sounds.play(!player.isDead());
        instanceOfAssaultableCats(assaultableCats);
        instanceOfCatProstitute(catProstitute);
        instanceOfEnemies(enemies);
        instanceOfRats(rats);
        addInteractables();
        showAlways();
        showAllMap1();
        run();
    }

    //Interact with player
    private void addInteractables() {

        for (Npc rat : rats) {
            rat.addToInteractables();
        }

        for (Npc cat : assaultableCats) {
            cat.addToInteractables();
        }

        for (Npc cat : catProstitute) {
            cat.addToInteractables();
        }

        /**
         * Game Boss
         */
        enemies[0].addToInteractables();
        enemies[1].addToInteractables();
    }

    private void showAlways() {

        /** First Game Map **/
        field.getMap().draw();

        /**Show Player**/
        player.getPlayer().draw();

        /** Second Game Map **/
        /** User Interface **/
        player.getEnergyBar().draw();
        player.getHpBar().draw();
        player.getEnergyAnimation().fill();
        player.getHpAnimation().fill();
    }

    private static void showAllMap1() {

        //Objects

        /**NPCs**/
        for (int i = 0; i < assaultableCats.length; i++) {
            assaultableCats[i].getNpc().draw();
            assaultableCats[i].getRedLifeBar().fill();
            assaultableCats[i].getGreenLifeBar().fill();
        }


        catProstitute[0].getNpc().draw();
        catProstitute[1].getNpc().draw();
        catProstitute[2].getNpc().draw();


        /**Characters **/

        oldLady.draw();

        enemies[0].getEnemy().draw();
    }

    private static void hideAllMap1() {

        /** First Game Map **/

        Canvas.getInstance().hide(oldLady);

        /**NPCs**/
        for (int i = 0; i < assaultableCats.length; i++) {
            Canvas.getInstance().hide(assaultableCats[i].getNpc());
            Canvas.getInstance().hide(assaultableCats[i].getRedLifeBar());
            Canvas.getInstance().hide(assaultableCats[i].getGreenLifeBar());
        }

        Canvas.getInstance().hide(catProstitute[0].getNpc());
        Canvas.getInstance().hide(catProstitute[1].getNpc());
        Canvas.getInstance().hide(catProstitute[2].getNpc());

        /**Characters **/
        //Make a for loop when more enemies here
        Canvas.getInstance().hide(enemies[0].getEnemy());
        Canvas.getInstance().hide(enemies[0].getEnemyField().getArea().getBoundArea());

        Canvas.getInstance().hide(enemies[1].getEnemy());
        Canvas.getInstance().hide(enemies[1].getEnemyField().getArea().getBoundArea());

    }

    public static void showAllMap2() {

        for (Area area : field.getNotWalkableMap2()) {
            area.getBoundArea().draw();
        }

        catProstitute[3].getNpc().draw();
        milk.drawMilk();

        enemies[1].getEnemy().draw();
        enemies[1].getEnemyField().getArea().getBoundArea().draw();

        for (int i = 0; i < rats.length; i++) {
            rats[i].getNpc().draw();
        }
    }

    public static void hideAllMap2() {

        for (Area area : field.getNotWalkableMap2()) {
            Canvas.getInstance().hide(area.getBoundArea());

        }
        Canvas.getInstance().hide(milk.getMilk());

        for (int i = 0; i < rats.length; i++) {
            Canvas.getInstance().hide(rats[i].getNpc());
        }

        Canvas.getInstance().hide(enemies[1].getEnemy());
        Canvas.getInstance().hide(enemies[1].getEnemyField().getArea().getBoundArea());

        Canvas.getInstance().hide(catProstitute[3].getNpc());
    }

    private void moveAll(){


        //insert for loop to run enemies with an enemy counter to avoid a Null Pointer
        try {

            for (int i = 0; i < assaultableCats.length; i++) {
                assaultableCats[i].move();
            }

            player.move();
            player.energyDecay();

            enemies[0].move();
            enemies[1].move();

            for (int i = 0; i < rats.length; i++) {
                rats[i].move();
            }

        } catch (Exception e) {
            //do nothing
        }
    }

    public static void changeMap() {

        if (firstMap) {

            Field.map.load("resources/SecondMap.png");
            hideAllMap1();
            showAllMap2();
            GameSound.DOOR.sounds.play(true);
            firstMap = false;
            return;
        }

        Field.map.load("resources/FirstMap.png");
        hideAllMap2();
        showAllMap1();
        GameSound.DOOR.sounds.play(true);
        firstMap = true;
    }

    private void instanceOfRats(Npc[] rats) {

        int[][] ratPos = new int[8][2];

        ratPos[0][0] = 566;
        ratPos[0][1] = 207;

        ratPos[1][0] = 488;
        ratPos[1][1] = 324;

        ratPos[2][0] = 986;
        ratPos[2][1] = 291;

        ratPos[3][0] = 785;
        ratPos[3][1] = 273;

        ratPos[4][0] = 466;
        ratPos[4][1] = 207;

        for (int i = 0; i < rats.length; i++) {
            rats[i] = Factory.npcFactory(NpcType.RAT, ratPos[i][0] ,ratPos[i][1], i);
        }
    }

    private void instanceOfCatProstitute(Npc[] catProstitute) {

        int[][] catPro = new int[4][2];

        catPro[0][0] = 390;
        catPro[0][1] = 540;
        catPro[1][0] = 800;
        catPro[1][1] = 96;
        catPro[2][0] = 1124;
        catPro[2][1] = 465;
        catPro[3][0] = 971;
        catPro[3][1] = 339;

        catProstitute[0] = Factory.npcFactory(NpcType.CAT_PROSTITUTE, catPro[0][0], catPro[0][1], 0);
        ((CatProstitute)catProstitute[0]).setCheckpoint(CheckpointType.CHECKPOINT1);
        catProstitute[1] = Factory.npcFactory(NpcType.CAT_PROSTITUTE, catPro[1][0], catPro[1][1], 0);
        ((CatProstitute)catProstitute[1]).setCheckpoint(CheckpointType.CHECKPOINT2);
        catProstitute[2] = Factory.npcFactory(NpcType.CAT_PROSTITUTE, catPro[2][0], catPro[2][1], 0);
        ((CatProstitute)catProstitute[2]).setCheckpoint(CheckpointType.CHECKPOINT3);
        catProstitute[3] = Factory.npcFactory(NpcType.CAT_PROSTITUTE, catPro[3][0], catPro[3][1], 0);
        ((CatProstitute)catProstitute[3]).setCheckpoint(CheckpointType.CHECKPOINTMAP2);

    }

    private void instanceOfEnemies(Enemy[] enemies){

        int[][] enemyPos = new int[4][2];
        enemyPos[0][0] = 110;
        enemyPos[0][1] = 350;
        enemyPos[1][0] = 220;
        enemyPos[1][1] = 200;

        enemies[0] = Factory.enemyFactory(EnemyType.COP_CAR, enemyPos[0][0], enemyPos[0][1], "copCar");
        enemies[1] = Factory.enemyFactory(EnemyType.COW_BOSS, enemyPos[1][0], enemyPos[1][1], "Cow2");
    }

    private void instanceOfAssaultableCats(Npc[] assaultableCats) {
        int[][] assaultableCatPos = new int[13][2];
        //Vertical
        assaultableCatPos[0][0] = 440;
        assaultableCatPos[0][1] = 235;

        //Horizontal
        assaultableCatPos[1][0] = 671;
        assaultableCatPos[1][1] = 300;

        // Vertical
        assaultableCatPos[2][0] = 488;
        assaultableCatPos[2][1] = 456;

        //Horizontal
        assaultableCatPos[3][0] = 764;
        assaultableCatPos[3][1] = 594;


        //Vertical
        assaultableCatPos[4][0] = 950;
        assaultableCatPos[4][1] = 105;

        //Horizontal
        assaultableCatPos[5][0] = 1082;
        assaultableCatPos[5][1] = 528;

        // Vertical
        assaultableCatPos[6][0] = 641;
        assaultableCatPos[6][1] = 429;
        //H
        assaultableCatPos[7][0] = 1091;
        assaultableCatPos[7][1] = 609;
        //V
        assaultableCatPos[8][0] = 908;
        assaultableCatPos[8][1] = 462;
        //H
        assaultableCatPos[9][0] = 158;
        assaultableCatPos[9][1] = 531;

        // V
        assaultableCatPos[10][0] = 23;
        assaultableCatPos[10][1] = 420;

        //H
        assaultableCatPos[11][0] = 320;
        assaultableCatPos[11][1] = 402;

        assaultableCatPos[12][0] = 1253;
        assaultableCatPos[12][1] = 230;

        // CREATE ASSAULTABLE CATS
        for (int i = 0; i < 13; i++) {
            assaultableCats[i] = Factory.npcFactory(NpcType.ASSAULTABLE_CAT, assaultableCatPos[i][0], assaultableCatPos[i][1], i);
        }
    }

    /**
     * CALL THIS TO INTERACT WITH STUFF
     *
     * @return Character to interact with,
     * or player reference if no interactible objects in range
     * <p>
     * PLAYER BY DEFAULT
     */
    public static Character checkInteraction() {

        Area interactTarget;

        for (Area area : hashMap.keySet()) {

            interactTarget = area;

            if(Area.checkInteract(player.getArea(),interactTarget, INTERACT_RANGE)){

                //Returns a character
                return hashMap.get(interactTarget);
            }
        }
        return player;
    }

    /**
     * Check milk location
     * @return true if u got it right
     */
    public static boolean checkMilk() {

        if(Area.checkInteract(player.getArea(), milk.getArea(), INTERACT_RANGE)){
            player.setHasMilk();
            milk.makeMilkDisappear();
            return true;
        }

        return false;
    }

    public static boolean canEnterCastle() {

        if (firstMap) {
            return(Area.checkInteract(player.getArea(), mapArea, 75));
        }
        return false;
    }

    public enum GameSound {
        CATMEOW(new Sounds("/resources/MEOW.wav")),
        CATCLAW(new Sounds("/resources/knife_hit17.wav")),
        CLICK(new Sounds("/resources/click.wav")),
        DOOR(new Sounds("/resources/dooropen.wav")),
        SHOOT(new Sounds("/resources/shoot.wav")),
        BACKMUSIC(new Sounds("/resources/backgroundMusic.wav"));

        public Sounds sounds;

        GameSound(Sounds sounds) {
            this.sounds = sounds;
        }

    }

    public void checkIfPlayerGainsLife() {

        if(player.isAssaultableCatIsDead()){
            player.gainLife();
        }
        if(player.isOneUpExists()){

            if(oneUpTimer == 0){
                oneUpTimer = player.getOneUp().getY();
            }
            player.getOneUp().translate(0,-1);

            if(player.getOneUp().getY() <= oneUpTimer-15){

                player.getOneUp().delete();
                player.setOneUpExists(false);
                oneUpTimer = 0;
            }
        }
    }

    public void checkIfPlayerHasMilk(){

        if(player.HasMilk()){
            milk.makeMilkDisappear();
            player.die();
        }
    }
    @Override
    public void run() {

        //Game loop to create everything!
        while (!player.isDead()) {
            try {

                Thread.sleep(35);

                //Check if player is attacking
                player.playerAttackVerification();
                checkIfPlayerGainsLife();
                checkIfPlayerHasMilk();
                moveAll();
            } catch (InterruptedException e) {
                //do nothing
            } catch (NullPointerException exception){
                //do nothing
            }
        }
        if(!player.HasMilk()){
            GameSound.BACKMUSIC.sounds.stop();
        }
    }
}