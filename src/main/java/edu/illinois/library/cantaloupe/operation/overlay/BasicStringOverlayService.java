package edu.illinois.library.cantaloupe.operation.overlay;

import edu.illinois.library.cantaloupe.config.Configuration;
import edu.illinois.library.cantaloupe.config.ConfigurationException;
import edu.illinois.library.cantaloupe.config.ConfigurationFactory;

import java.awt.Color;
import java.awt.Font;

class BasicStringOverlayService extends BasicOverlayService {

    static final String COLOR_CONFIG_KEY =
            "overlays.BasicStrategy.string.color";
    static final String FONT_CONFIG_KEY =
            "overlays.BasicStrategy.string.font";
    static final String FONT_SIZE_CONFIG_KEY =
            "overlays.BasicStrategy.string.font.size";
    static final String STRING_CONFIG_KEY =
            "overlays.BasicStrategy.string";
    static final String STROKE_COLOR_CONFIG_KEY =
            "overlays.BasicStrategy.string.stroke.color";
    static final String STROKE_WIDTH_CONFIG_KEY =
            "overlays.BasicStrategy.string.stroke.width";

    private Color color;
    private Font font;
    private String string;
    private Color strokeColor;
    private float strokeWidth;

    BasicStringOverlayService() throws ConfigurationException {
        super();
        readConfig();
    }

    StringOverlay getOverlay() {
        return new StringOverlay(string, getPosition(), getInset(), font,
                color, strokeColor, strokeWidth);
    }

    private void readConfig() throws ConfigurationException {
        final Configuration config = ConfigurationFactory.getInstance();
        // Fill color
        color = ColorUtil.fromString(config.getString(COLOR_CONFIG_KEY));
        // Font
        font = new Font(config.getString(FONT_CONFIG_KEY), Font.PLAIN,
                config.getInt(FONT_SIZE_CONFIG_KEY, 18));
        // String
        string = config.getString(STRING_CONFIG_KEY, "");
        // Stroke color
        strokeColor = ColorUtil.fromString(
                config.getString(STROKE_COLOR_CONFIG_KEY, "black"));
        // Stroke width
        strokeWidth = config.getFloat(STROKE_WIDTH_CONFIG_KEY, 2f);
    }

}
