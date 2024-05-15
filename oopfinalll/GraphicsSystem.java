import java.awt.*;
import javax.swing.*;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import uk.ac.leedsbeckett.oop.OOPGraphics;

public class GraphicsSystem extends OOPGraphics{

    int x , y , i;
    public int imageCaptured= 0;
    float penWidth;

    private BufferedImage getsavedimage() {
        return this.getBufferedImage();
    }

    private boolean isImageUnsaved = true;

    // Method to check if the current image is unsaved
    private boolean isImageUnsaved() {
        return isImageUnsaved;
    }

    // Method to mark the image as saved
    private void imageSaved() {
        isImageUnsaved = false;
    }

    // Method to mark the image as modified (unsaved)
    private void imageModified() {
        isImageUnsaved = true;
    }

    private List<String> commandHistory = new ArrayList<>();

    public void setsavedimage(BufferedImage img) {
        this.setBufferedImage(img);
    }
    public GraphicsSystem() {
        clear();
        displayMessage("Aditi pokharel");
        getGraphicsContext().drawString("Aditi pokharel", 100 , 150);   //display name ....

    }

    public String ListOfCommand(String cmd) {	// list of all command
        String[] ListOfCommand = { "penup", "pendown", "about", "turnleft", "turnright", "forward", "backward", "circle", "square", "triangle", "pentagon", "penwidth", "pencolor", "red", "blue", "yellow", "pink", "green", "reset", "clear" ,"load","save", "savecommand", "loadcommand" };

        for(String check : ListOfCommand) {
            if (check.equals(cmd))
                return check;
        }
        return "";
    }

    private boolean checkAndPromptSave() {
        if (isImageUnsaved()) {
            // Prompt the user with a dialog to save the current state
            int choice = JOptionPane.showConfirmDialog(null, "The current state has not been saved. Would you like to save it before proceeding?", "Unsaved Changes", JOptionPane.YES_NO_CANCEL_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                // The user chose to save the current state
                save(); // Call the save() method to save the current state
                return true; // Return true indicating the user wants to proceed after saving
            } else if (choice == JOptionPane.NO_OPTION) {
                // The user chose not to save and proceed
                return true; // Return true indicating the user wants to proceed without saving
            } else {
                // The user chose to cancel
                return false; // Return false indicating the user does not want to proceed
            }
        }
        return true; // Return true if there were no unsaved changes
    }

    private void saveCommands() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Commands");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            // Ensure the file path ends with the appropriate extension
            if (!file.getAbsolutePath().endsWith(".txt")) {
                file = new File(file.getAbsolutePath() + ".txt");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String command : commandHistory) {
                    writer.write(command);
                    writer.newLine(); // Write each command on a new line
                }
                JOptionPane.showMessageDialog(null, "Commands have been saved to: " + file.getAbsolutePath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error saving commands: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }



