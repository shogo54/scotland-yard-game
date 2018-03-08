package ScotlandYardFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import graph.Node;
import main.Main;
import player.Detective;
import player.Person.Ticket;

/**
 * Class to show the frame of the board game
 * 
 * Has most of the components necessary for the game: canvas (JPanel), graph
 * (ScotlandYardGraph), gm (GameMaster), showMrX (boolean)
 * 
 * @author Shogo Akiyama
 *
 */
@SuppressWarnings("serial")
public class ScotlandYardFrame extends JFrame {

	// the canvas contained in the frame where we'll draw everything
	private JPanel canvas;
	// graph and pointMap of ScotlandYard
	private ScotlandYardGraph graph;
	// game master
	private GameMaster gm;

	// special option for debugging purpose
	// show MrX always
	// determined by Main.DEBUG
	private boolean showMrX;

	public ScotlandYardFrame() throws IOException {
		// read the image
		final Image img = ImageIO.read(new File("files/sybig.png"));
		graph = new ScotlandYardGraph();
		gm = new GameMaster(graph);
		showMrX = Main.DEBUG;

		canvas = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paint(Graphics graphics) {
				Graphics2D g = (Graphics2D) graphics;
				// first draw the map
				g.drawImage(img, 0, 0, null);

				// if mrx has to show up, do so
				if (gm.doesMrxShowUp() || showMrX) {
					// coloring Mrx
					Point Mrx = graph.getPointMap().get(gm.getMrx().getPosition().getName());
					g.setColor(Color.BLACK);
					g.fillOval(Mrx.x - 13, Mrx.y - 13, 26, 26);
					g.setColor(Color.WHITE);
					g.drawString("X", Mrx.x - 3, Mrx.y + 4);
				}

				// coloring 5 detectives
				for (int i = 0; i < 5; i++) {
					Detective d = gm.getDetectives().get(i);
					Point place = graph.getPointMap().get(d.getPosition().getName());
					g.setColor(Turn.identify(i).COLOR);
					g.fillOval(place.x - 13, place.y - 13, 26, 26);
					g.setColor(Color.BLACK);
					g.drawString(d.getName(), place.x - 3, place.y + 4);
				}

				// check whether there is a winner or not
				if (!(gm.checkWinner() == null)) {
					gm.setGameOver(true);
					gameSetPopup();
				}

				// ask the user to press start button at the beginning
				if (gm.getTurn() == null) {
					System.out.println("press start button: File -> Start");

					// if it is MrX's turn, move him and show his left tickets
					// and travel log
				} else if (gm.getTurn() == Turn.MrX) {
					System.out.println("--------------------------------------------------");
					System.out.println("Turn of MrX");

					try {
						gm.moveMrX();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					System.out.println("\nTickets");
					gm.showMrXTickets();
					System.out.println("\nTravel Log");
					gm.showTravelLog();
					gm.nextTurn();
					repaint();

					// if it is detective's turn, show possible moves of current
					// detective
				} else {
					Detective d = gm.getDetectives().get(gm.getTurn().NUM);
					System.out.println("--------------------------------------------------");
					System.out.println("Turn of Detective " + gm.getTurn());
					for (Node next : d.possibleMoves()) {
						Point nextPoint = graph.getPointMap().get(next.getName());
						g.setColor(gm.getTurn().COLOR);
						g.drawRect(nextPoint.x - 20, nextPoint.y - 20, 40, 40);
					}
				}
			}
		};
		// figuring out the right sequence of these next few method calls was
		// actually difficult
		// and I can't remember which stackoverflow articles helped me figure it
		// out
		canvas.setPreferredSize(new Dimension(img.getWidth(null) + 500, img.getHeight(null)+23));

		// System.out.printf("width=%d, height=%d\n", img.getWidth(null),
		// img.getHeight(null));
		this.getContentPane().add(canvas, BorderLayout.CENTER);
		this.setResizable(false);
		this.pack();
		this.setLocation(0, 0);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		addMenu();

		addMouseHandlers();

	}

