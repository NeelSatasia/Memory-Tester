package code;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;

public class MyPanel extends JPanel implements ActionListener {
	
	Timer timer = new Timer(600, this);
	
	GridBagLayout panelLayout;
	GridBagConstraints gbc = new GridBagConstraints();
	
	int[][] unorganizedNums = new int[4][4];
	boolean[][] numsDisplayed = new boolean[unorganizedNums.length][unorganizedNums[0].length];
	int totalNums = 5;
	
	ArrayList<Integer> playerOrderedList = new ArrayList<Integer>();
	ArrayList<Integer> completeOrderedList = new ArrayList<Integer>();
	ArrayList<int[]> indices = new ArrayList<int[]>();
	
	JButton[][] buttons = new JButton[unorganizedNums.length][unorganizedNums[0].length];
	
	JPanel firstPanel;
	JPanel secondPanel;
	
	int value = 0;
	int rowOfShownValue = 0;
	int colOfShownValue = 0;
	
	int currentValue = -1;
	
	boolean stopTime = false;
	
	boolean gameOver;
	
	int tries;
	int score;
	
	JButton triesButton = new JButton(" Tries Left: " + tries + " ");
	JButton playButton = new JButton("  Play  ");
	JButton customizeButton = new JButton(" Customize ");
	
	JLabel gameTitleLabel = new JLabel("", SwingConstants.CENTER);
	