    // Method to load commands from a text file and execute them
    private void loadCommands() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Commands");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String command;
                while ((command = reader.readLine()) != null) {
                    // Add the command to the history and execute it
                    commandHistory.add(command);
                    processCommand(command);
                }
                JOptionPane.showMessageDialog(null, "Commands have been loaded from: " + file.getAbsolutePath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error loading commands: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    public void processCommand(String command) {   //this method must be provided because LBUGraphics will call it when it's JTextField is used

        String[] str = command.split(" ");
        String cmd = ListOfCommand(str[0]);

        System.out.println("Given Command: " + cmd);
        int parameter = 0;

        if (str.length > 1) {
            parameter = -1;

            try {
                parameter = Integer.parseInt(str[1]);
            } catch (Exception error) {
                JOptionPane.showMessageDialog(null, "Invalid: You have entered a non numeric parameter!");
                parameter = -2;
            }
        }

        // call all the command
        switch (cmd.toLowerCase()) {
            case "about":
                if (command.equalsIgnoreCase("about")) {
                    about(); // display the dancing turtle if command is "about"

                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Parameter have been entered!");
                }
                break;

            case "savecommand":
                if (command.equalsIgnoreCase("savecommand")) {
                    saveCommands();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Parameter have been entered!");
                }
                break;

            case "loadcommand":
                if (command.equalsIgnoreCase("loadcommand")) {
                    loadCommands();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Parameter have been entered!");
                }
                break;

            case "penup":
                if (parameter != 0) {
                    JOptionPane.showMessageDialog(null, "Invalid Parameter have been entered!");
                } else {
                    this.penUp();
                    imageCaptured = 0;
                }
                break;

            case "pendown":
                if (parameter != 0) {
                    JOptionPane.showMessageDialog(null, "Invalid Parameter have been entered!");
                } else {
                    this.penDown();
                    imageCaptured = 0;
                }
                break;

            case "turnleft":
                if (str.length < 2) {
                    // Handle the case where the parameter is missing
                    JOptionPane.showMessageDialog(null, "Parameter is missing. Please provide a parameter between 0 and 360 degrees.");
                } else if (parameter == -2 || parameter < 0 || parameter > 360) {
                    JOptionPane.showMessageDialog(null, "Invalid Parameter have been entered!");
                } else {
                    this.turnLeft(parameter);
                    imageCaptured = 0;
                }
                break;

            case "turnright":
                if (str.length < 2) {
                    // Handle the case where the parameter is missing
                    JOptionPane.showMessageDialog(null, "Parameter is missing. Please provide a parameter between 0 and 360 degrees.");
                }
                else if (parameter == -2 || parameter < 0 || parameter > 360) {
                    JOptionPane.showMessageDialog(null, "Invalid Parameter have been entered!");
                } else {
                    this.turnRight(parameter);
                    imageCaptured = 0;
                }
                break;

            case "forward":
                if (str.length < 2) {
                    // Handle the case where the parameter is missing
                    JOptionPane.showMessageDialog(null, "Parameter is missing. Please provide a parameter between 0 and 160 degrees.");
                }
                else if (parameter == -2 || parameter < 0 || parameter > 160) {
                    JOptionPane.showMessageDialog(null, "Invalid Parameter have been entered!");
                } else {
                    this.forward(parameter);
                    imageCaptured = 0;
                }
                break;

            case "backward":
                if (str.length < 2) {
                    // Handle the case where the parameter is missing
                    JOptionPane.showMessageDialog(null, "Parameter is missing. Please provide a parameter between 0 and 160 degrees.");
                }
                else if (parameter == -2 || parameter < 0 || parameter > 160) {
                    JOptionPane.showMessageDialog(null, "Invalid Parameter have been entered!");
                } else {
                    this.forward(-parameter);
                    imageCaptured = 0;
                }
                break;

            case "circle":
                if (str.length < 2) {
                    // Handle the case where the parameter is missing
                    JOptionPane.showMessageDialog(null, "Parameter is missing. Please provide a parameter between 0 and 140 degrees.");
                }
                else if (parameter == -2 || parameter < 0 || parameter > 140) {
                    JOptionPane.showMessageDialog(null, "Invalid Parameter have been entered!");
                } else {
                    this.circle(parameter);
                    imageCaptured = 0;
                }
                break;

            case "square":
                if (str.length < 2) {
                    // Handle the case where the parameter is missing
                    JOptionPane.showMessageDialog(null, "Parameter is missing. Please provide a parameter between 0 and 140 degrees.");
                }
                else if (parameter == -2 || parameter < 0 || parameter > 140) {
                    JOptionPane.showMessageDialog(null, "Invalid Parameter have been entered!");
                } else {
                    for (int i = 0; i < 4; i++) {
                        this.forward(parameter);
                        this.turnRight(90);
                    }
                    imageCaptured = 0;
                }
                break;

            case "triangle":
                if (str.length < 2) {
                    // Handle the case where the parameter is missing
                    JOptionPane.showMessageDialog(null, "Parameter is missing. Please provide a parameter between 0 and 140 degrees.");
                }
                else if (parameter == -2 || parameter < 0 || parameter > 140) {
                    JOptionPane.showMessageDialog(null, "Invalid Parameter have been entered!");
                } else {
                    this.penDown();
                    for (int i = 0; i < 3; i++) {
                        this.forward(parameter);
                        this.turnLeft(120);
                    }
                    this.penUp();
                    imageCaptured = 0;
                }
                break;

            case "pentagon":
                if (str.length < 2) {
                    // Handle the case where the parameter is missing
                    JOptionPane.showMessageDialog(null, "Parameter is missing. Please provide a parameter between 0 and 140 degrees.");
                }
                else if (parameter == -2 || parameter < 0 || parameter > 140) {
                    JOptionPane.showMessageDialog(null, "Invalid Parameter has been entered!");
                } else {
                    penDown();
                    for (int i = 0; i < 5; i++) {
                        forward(parameter);
                        turnRight(72);
                    }
                    penUp();
                    imageCaptured = 0;
                }
                break;

            case "penwidth":
                setStroke(parameter);
                break;

            case "pencolor":
                if (str.length < 4) {
                    JOptionPane.showMessageDialog(null, "Insufficient number of parameters for PenColor command!");
                } else {
                    int parameter2 = Integer.parseInt(str[2]);
                    int parameter3 = Integer.parseInt(str[3]);
                    if (parameter < 0 || parameter > 255 || parameter2 < 0 || parameter2 > 255 || parameter3 < 0
                            || parameter3 > 255) {
                        JOptionPane.showMessageDialog(null, "Invalid RGB values have been entered! Entered values should be below 255");
                    } else {
                        RGB(parameter, parameter2, parameter3);
                    }
                }
                break;

            case "red":
                if (parameter != 0) {
                    JOptionPane.showMessageDialog(null, "Invalid Parameter have been entered!");
                } else {
                    this.PenColour = Color.RED;
                    imageCaptured = 0;

                }
                break;

            case "blue":
                if (parameter != 0) {
                    JOptionPane.showMessageDialog(null, "Invalid Parameter have been entered!");
                } else {
                    this.PenColour = Color.BLUE;
                    imageCaptured = 0;

                }
                break;

            case "yellow":
                if (parameter != 0) {
                    JOptionPane.showMessageDialog(null, "Invalid Parameter have been entered!");
                } else {
                    this.PenColour = Color.YELLOW;
                    imageCaptured = 0;
                }
                break;

            case "green":
                if (parameter != 0) {
                    JOptionPane.showMessageDialog(null, "Invalid Parameter have been entered!");
                } else {
                    this.PenColour = Color.GREEN;
                    imageCaptured = 0;
                }
                break;

            case "pink":
                if (parameter != 0) {
                    JOptionPane.showMessageDialog(null, "Invalid Parameter have been entered!");
                } else {
                    this.PenColour = Color.PINK;
                    imageCaptured = 0;
                }
                break;

            case "reset":
                if (parameter != 0) {
                    JOptionPane.showMessageDialog(null, "Invalid parameter have been entered!");
                } else {
                    this.reset();
                    imageCaptured = 0;
                }
                break;

            case "clear":
                if (parameter != 0) {
                    JOptionPane.showMessageDialog(null, "Invalid Parameter have been entered!");
                } else {
                    if (checkAndPromptSave()) {
                    clear();
                    imageCaptured = 0;
                    }
                }
                break;

            case "load":
                load();
                break;

            case "save":
                save();
                break;

            default:
                JOptionPane.showMessageDialog(null, "Invalid Command has been entered!");
                break;
        }

        commandHistory.add(command);

    }

    public void RGB(int redValue, int greenValue, int blueValue) {
        Color rgbColour = new Color(redValue, greenValue, blueValue);
        setPenColour(rgbColour);

    }



    private void setPenColor(String color) {
        switch (color) {
            case "red":
                setPenColour(Color.RED);
                break;
            case "blue":
                setPenColour(Color.BLUE);
                break;
            case "pink":
                setPenColour(Color.PINK);
                break;
            case "yellow":
                setPenColour(Color.YELLOW);
                break;
            case "green":
                setPenColour(Color.GREEN);
                break;
            case "white":
                setPenColour(Color.WHITE);
                break;
            case "cyan":
                setPenColour(Color.cyan);
                break;
            case "magenta":
                setPenColour(Color.MAGENTA);
                break;
            default:
                JOptionPane.showMessageDialog(null, " unsupported color");
                break;
        }
    }



    private void save() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Image");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "png", "jpeg", "jpg"));

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try {
                String filePath = file.getAbsolutePath();
                // Ensure the file path ends with the appropriate extension
                if (!filePath.endsWith(".png") && !filePath.endsWith(".jpeg") && !filePath.endsWith(".jpg")) {
                    filePath += ".png";
                }

                File outfile = new File(filePath);
                ImageIO.write(getBufferedImage(), "png", outfile);
                JOptionPane.showMessageDialog(null, "Image has been saved to: " + filePath);
            } catch (IOException exp) {
                JOptionPane.showMessageDialog(null, "Error saving image: " + exp.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                exp.printStackTrace();
            }
        }
    }