	private void gameSetPopup() {
		JFrame popup = new JFrame();
		popup.setSize(280, 80);
		JPanel panel = new JPanel();
		popup.add(panel);
		JLabel message = new JLabel();
		if (gm.checkWinner() instanceof player.MrX) {
			message.setText("MrX wins! Thanks for playing!");
		}
		if (gm.checkWinner() instanceof player.Detective) {
			message.setText("Team Detective wins! Congratuations!!");
		}
		panel.add(message);

		popup.setVisible(true);
		popup.setLocation(1000, 500);
	}

	private void addMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem start = new JMenuItem("Start");
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gm.gameStart();
			}
		});
		file.add(start);
		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		file.add(exit);
		menuBar.add(file);

		// // simple label for number of moves
		// menuBar.add(new JLabel("# moves"));
		//
		// String[] numMovesString = new String[] { "0", "1", "2", "3", "4", "5"
		// };
		// JComboBox<String> numMovesSelector = new
		// JComboBox<String>(numMovesString);
		// // set max width of JComboBox
		// // http://stackoverflow.com/questions/4629015/jcombobox-width
		// numMovesSelector.setMaximumSize(numMovesSelector.getPreferredSize());
		// numMovesSelector.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// // ignore that it's an unchecked cast
		// @SuppressWarnings("unchecked")
		// JComboBox<String> source = (JComboBox<String>) e.getSource();
		// // this figures out which numbered item it is in the combobox
		// // and then sets the instance variable numMoves to that number
		// numMoves = Integer.parseInt((String) source.getSelectedItem());
		// System.out.println("num moves is now " + numMoves);
		// canvas.repaint();
		// }
		// });
		// menuBar.add(numMovesSelector);
		//
		// // checkbox to decide whether to show all moves along the way, or
		// just
		// // the most recent possibilities
		// final JCheckBoxMenuItem inclusiveCheckBox = new
		// JCheckBoxMenuItem("show all moves");
		// inclusiveCheckBox.setSelected(false);
		// menuBar.add(inclusiveCheckBox);
		// inclusiveCheckBox.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// System.out.println(e.getActionCommand());
		// inclusive = inclusiveCheckBox.isSelected();
		// canvas.repaint();
		// }
		// });
		// inclusiveCheckBox.setMaximumSize(inclusiveCheckBox.getPreferredSize());
		//
		// // Should we pay attention to the transportation types (taxi, bus,
		// // underground)
		// // that Mr X can use when we compute the next 5 moves?
		// final JCheckBoxMenuItem useTransportType = new JCheckBoxMenuItem("use
		// transport types");
		// useTransportType.setSelected(false);
		// menuBar.add(useTransportType);
		// useTransportType.addActionListener(new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// System.out.println(e.getActionCommand());
		// useTransportTypes = useTransportType.isSelected();
		// canvas.repaint();
		// }
		// });
		// useTransportType.setMaximumSize(useTransportType.getPreferredSize());
		//
		// // menu for selecting transportation types
		// // this is actually a top-level menu with a series of sub-menus
		// JMenu transportMenu = new JMenu("Transportation types");
		// // Add transportation types for up to 5 moves
		// transportMenu.add(makeTransportSelectorMenu());
		// transportMenu.add(makeTransportSelectorMenu());
		// transportMenu.add(makeTransportSelectorMenu());
		// transportMenu.add(makeTransportSelectorMenu());
		// transportMenu.add(makeTransportSelectorMenu());
		// menuBar.add(transportMenu);

		// now add the menubar to our JFrame
		setJMenuBar(menuBar);
	}

	// // Helper method to make a menu with 4 options (any, taxi, bus,
	// underground)
	// // that will update the menu title when you select one
	// private JMenu makeTransportSelectorMenu() {
	// // defaut is "any"
	// JMenu selectorMenu = new JMenu("any");
	// // add menu items for the 4 transport types
	// selectorMenu.add(new JMenuItem("any"));
	// selectorMenu.add(new JMenuItem("taxi"));
	// selectorMenu.add(new JMenuItem("bus"));
	// selectorMenu.add(new JMenuItem("underground"));
	// // now add an ActionListener that changes the name of the
	// // transport type in the JMenu and also updates the instance variable
	// // that lists the transport types
	// for (int i = 0; i < selectorMenu.getItemCount(); i++) {
	// selectorMenu.getItem(i).addActionListener(new ActionListener() {
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// // I'm not going to lie: this method is kind of a hack...
	// // But, I couldn't figure out how else to make it work.
	//
	// // transportType will be any, taxi, bus, or underground
	// String transportType = e.getActionCommand();
	// // get the JMenuItem that was clicked
	// JMenuItem menuItem = (JMenuItem) e.getSource();
	// // really indirect way to get back to the menu that this
	// // menu item was part of
	// JMenu menu = (JMenu) ((JPopupMenu) menuItem.getParent()).getInvoker();
	// // change the label of the menu to reflect the
	// // transportation type that was chosen
	// menu.setText(transportType);
	// // now get the top-level menu that owns the sub-menus for
	// // each move
	// JMenu topLevelMenu = (JMenu) ((JPopupMenu)
	// menu.getParent()).getInvoker();
	// // This is a relatively indirect way to figure out which
	// // menu
	// // contained the menu item that was clicked. But it works!
	// for (int i = 0; i < topLevelMenu.getItemCount(); i++) {
	// if (topLevelMenu.getItem(i) == menu) {
	// // we've found the menu
	// transportTypes.set(i, transportType);
	// System.out.printf("move number %d uses transport type %s\n", i,
	// transportType);
	// break;
	// }
	// }
	// }
	// });
	// }
	// return selectorMenu;
	// }

	private void addMouseHandlers() {
		canvas.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (!(gm.getTurn() == null)) {
					// if the game is ended, exit
					if (gm.isGameOver() == true)
						System.exit(0);

					// store where the user clicked
					Point clicked = e.getPoint();

					Detective d = gm.getDetectives().get(gm.getTurn().NUM);

					// if there is no possible moves, skip his turn
					if (d.possibleMoves().size() == 0) {
						System.out.println("Detective " + d.getName() + " cannot move");
						System.out.println("Skip his turn");
						gm.nextTurn();
					}

					// make the detective move to the point the user clicked
					for (Node n : d.possibleMoves()) {
						if (20 >= clicked.distance(graph.getPointMap().get(n.getName()))) {
							List<String> list = new ArrayList<String>();
							for (String way : d.getPosition().getWay(n)) {
								list.add(way);
							}
							// if there is more than one way to go, let the user
							// choose by pop up menu
							if (list.size() > 1) {
								PopupMenu menu = new PopupMenu(d, n, list);
								menu.show(e.getComponent(), e.getX(), e.getY());
								menu.setVisible(true);
								// if there is only one way, move the detective
								// to the place
							} else {
								d.moveTo(n, Ticket.valueOf(list.get(0)), gm.getMrx());
								System.out.println("");
								d.showLeftTickets();
								gm.nextTurn();
							}
							break;
						}
					}
				}
				// Finally, redraw
				canvas.repaint();
			}
		});
	}

	class PopupMenu extends JPopupMenu {

		public PopupMenu(Detective d, Node n, List<String> list) {
			for (String s : list) {
				add(makeMenuItem(d, n, s));
			}
		}

		// helper method to make Menu item
		private JMenuItem makeMenuItem(Detective d, Node n, String s) {
			JMenuItem item = new JMenuItem(s);
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					d.moveTo(n, Ticket.valueOf(s), gm.getMrx());
					System.out.println("");
					d.showLeftTickets();
					gm.nextTurn();
					canvas.repaint();
				}
			});
			return item;
		}
	}

}
