package ScotlandYardFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

class DataPanel extends JPanel {
	
	public DataPanel(int width, int height){
		this.setPreferredSize(new Dimension(width, height));
		this.setBackground(Color.BLACK);
		this.add(new StatusPanel(width, 450));
		this.setVisible(true);
	}
	
	class StatusPanel extends JPanel {
		
		public StatusPanel(int width, int height){
			this.setPreferredSize(new Dimension(width, height));
			this.setBackground(Color.BLUE);
			this.setLayout(new GridLayout(3,2));
			this.add(new PersonStatusPanel());
			this.add(new PersonStatusPanel());
			this.add(new PersonStatusPanel());
			this.add(new PersonStatusPanel());
			//this.add(new PersonStatusPanel(width/2, height/3));
			//this.add(new PersonStatusPanel(width/2, height/3));
			this.setVisible(true);
		}
		
	}
	
	class PersonStatusPanel extends JPanel {
		
		public PersonStatusPanel(){
			this.setBackground(Color.PINK);
			this.setBorder(new BevelBorder(BevelBorder.RAISED));
			this.setVisible(true);
			this.add(new JLabel("Name"));
			//this.
		}
	}
	
}

