package com.qy.cn;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileFilter;

public class Application2 extends JFrame implements Cloneable {
	public Application2() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(800, 700);
		this.setLayout(new BorderLayout());
		keyWords1 = new String[] { "那么", "还是", "sdf" };
		keyWords2 = new String[] { "所以", "而且", };
		input = new JTextArea();
		JPanel ip = new JPanel();
		ip.setLayout(new BorderLayout());
		ip.add(input, BorderLayout.CENTER);
		ip.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "输入文本"));
		output1 = new JTextArea();
		JPanel o1p = new JPanel();
		o1p.setLayout(new BorderLayout());
		o1p.add(output1, BorderLayout.CENTER);
		o1p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "以下为"));
		output2 = new JTextArea();
		JPanel o2p = new JPanel();
		o2p.setLayout(new BorderLayout());
		o2p.add(output2, BorderLayout.CENTER);
		o2p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "以下为"));
		JSplitPane split1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, o1p, o2p);
		split1.setDividerLocation(350);
		JSplitPane split2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, ip, split1);
		split2.setDividerLocation(300);
		this.add(split2, BorderLayout.CENTER);
		open = new JButton("导入");
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(".");
				chooser.setMultiSelectionEnabled(false);
				chooser.addChoosableFileFilter(new FileFilter() {
					@Override
					public boolean accept(File file) {
						if (file.isDirectory())
							return true;
						int length = file.getName().length();
						if (length < 5)
							return false;
						if (file.getName().substring(length - 4).equals(".txt"))
							return true;
						return false;
					}

					@Override
					public String getDescription() {
						return "文本文件";
					}
				});
				chooser.showOpenDialog(Application2.this);
				File file = chooser.getSelectedFile();
				if (file == null)
					return;
				try {
					Scanner sc = new Scanner(file);
					String text = "";
					while (sc.hasNextLine())
						text += sc.nextLine() + "\n";
					input.setText(text);
					String[] array = getSentences();
					output1.setText(getKeySentences(keyWords1, array));
					output2.setText(getKeySentences(keyWords2, array));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		save = new JButton("导出");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(".");
				chooser.setMultiSelectionEnabled(false);
				chooser.addChoosableFileFilter(new FileFilter() {
					@Override
					public boolean accept(File file) {
						if (file.isDirectory())
							return true;
						int length = file.getName().length();
						if (length < 5)
							return false;
						if (file.getName().substring(length - 4).equals(".txt"))
							return true;
						return false;
					}

					@Override
					public String getDescription() {
						return "文本文件";
					}
				});
				chooser.showSaveDialog(Application2.this);
				File file = chooser.getSelectedFile();
				if (file == null)
					return;
				try {
					PrintWriter pw = new PrintWriter(file);
					pw.print(output1.getText());
					pw.flush();
					pw.print(output2.getText());
					pw.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		JPanel buttonPane = new JPanel();
		buttonPane.add(open);
		buttonPane.add(save);
		this.add(buttonPane, BorderLayout.SOUTH);
	}

	public String[] getSentences() {
		ArrayList<String> set = new ArrayList<String>();
		int length = input.getText().length();
		for (int i = 0, last = 0; i < length; i++) {
			String s = String.valueOf(input.getText().charAt(i));
			if (s.equals("\n"))
				last = i + 1;
			if (s.equals(".") || s.equals(",") || s.equals("。") || s.equals("。") || s.equals("！") || s.equals("？")
					|| s.equals("?") || s.equals("!") || s.equals("，")) {
				set.add(input.getText().substring(last, i) + s);
				last = i + 1;
			}
		}
		return set.<String>toArray(new String[set.size()]);
	}

	public String getKeySentences(String[] key, String[] sentences) {
		String result = "";
		A: for (int i = 0; i < sentences.length; i++) {
			for (int k = 0; k < key.length; k++)
				if (sentences[i].contains(key[k].subSequence(0, key[k].length()))) {
					result += sentences[i] + "\n";
					continue A;
				}
		}
		return result;
	}

	private JTextArea input;
	private JTextArea output1;
	private JTextArea output2;
	private JButton open;
	private JButton save;
	private String[] keyWords1;
	private String[] keyWords2;

	public static void main(String... args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Application2().setVisible(true);
			}
		});
	}
}