	public MyPanel() {
		new JPanel();
		panelLayout = new GridBagLayout();
		setLayout(panelLayout);
		
		UIManager.put("Button.disabledText", Color.BLACK);
		
		goToMainPage();
		
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameOver = false;
				tries = 3;
				triesButton.setText(" Tries Left: " + tries + " ");
				
				removeMainPage();
				
				setBackground(Color.WHITE);
				
				repaint();
				
				gbc.fill = GridBagConstraints.BOTH;
				gbc.weightx = 1;
				gbc.weighty = 1;
				gbc.gridx = 0;
				gbc.gridy = 0;
				gbc.ipady = 200;
				
				firstPanel = new JPanel();
				add(firstPanel, gbc);
				firstPanel.setLayout(new GridLayout(unorganizedNums.length, unorganizedNums[0].length, 5, 5));
				firstPanel.setBackground(Color.WHITE);
				
				gbc.gridy = 1;
				gbc.ipady = 0;
				
				secondPanel = new JPanel();
				add(secondPanel, gbc);
				secondPanel.setBackground(Color.WHITE);
				secondPanel.setOpaque(false);
				secondPanel.setLayout(new GridBagLayout());
				GridBagConstraints secondPanelgbc = new GridBagConstraints();
				
				secondPanelgbc.anchor = GridBagConstraints.CENTER;
				
				createNewGrid();
				
				secondPanelgbc.insets = new Insets(5, 5, 5, 5);
				secondPanelgbc.gridx = 0;
				secondPanelgbc.gridy = 0;
				secondPanelgbc.gridwidth = 1;
				
				secondPanel.add(triesButton, secondPanelgbc);
				customizeButton(triesButton, false, Color.WHITE, Color.BLACK, BorderFactory.createLineBorder(Color.BLACK, 1));
				triesButton.setFont(new Font("Times New Roman", Font.PLAIN, 18));
				
				secondPanelgbc.gridx = 1;
				
				JButton newGameButton = new JButton(" New Game ");
				secondPanel.add(newGameButton, secondPanelgbc);
				customizeButton(newGameButton, true, new Color(30, 144, 255), Color.WHITE, BorderFactory.createLineBorder(Color.BLACK, 1));
				newGameButton.setFont(new Font("Times New Roman", Font.PLAIN, 18));
				
				newGameButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(timer.isRunning()) {
							timer.stop();
							stopTime = true;
						}
						
						indices.clear();
						playerOrderedList.clear();
						completeOrderedList.clear();
						
						for(int i = 0; i < buttons.length; i++) {
							for(int j = 0; j < buttons[0].length; j++) {
								firstPanel.remove(buttons[i][j]);
							}
						}
						
						unorganizedNums = new int[unorganizedNums.length][unorganizedNums[0].length];
						buttons = new JButton[buttons.length][buttons[0].length];
						numsDisplayed = new boolean[numsDisplayed.length][numsDisplayed[0].length];
						
						createNewGrid();
						
						gameOver = false;
						
						tries = 3;
						triesButton.setText(" Tries Left: " + tries + " ");
						customizeButton(triesButton, false, Color.WHITE, Color.WHITE, BorderFactory.createLineBorder(Color.BLACK, 1));
						
						stopTime = false;
						timer.restart();
					}
				});
				
				secondPanelgbc.gridx = 0;
				secondPanelgbc.gridy = 1;
				secondPanelgbc.gridwidth = 2;
				
				JButton backButton = new JButton(" Back ");
				customizeButton(backButton, true, new Color(255, 102, 0), Color.WHITE, BorderFactory.createLineBorder(Color.BLACK, 1));
				backButton.setFont(new Font("Times New Roman", Font.PLAIN, 18));
				
				secondPanel.add(backButton, secondPanelgbc);
				
				backButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(timer.isRunning()) {
							timer.stop();
							stopTime = true;
						}
						
						gameOver = true;
						tries = 0;
						
						for(int i = 0; i < buttons.length; i++) {
							for(int j = 0; j < buttons[0].length; j++) {
								remove(buttons[i][j]);
							}
						}
						
						indices.clear();
						completeOrderedList.clear();
						playerOrderedList.clear();
						
						remove(triesButton);
						remove(newGameButton);
						remove(backButton);
						remove(firstPanel);
						remove(secondPanel);
						
						goToMainPage();
						
						repaint();
					}
				});
				
				stopTime = false;
				
				timer.setInitialDelay(1000);
				timer.restart();
			}
		});
		
		triesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tries--;
				triesButton.setText(" Tries Left: " + tries + " ");
				value = completeOrderedList.get(completeOrderedList.indexOf(currentValue));
				for(int i = 0; i < unorganizedNums.length; i++) {
					for(int j = 0; j < unorganizedNums[0].length; j++) {
						if(numsDisplayed[i][j] && unorganizedNums[i][j] == value) {
							rowOfShownValue = i;
							colOfShownValue = j;
						}
						if(buttons[i][j].isEnabled()) {
							customizeButton(buttons[i][j], false, new Color(0, 128, 128), Color.BLACK, null);
						}
					}
				}
				stopTime = false;
				timer.restart();
				
				customizeButton(triesButton, false, Color.WHITE, Color.BLACK, BorderFactory.createLineBorder(Color.BLACK, 1));
			}
		});
		
		customizeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeMainPage();
				
				setBackground(Color.WHITE);
				
				gbc.anchor = GridBagConstraints.CENTER;
				gbc.weightx = 0;
				gbc.weighty = 0;
				gbc.insets = new Insets(0, 15, 15, 15);
				gbc.gridwidth = 2;
				gbc.gridx = 0;
				gbc.gridy = 0;
				
				JButton backButton = new JButton(" Back ");
				customizeButton(backButton, true, new Color(255, 102, 0), Color.WHITE, BorderFactory.createLineBorder(Color.BLACK, 1));
				backButton.setFont(new Font("Times New Roman", Font.PLAIN, 18));
				
				add(backButton, gbc);
				
				gbc.gridwidth = 1;
				gbc.insets = new Insets(10, 0, 0, 0);
				gbc.gridy = 1;
				
				String[] gridSizes = {"2 by 2", "3 by 3", "4 by 4", "5 by 5", "6 by 6"};
				JComboBox<String> gridSizesList = new JComboBox<String>(gridSizes);
				
				JComboBox<Integer> totalValuesComboBox = new JComboBox<Integer>();
				
				JLabel gridSizeLabel = new JLabel("Grid Size: ", SwingConstants.CENTER);
				add(gridSizeLabel, gbc);
				gridSizeLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
				
				for(int i = 0; i < gridSizes.length; i++) {
					String selectedGridSize = gridSizesList.getItemAt(i);
					int gridSizeNum = Integer.parseInt(selectedGridSize.substring(0, 1));
					if(unorganizedNums.length == gridSizeNum) {
						gridSizesList.setSelectedIndex(i);
						break;
					}
				}
				
				gbc.gridx = 1;
				
				add(gridSizesList, gbc);
				gridSizesList.setBackground(Color.WHITE);
				gridSizesList.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
				gridSizesList.setFont(new Font("Times New Roman", Font.PLAIN, 15));
				
				gridSizesList.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(e.getSource() == gridSizesList) {
							String selectedGridSize = gridSizesList.getSelectedItem().toString();
							int gridSizeNum = Integer.parseInt(selectedGridSize.substring(0, 1));
							unorganizedNums = new int[gridSizeNum][gridSizeNum];
							buttons = new JButton[unorganizedNums.length][unorganizedNums[0].length];
							numsDisplayed = new boolean[unorganizedNums.length][unorganizedNums[0].length];
							totalValuesComboBox.removeAll();
							for(int i = 3; i < unorganizedNums.length * unorganizedNums[0].length; i++) {
								totalValuesComboBox.addItem(i + 1);
							}
						}
					}
				});
				
				gbc.gridx = 0;
				gbc.gridy = 2;
				
				JLabel totalValuesLabel = new JLabel("Total Values: ", SwingConstants.CENTER);
				add(totalValuesLabel, gbc);
				totalValuesLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
				
				for(int i = 3; i < unorganizedNums.length * unorganizedNums[0].length; i++) {
					totalValuesComboBox.addItem(i + 1);
					if((Integer) totalValuesComboBox.getItemAt(totalValuesComboBox.getItemCount() - 1) == totalNums) {
						totalValuesComboBox.setSelectedIndex(totalValuesComboBox.getItemCount() - 1);
					}
				}
				
				gbc.gridx = 1;
				
				add(totalValuesComboBox, gbc);
				totalValuesComboBox.setBackground(Color.WHITE);
				totalValuesComboBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
				totalValuesComboBox.setFont(new Font("Times New Roman", Font.PLAIN, 15));
				
				totalValuesComboBox.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(e.getSource() == totalValuesComboBox) {
							totalNums = (Integer) totalValuesComboBox.getSelectedItem();
						}
					}
				});
				
				backButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						remove(backButton);
						remove(gridSizeLabel);
						remove(gridSizesList);
						remove(totalValuesLabel);
						remove(totalValuesComboBox);
						
						goToMainPage();
						
						repaint();
					}
				});
				
				revalidate();
				repaint();
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String nums = buttons[rowOfShownValue][colOfShownValue].getText();
		String underlines = "";
		for(int k = 0; k < nums.length(); k++) {
			underlines += "_";
		}
		buttons[rowOfShownValue][colOfShownValue].setText(underlines);
		buttons[rowOfShownValue][colOfShownValue].setBackground(new Color(0, 128, 128));
		
		if(stopTime) {
			timer.stop();
			for(int i = 0; i < buttons.length; i++) {
				for(int j = 0; j < buttons[0].length; j++) {
					if(numsDisplayed[i][j] && playerOrderedList.contains(unorganizedNums[i][j]) == false) { 
						customizeButton(buttons[i][j], true, new Color(0, 128, 128), Color.WHITE, null);
					}
				}
			}
			if(tries > 0) {
				customizeButton(triesButton, true, new Color(0, 128, 0), Color.WHITE, BorderFactory.createLineBorder(Color.BLACK, 1));
			}
		}
		
		if(timer.isRunning()) {
			for(int i = 0; i < unorganizedNums.length; i++) {
				for(int j = 0; j < unorganizedNums[0].length; j++) {
					if(numsDisplayed[i][j] && unorganizedNums[i][j] == value && playerOrderedList.contains(unorganizedNums[i][j]) == false) {
						buttons[i][j].setBackground(new Color(255, 255, 77));
						buttons[i][j].setText(unorganizedNums[i][j] + "");
						rowOfShownValue = i;
						colOfShownValue = j;
						break;
					}
				}
			}
		}
		
		if(completeOrderedList.indexOf(value) + 1 == completeOrderedList.size()) {
			stopTime = true;
		} else {
			value = completeOrderedList.get(completeOrderedList.indexOf(value) + 1);
		}
		
		repaint();
	}
	
	public void customizeButton(JButton button, boolean enable, Color buttonColor, Color textColor, Border border) {
		button.setEnabled(enable);
		button.setBorder(border);
		button.setBackground(buttonColor);
		if(enable) {
			button.setForeground(textColor);
		}
		button.setFocusable(false);
		button.setContentAreaFilled(false);
		button.setOpaque(true);
	}
	
	public void goToMainPage() {
		setOpaque(true);
		setBackground(new Color(30, 144, 255));
		
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.CENTER;
		gbc.insets = new Insets(40, 0, 0, 0);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;
		
		add(gameTitleLabel, gbc);
		String gameTitle = "<html><u>Memory Improver</u></html>";
		gameTitleLabel.setText(gameTitle);
		gameTitleLabel.setFont(new Font("Times New Roman", Font.PLAIN, 25));
		gameTitleLabel.setForeground(Color.WHITE);
		
		gbc.gridy = 1;
		
		add(playButton, gbc);
		customizeButton(playButton, true, new Color(65, 105, 225), Color.WHITE, BorderFactory.createLineBorder(Color.WHITE, 1));
		playButton.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		
		
		gbc.insets = new Insets(10, 0, 0, 0);
		gbc.gridy = 2;
		
		add(customizeButton, gbc);
		customizeButton(customizeButton, true, new Color(65, 105, 225), Color.WHITE, BorderFactory.createLineBorder(Color.WHITE, 1));
		customizeButton.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		
		repaint();
	}
	
	public void removeMainPage() {
		remove(gameTitleLabel);
		remove(playButton);
		remove(customizeButton);
	}
	
	public void createNewGrid() {
		for(int i = 0; i < unorganizedNums.length; i++) {
			for(int j = 0; j < unorganizedNums[0].length; j++) {
				indices.add(new int[] {i, j});
				numsDisplayed[i][j] = false;
				buttons[i][j] = new JButton();
			}
		}
		
		int num = (int)(Math.random() * 1001);
		for(int i = 0; i < totalNums; i++) {
			completeOrderedList.add(num);
			playerOrderedList.add(num);
			num += (int)(Math.random() * 5) + 1;
		}
		
		value = completeOrderedList.get(0);
		currentValue = value;
		
		while(playerOrderedList.isEmpty() == false) {
				int randIndex_indices = (int)(Math.random() * indices.size());
				int[] randIndices = indices.get(randIndex_indices);
				indices.remove(randIndex_indices);
				int randIndex_duplicateList = (int)(Math.random() * playerOrderedList.size());
				
				unorganizedNums[randIndices[0]][randIndices[1]] = playerOrderedList.get(randIndex_duplicateList);
				numsDisplayed[randIndices[0]][randIndices[1]] = true;
				
				playerOrderedList.remove(randIndex_duplicateList);
				
				if(unorganizedNums[randIndices[0]][randIndices[1]] == value) {
					rowOfShownValue = randIndices[0];
					colOfShownValue = randIndices[1];
				}
				
				String nums = unorganizedNums[randIndices[0]][randIndices[1]] + "";
				String underlines = "";
				
				for(int i = 0; i < nums.length(); i++) {
					underlines += "_";
				}
				
				buttons[randIndices[0]][randIndices[1]].setText(underlines);
		}
		
		for(int i = 0; i < buttons.length; i++) {
			for(int j = 0; j < buttons[0].length; j++) {
				int i2 = i;
				int j2 = j;
				
				firstPanel.add(buttons[i][j]);
				if(numsDisplayed[i][j]) {
					customizeButton(buttons[i][j], false, new Color(0, 128, 128), Color.BLACK, null);
					buttons[i][j].setFont(new Font("Times New Roman", Font.PLAIN, 16));
				} else {
					customizeButton(buttons[i][j], false, new Color(230, 230, 230), Color.BLACK, null);
					buttons[i][j].setText("");
				}
				
				buttons[i][j].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(gameOver == false) {
							if(unorganizedNums[i2][j2] == currentValue) {
								playerOrderedList.add(currentValue);
								customizeButton(buttons[i2][j2], false, new Color(77, 255, 77), Color.BLACK, null);
								buttons[i2][j2].setText(unorganizedNums[i2][j2] + "");
								
								if(completeOrderedList.indexOf(currentValue) + 1 < completeOrderedList.size()) {
									currentValue = completeOrderedList.get(completeOrderedList.indexOf(currentValue) + 1);
								} else {
									customizeButton(triesButton, false, Color.WHITE, Color.BLACK, BorderFactory.createLineBorder(Color.BLACK, 1));
									
									repaint();
								}
							} else {
								gameOver = true;
								customizeButton(triesButton, false, Color.WHITE, Color.BLACK, BorderFactory.createLineBorder(Color.BLACK, 1));
								for(int i = 0; i < buttons.length; i++) {
									for(int j = 0; j < buttons[0].length; j++) {
										if(numsDisplayed[i][j] && buttons[i][j].isEnabled()) {
											customizeButton(buttons[i][j], false, new Color(255, 77, 77), Color.BLACK, null);
											buttons[i][j].setText(unorganizedNums[i][j] + "");
										}
									}
								}
							}
						}
					}
				});
			}
		}
	}
}