    private void load() {
        // Check if there is an unsaved image and prompt the user if necessary
        if (isImageUnsaved()) {
            int choice = JOptionPane.showConfirmDialog(null, "The current image has not been saved. Would you like to save it before loading a new image?", "Unsaved Image", JOptionPane.YES_NO_CANCEL_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                // Save the current image
                save();
            } else if (choice == JOptionPane.CANCEL_OPTION) {
                // If user cancels, return from the method
                return;
            }
        }

        // Create a file chooser to select an image file to load
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Image");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "png", "jpeg", "jpg"));

        // Show the open file dialog and get the user's selection
        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                // Read the image from the selected file
                BufferedImage image = ImageIO.read(file);
                // Set the loaded image to the current image buffer and repaint
                setBufferedImage(image);
                repaint();
                // Notify the user that the image has been loaded successfully
                JOptionPane.showMessageDialog(null, "Image has been loaded from: " + file.getAbsolutePath());
            } catch (IOException error) {
                // Handle any errors that occur during the image loading process
                JOptionPane.showMessageDialog(null, "Error loading image: " + error.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                error.printStackTrace();
            }
        }
    }

    public void about() {
        super.about();
        //setting the turtle pen in the right position
        clear();
        reset();
        setTurtleSpeed(1);
        forward(40);
        turnRight();
        forward(370);
        turnRight(180);
        setStroke(4);
        setPenColour(Color.red);
        //for a
        forward (20) ;
        turnRight () ;
        forward (100) ;
        turnLeft (160) ;
        penDown () ;
        forward (100) ;
        turnRight (150) ;
        forward (100) ;
        penUp () ;
        forward(-50);
        turnRight (100) ;
        penDown () ;
        forward (27);
        penUp () ;
        turnLeft ();
        forward (50);
        turnLeft () ;
        forward (70);




        //for D
        forward(-20); //gaps between letters
        turnLeft(90);
        penDown();
        forward(100);
        turnRight();
        for (int i = 0; i < 11.5; i++) {  // Loops through the drawing process for approximately 11 iterations
            forward(15);
            turnRight(16); }






        //for i
        penUp () ;
        pointTurtle (90) ;
        forward (75) ;
        penDown () ;
        turnLeft () ;
        forward (100) ;
        penUp () ;
        turnRight (180) ;
        forward (100) ;
        turnLeft();
        forward (40) ;

        //fort
        turnLeft (90) ;
        penDown () ;
        forward (100) ;
        turnLeft () ;
        penUp () ;
        forward (30) ;
        penDown () ;
        forward (-60) ;
        turnLeft (90) ;
        penUp () ;
        forward (100) ;
        turnLeft (90);
        forward (20) ;
        penUp () ;

        //for i
        penUp () ;
        pointTurtle (90) ;
        forward (10) ;
        penDown () ;
        turnLeft () ;
        forward (100) ;
        penUp () ;
        turnRight (180) ;
        forward (100) ;
        turnLeft();
        forward (40) ;

    }
}

