package keypad;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class KeypadCommander extends JPanel implements ActionListener {

	// Document display by the text field

	private static Document display = null;

	/**
	 * Creates a key pad panel containing a button for each digit (0 - 9) And a
	 * text field for displaying what's pressed
	 */

	public KeypadCommander() {
		super(new BorderLayout());

		// Create a panel for the buttons
		// We'll use a GridLayout to display the buttons in a grid

		JPanel buttons = new JPanel(new GridLayout(3, 5));

		for (int i = 0; i < 15; i++) {
			String buttonRef = new String();

			switch (i) {
			case 1:
				buttonRef = "UP";
				break;

			case 3:
				buttonRef = "+";
				break;

			case 4:
				buttonRef = "OPEN";
				break;

			case 5:
				buttonRef = "LEFT";
				break;

			case 7:
				buttonRef = "RIGHT";
				break;

			case 8:
				buttonRef = "-";
				break;

			case 9:
				buttonRef = "CLOSE";
				break;

			case 11:
				buttonRef = "DOWN";
				break;

			case 13:
				buttonRef = "STOP";
				break;

			default:
				break;
			}
			JButton button = new JButton(buttonRef);

			// By adding an ActionListener to our button we
			// can get notified when the button has been pressed

			button.addActionListener(this);
			buttons.add(button);
		}

		// Create a text field to display the keys entered

		JTextField displayField = new JTextField();
		display = displayField.getDocument();
		displayField.setEditable(false);
		add(BorderLayout.CENTER, buttons);
		add(BorderLayout.SOUTH, displayField);
	}

	/**
	 * Create and setup the main window
	 */

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("Keypad");

		// We want the application to exit when the window is closed

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add the key pad to window

		frame.getContentPane().add(new KeypadCommander());

		// Display the window.

		frame.pack();
		frame.setVisible(true);

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				String typed = new String();
				if (e.getID() == KeyEvent.KEY_PRESSED) {
					if (e.getKeyCode() == KeyEvent.VK_LEFT) {
						typed = "LEFT";

					}
					if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
						typed = "RIGHT";

					}

					if (e.getKeyCode() == KeyEvent.VK_UP) {
						typed = "UP";

					}
					if (e.getKeyCode() == KeyEvent.VK_DOWN) {
						typed = "DOWN";

					}

					if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
						System.exit(0);

					}

					updateDisplay(typed);

				}

				return false;
			}
		});

	}

	private static void updateDisplay(String str) {

		try {
			display.insertString(display.getLength(), str + " ", null);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Called when a button is pressed
	 */

	@Override
	public void actionPerformed(ActionEvent event) {

		// Determine which key was pressed

		String key = event.getActionCommand();

		// Insert the pressed key into our text fields Document

		updateDisplay(key);

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}

class MyKeyListener extends KeyAdapter {
	public void keyPressed(KeyEvent evt) {
		if (evt.getKeyChar() == 'a') {
			System.out.println("Check for key characters: " + evt.getKeyChar());
		}
		if (evt.getKeyCode() == KeyEvent.VK_HOME) {
			System.out.println("Check for key codes: " + evt.getKeyCode());
		}
	}
}
