import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class QuizCardPlayer {
	private JFrame frame;
	private JPanel panel;
	private JTextArea display;
	//private JTextArea answer;
	private JButton button;
	private QuizCard currentCard;
	private ArrayList<QuizCard> cardList;
	private int currentCardIndex;
	private boolean isShowAnswer;
	
	public static void main(String[] args) {
		QuizCardPlayer player = new QuizCardPlayer();
		player.go();
	}
	
	public void go() {
		frame = new JFrame("卡片阅读器");
		panel = new JPanel();
		
		display = new JTextArea(10, 20);
		display.setLineWrap(true);
		display.setEditable(false);
		
		JScrollPane qScroller = new JScrollPane(display);	//add scroll to text area
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		button = new JButton("显示问题");
		button.addActionListener(new nextCardListener());
		
		panel.add(qScroller);
		panel.add(button);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("文件");
		JMenuItem loadMenuItem = new JMenuItem("加载已有的卡片…");
		loadMenuItem.addActionListener(new openMenuListener());
		fileMenu.add(loadMenuItem);
		menuBar.add(fileMenu);
		frame.setJMenuBar(menuBar);		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(BorderLayout.CENTER, panel);
		frame.setSize(330, 300);
		frame.setVisible(true);
	}
	
	public void loadFile(File file) {
		cardList = new ArrayList<QuizCard>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			while((line = reader.readLine()) != null) { //read a line of text
				generateCard(line);
			}
			reader.close();
		} catch(Exception ex) {
			System.out.println("找不到文件 or 用户点击了取消按钮");
			//ex.printStackTrace();
		}		
	}
	
	public void generateCard(String lineToParse) {
		String[] result = lineToParse.split("/");
		QuizCard card = new QuizCard(result[0], result[1]);
		cardList.add(card);
		System.out.println("已生成一张卡片！");
	}
	
	public void showNextCard() {
		currentCard = cardList.get(currentCardIndex);
		currentCardIndex++;
		display.setText(currentCard.getQuestion());
		button.setText("显示答案");
		isShowAnswer = true;
	}
	
	private class nextCardListener implements ActionListener {	//判断文本框里的是问题还是答案，按钮按下后在答案和下一个问题之间切换
		public void actionPerformed(ActionEvent e) {
			if(isShowAnswer) {
				display.setText(currentCard.getAnswer());
				button.setText("下一个问题");
				isShowAnswer = false;
			} else {
				if(currentCardIndex < cardList.size()) {
					showNextCard();
				} else {
					System.out.println("已经是最后一张卡片了！");
					button.setEnabled(false);
				}
			}
		}		
	}

	private class openMenuListener implements ActionListener {	//打开并加载储存有卡片的文件
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileOpen = new JFileChooser();
			fileOpen.showOpenDialog(frame);	//弹出系统对话框让用户选择需要加载的文件
			loadFile(fileOpen.getSelectedFile());
		}
	}
	
}
