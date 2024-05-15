//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import java.awt.FlowLayout;

import javax.swing.JFrame;

import uk.ac.leedsbeckett.oop.OOPGraphics;

public class TurtleGraphics extends OOPGraphics
{
    public static void main(String[] args)
    {
        new TurtleGraphics(); //create instance of class that extends OOPGraphics (could be separate class without main), gets out of static context
    }

    public TurtleGraphics()
    {
        JFrame MainFrame = new JFrame();                //create a frame to display the turtle panel on
        MainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Make sure the app exits when closed
        GraphicsSystem gs = new GraphicsSystem();
        MainFrame.setLayout(new FlowLayout());  //not strictly necessary
        MainFrame.add(gs);                                    //"this" is this object that extends turtle graphics we are adding a turtle graphics panel to the frame
        MainFrame.pack();                                               //set the frame to a size we can see
        MainFrame.setVisible(true);                             //now display it
        about();
// call the OOPGraphics about method to display version information

    }


    public void processCommand(String command)      //this method must be provided because OOPGraphics will call it when it's JTextField is used
    {
        //String parameter is the text typed into the OOPGraphics JTextfield
        //lands here if return was pressed or "ok" JButton clicked

        //TO DO
    }
}