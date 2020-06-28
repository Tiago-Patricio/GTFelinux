package org.academiadecodigo.felinux.gtfo.characters.moveable.player;

import jdk.swing.interop.SwingInterOpUtils;
import org.academiadecodigo.felinux.gtfo.characters.Character;
import org.academiadecodigo.felinux.gtfo.characters.moveable.Moveable;
import org.academiadecodigo.felinux.gtfo.characters.moveable.enemies.Enemy;
import org.academiadecodigo.felinux.gtfo.field.Field;
import org.academiadecodigo.simplegraphics.graphics.Color;
import org.academiadecodigo.simplegraphics.graphics.Rectangle;
import org.academiadecodigo.simplegraphics.keyboard.KeyboardHandler;
import org.academiadecodigo.simplegraphics.pictures.Picture;



public class Player extends Character implements Moveable{

    private Picture player;
    private boolean dead = false;
    private int energy = 1000; //animation scalled to 100
    private boolean hasMilk = false;
    private int clawDamage = 1;
    private Picture energyBar;
    private Rectangle energyAnimation;
    private Picture hpBar;
    private Rectangle hpAnimation;

    //284 - 318    Y road size Left
    //004                                                 //1292 X
    //Castelo fica em 1180x - 100y
    // AC  fica em  1126 - 1124


    //These are used for movement
    public static float dx;
    public static float dy;


     public Player(String name) {
        super();
        super.setLives(7);
        this.player = new Picture(50,100,"resources/images/" + name);
        this.energyBar = new Picture(5, 5, "resources/images/EmptyEnergyBar.png");
        this.hpBar = new Picture(162, 5, "resources/images/EmptyHpBar.png");
        //ENERGY BAR
        this.energyAnimation = new Rectangle(55, 27, 100, 10); //pos x pos y size size
        energyAnimation.setColor(new Color(255,255,0));
        //HP BAR
        this.hpAnimation = new Rectangle(207, 26, 100, 10);
        hpAnimation.setColor(new Color(255,0,0));
    }

    public void energyDecay(){
        if(this.energy <= 0){
            if(super.getLives() <= 1) {
                this.dead = true;
                //HP BAR
                hpAnimation.translate( -7,0);
                hpAnimation.grow(-7,0);
                return;
            }
            this.takeLethalDamage();
            this.energyReset();
            //HP BAR
            hpAnimation.translate( -7,0);
            hpAnimation.grow(-7,0);
        }
        this.loseEnergy();
        energyAnimation.translate(-0.5,0);
        energyAnimation.grow(-0.5,0);
    }

    /**
     * GameHandler calls this method to move Player
     */
    public void move(){

        player.translate(Player.dx,Player.dy);

    }

    /**
     * Actual movement called by method move()
     */
    @Override
    public void moveLeft(){
        if(field.getPADDING_X()  >= player.getX()){
            return;
        } player.translate(-field.getCellSize(),0);
    }

    @Override
    public void moveUp(){
        if(field.getPADDING_Y() >= player.getY()){
            return;
        } player.translate(0,-field.getCellSize());
    }

    @Override
    public void moveRight(){
        if(field.getSizeCol() <= player.getMaxX() - field.getPADDING_X()){
            return;
        } player.translate(field.getCellSize(),0);
    }

    @Override
    public void moveDown(){
        if(field.getSizeRow() <= player.getMaxY() - field.getPADDING_Y()){
            return;
        } player.translate(0, field.getCellSize());
    }                                            //Castelo fica em 1180x - 100y

    public Picture getPlayer() {
        return player;
    }

    public Picture getEnergyBar() {
        return energyBar;
    }

    public Picture getHpBar() {
        return hpBar;
    }

    public Rectangle getEnergyAnimation() {
        return energyAnimation;
    }

    public Rectangle getHpAnimation() {
        return hpAnimation;
    }

    public void energyReset(){
        this.setEnergy(100);
        energyAnimation.translate(50, 0);  //funciona com metade dos valores, dunno why
        energyAnimation.grow(50, 0);
    }

    public boolean isDead() {
        return dead;
    }

    public void attack(Enemy enemy) throws NullPointerException {

        //Fazer diferenca entre o xMIN deles e ser menor que 5, por exemplo  - valores absolutos
        //Fazer diferenca entre o YMIN deles e ser menor que 5, por exemplo  - valores absolutos
        //Fazer diferenca entre o xMAX deles e ser menor que 5, por exemplo  - valores absolutos
        //Fazer diferenca entre o YMAX deles e ser menor que 5, por exemplo  - valores absolutos
        if (200>(Math.abs(this.getX()-enemy.getEnemy().getX()))&&200>(Math.abs(this.getY()-enemy.getEnemy().getY()))) {


            if (enemy.getLives() <= 1) {
                enemy.setDead();
                return;
            }
            enemy.setLives(enemy.getLives() - 1);
            System.out.println(enemy.getLives());
        }
    }

    public void loseEnergy() {
        this.energy--;
    }

    public void stealMilk() {
        this.hasMilk = true;
    }

    public int getClawDamage() {
        return this.clawDamage;
    }

    public int getEnergy() {
        return energy;
    }

    @Override
    public int getLives() {
        return super.getLives();
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
    public void gainLife() {
        super.setLives(super.getLives() + 1);
        hpAnimation.translate( 29,0);
    }

    private boolean isWalkable() {
        return (field.isWalkable(player.getX(), player.getY()));
    }
}