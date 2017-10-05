import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

//用户写入卡片内容并以文本格式保存
public class QuizCardBuilder {
	private JFrame frame;
	private JPanel panel;
	private JTextArea question;
	private JTextArea answer;
	private JLabel questionLabel = new JLabel("问题:");
	private JLabel answerLabel = new JLabel("答案:");
	private JButton nextButton = new JButton("下一张");
	private ArrayList<QuizCard> cardList = new ArrayList<QuizCard>();
		
	public static void main(String[] args) {
		QuizCardBuilder builder = new QuizCardBuilder();
		builder.go();
	}
	
	public void go() {
		frame = new JFrame("卡片存储器");
		panel = new JPanel();
		
		question = new JTextArea(10, 20);
		question.setLineWrap(true);
		question.setWrapStyleWord(true);
		question.addKeyListener(new tabKeyListener());
		
		JScrollPane qScroller = new JScrollPane(question);	//add scroll to question text area
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		answer = new JTextArea(10, 20);
		answer.setLineWrap(true);
		answer.setWrapStyleWord(true);
		
		JScrollPane aScroller = new JScrollPane(answer);	//add scroll to answer text area
		aScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		aScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		panel.add(questionLabel);
		panel.add(qScroller);
		panel.add(answerLabel);
		panel.add(aScroller);
		panel.add(nextButton);
		nextButton.addActionListener(new nextCardListener());
		
		JMenuBar menuBar = new JMenuBar();	//菜单栏
		JMenu fileMenu = new JMenu("文件");
		JMenuItem newMenuItem = new JMenuItem("新建卡片");
		newMenuItem.addActionListener(new newMenuListener());
		JMenuItem saveMenuItem = new JMenuItem("保存卡片…");
		saveMenuItem.addActionListener(new saveMenuListener());
		fileMenu.add(newMenuItem);
		fileMenu.add(saveMenuItem);
		menuBar.add(fileMenu);
		frame.setJMenuBar(menuBar);		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(BorderLayout.CENTER, panel);
		frame.setSize(330, 500);
		frame.setVisible(true);
	}
	
	public void saveFile(File file) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file)); //将文件链接到缓冲区以提高效率
			for(QuizCard card : cardList) {
				writer.write(card.getQuestion() + "/");
				writer.write(card.getAnswer() + "\n");
			}
			writer.close();
		} catch(IOException ex) {
			System.out.println("无法读取卡片列表！");
			ex.printStackTrace();
		}
	}
	
	public void clearCard() {	//清空当前的文本框
		question.setText("");
		answer.setText("");
		question.requestFocus();
	}
	
	private class nextCardListener implements ActionListener { //将当前卡片加入cardList，之后清空文本框以便写入下一张卡片
		public void actionPerformed(ActionEvent e) {
			QuizCard card = new QuizCard(question.getText(), answer.getText());
			cardList.add(card);
			clearCard();
		}	
	}
	
	private class saveMenuListener implements ActionListener {	//保存cardList上所有元素（即所有的卡片）至一个可直接阅读的文件
		public void actionPerformed(ActionEvent e) {
			QuizCard card = new QuizCard(question.getText(), answer.getText());	//保存当前文本框里的卡片至cardList
			cardList.add(card);
			JFileChooser fileSave = new JFileChooser();
			try {
				fileSave.showSaveDialog(frame); //弹出系统对话框让用户存储文件
				saveFile(fileSave.getSelectedFile());
			} catch(NullPointerException ex) {	//若用户点击“取消”，则会抛出nullPointerException
				cardList.remove(card);
			}		
		}	
	}
	
	private class newMenuListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			cardList.clear(); //清空当前cardList上所有卡片，以便新建*下一组*卡片
			clearCard();
		}	
	}
	
	private class tabKeyListener implements KeyListener {
		public void keyTyped(KeyEvent e) {}

		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_TAB) {
				answer.requestFocus();
			}	
		}

		public void keyReleased(KeyEvent e) {}
		
	}
}
