package com.wisekrakr.firstgame.overlays;


import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.wisekrakr.firstgame.MyAssetManager;


import java.awt.*;

public class TabbedPane extends Panel {

    public TabbedPane() {
        super(new GridLayout(1, 1));

        MyAssetManager myAssetManager = new MyAssetManager();
        myAssetManager.loadTextures();

        TabbedPane tabbedPane = new TabbedPane();

        Component panel1 = makeTextPanel("Panel #1");
        tabbedPane.add("Tab1", panel1);

        Component panel2 = makeTextPanel("Panel #2");
        tabbedPane.add("Tab1", panel1);

        Component panel3 = makeTextPanel("Panel #3");
        tabbedPane.add("Tab1", panel1);

        Component panel4 = makeTextPanel(
                "Panel #4 (has a preferred size of 410 x 50).");
        panel4.setPreferredSize(new Dimension(410, 50));
        tabbedPane.add("Tab1", panel1);


        //Add the tabbed pane to this panel.
        add(tabbedPane);

    }

    private Component makeTextPanel(String text) {
        Panel panel = new Panel();
        Label filler = new Label(text);
        filler.setAlignment(Label.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from
     * the event dispatch thread.
     */
    public void createAndShowGUI() {
        //Create and set up the window.
        Frame frame = new Frame("TabbedPaneDemo");


        //Add content to the window.
        frame.add(new TabbedPane(), BorderLayout.CENTER);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
}
