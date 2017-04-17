/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gold.daniel.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 *
 * @author wrksttnpc
 */
public class Fonts
{
    public static BitmapFont TITLE_FONT;
    public static BitmapFont OPTIONS_FONT;
    
    public static GlyphLayout TITLE_GLYPH_LAYOUT;
    
    public static void loadFonts()
    {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/comic.ttf"));
        {

            FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

            params.size = 128;
            TITLE_FONT = generator.generateFont(params);
            TITLE_GLYPH_LAYOUT = new GlyphLayout(TITLE_FONT, "MAIN MENU");

            params.size = 40;
            OPTIONS_FONT = generator.generateFont(params);
        }
        generator.dispose();
    }
}
