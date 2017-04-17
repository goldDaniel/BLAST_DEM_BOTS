/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gold.daniel.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 *
 * @author wrksttnpc
 */
public class Sounds
{
    public static Sound MENU_SOUND = Gdx.audio.newSound(Gdx.files.internal("audio/menu_new.mp3"));
    public static Sound NAV_SOUND = Gdx.audio.newSound(Gdx.files.internal("audio/GUI/misc_menu.wav"));
}
