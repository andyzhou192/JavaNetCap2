package com.view.script.editor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.fife.ui.rsyntaxtextarea.ErrorStrip;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.RTextScrollPane;

import com.common.Constants;
import com.common.util.LogUtil;
import com.view.util.ViewDataHandler;

@SuppressWarnings("serial")
public class CodeEditPane extends JRootPane implements HyperlinkListener, SyntaxConstants {
	
	private Class<?> cl = CodeEditPane.class;

	private JFrame parent;
	private RTextScrollPane scrollPane;
	private RSyntaxTextArea textArea;
	
	public CodeEditPane(JFrame parent, String file) {
		this.parent = parent;
		this.textArea = createTextArea();
		this.textArea.setSyntaxEditingStyle(SYNTAX_STYLE_JAVA);
		if(null != file)
			setText(file);
		this.scrollPane = new RTextScrollPane(textArea, true);
		Gutter gutter = scrollPane.getGutter();
		gutter.setBookmarkingEnabled(true);
		gutter.setBookmarkIcon(new ImageIcon(Constants.BOOKMARK_ICON)); // "/img/bookmark.png"
		this.getContentPane().add(scrollPane);
		ErrorStrip errorStrip = new ErrorStrip(textArea);
		this.getContentPane().add(errorStrip, BorderLayout.LINE_END);
		this.setJMenuBar(createMenuBar());
	}

	private void addSyntaxItem(String name, String style,
			ButtonGroup bg, JMenu menu) {
		JRadioButtonMenuItem item = new JRadioButtonMenuItem(
				new ChangeSyntaxStyleAction(name, style));
		bg.add(item);
		menu.add(item);
	}


	private void addThemeItem(String name, String themeXml, ButtonGroup bg,
			JMenu menu) {
		JRadioButtonMenuItem item = new JRadioButtonMenuItem(
				new ThemeAction(name, themeXml));
		bg.add(item);
		menu.add(item);
	}
	
	private void addFileItem(JFrame frame, String name, JMenu menu, URL iconUrl) {
		JMenuItem item = new JMenuItem(new SaveFileAction(frame, name, iconUrl));
		menu.add(item);
	}


	private JMenuBar createMenuBar() {
		JMenuBar mb = new JMenuBar();

		JMenu menu = new JMenu("File");
		addFileItem(parent, "Save", menu, Constants.SAVE_ICON);
		addFileItem(parent, "SaveAs", menu, Constants.SAVEAS_ICON);
		mb.add(menu);
		
		menu = new JMenu("Language");
		ButtonGroup bg = new ButtonGroup();
		addSyntaxItem("C", SYNTAX_STYLE_C, bg, menu);
		addSyntaxItem("Java", SYNTAX_STYLE_JAVA, bg, menu);
		addSyntaxItem("Perl", SYNTAX_STYLE_PERL, bg, menu);
		addSyntaxItem("Ruby", SYNTAX_STYLE_RUBY, bg, menu);
		addSyntaxItem("SQL", SYNTAX_STYLE_SQL, bg, menu);
		addSyntaxItem("XML", SYNTAX_STYLE_XML, bg, menu);
		menu.getItem(1).setSelected(true);
		mb.add(menu);

		menu = new JMenu("View");
		JCheckBoxMenuItem cbItem = new JCheckBoxMenuItem(new CodeFoldingAction());
		cbItem.setSelected(true);
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new ViewLineHighlightAction());
		cbItem.setSelected(true);
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new ViewLineNumbersAction());
		cbItem.setSelected(true);
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new AnimateBracketMatchingAction());
		cbItem.setSelected(true);
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new BookmarksAction());
		cbItem.setSelected(true);
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new WordWrapAction());
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new ToggleAntiAliasingAction());
		cbItem.setSelected(true);
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new MarkOccurrencesAction());
		cbItem.setSelected(true);
		menu.add(cbItem);
		cbItem = new JCheckBoxMenuItem(new TabLinesAction());
		menu.add(cbItem);
		mb.add(menu);

		bg = new ButtonGroup();
		menu = new JMenu("Themes");
		addThemeItem("Default", "/theme/default.xml", bg, menu); // "theme/default.xml"
		addThemeItem("Default (Alternate)", "/theme/default-alt.xml", bg, menu); // "theme/default-alt.xml"
		addThemeItem("Dark", "/theme/dark.xml", bg, menu); // "theme/dark.xml"
		addThemeItem("Eclipse", "/theme/eclipse.xml", bg, menu); // "theme/eclipse.xml"
		addThemeItem("IDEA", "/theme/idea.xml", bg, menu); // "theme/idea.xml"
		addThemeItem("Visual Studio", "/theme/vs.xml", bg, menu); // "theme/vs.xml"
		mb.add(menu);

		menu = new JMenu("Help");
		JMenuItem item = new JMenuItem(new AboutAction(Constants.ABOUT_ICON));
		menu.add(item);
		mb.add(menu);

		return mb;

	}


	/**
	 * Creates the text area for this application.
	 *
	 * @return The text area.
	 */
	private RSyntaxTextArea createTextArea() {
		RSyntaxTextArea textArea = new RSyntaxTextArea(25, 70);
		textArea.setTabSize(3);
		textArea.setCaretPosition(0);
		textArea.addHyperlinkListener(this);
		textArea.requestFocusInWindow();
		textArea.setMarkOccurrences(true);
		textArea.setCodeFoldingEnabled(true);
		textArea.setClearWhitespaceLinesEnabled(false);
		return textArea;
	}


	/**
	 * Focuses the text area.
	 */
	void focusTextArea() {
		textArea.requestFocusInWindow();
	}


	/**
	 * Called when a hyperlink is clicked in the text area.
	 *
	 * @param e The event.
	 */
	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType()==HyperlinkEvent.EventType.ACTIVATED) {
			URL url = e.getURL();
			if (url==null) {
				UIManager.getLookAndFeel().provideErrorFeedback(null);
			}
			else {
				JOptionPane.showMessageDialog(this,
									"URL clicked:\n" + url.toString());
			}
		}
	}
	
	/**
	 * Sets the content in the text area to that in the specified resource.
	 *
	 * @param resource The resource to load.
	 */
	public void setText(String resource) {
		BufferedReader r = null;
		try {
			InputStream is = new FileInputStream(new File(resource));
			r = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			textArea.read(r, null);
			r.close();
			textArea.setCaretPosition(0);
			textArea.discardAllEdits();
		} catch (RuntimeException re) {
			LogUtil.err(cl, re); // FindBugs
		} catch (Exception e) { // Never happens
			textArea.setText("Type here to see syntax highlighting");
		}
	}


	private class AboutAction extends AbstractAction {
		public AboutAction(URL iconUrl) {
			putValue(NAME, "About RSyntaxTextArea...");
			putValue(SMALL_ICON, new ImageIcon(iconUrl));
		}

		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(CodeEditPane.this,
					"<html><b>RSyntaxTextArea</b> - A Swing syntax highlighting text component" +
					"<br>Version 2.0.7" +
					"<br>Licensed under a modified BSD license",
					"About RSyntaxTextArea",
					JOptionPane.INFORMATION_MESSAGE);
		}

	}


	private class AnimateBracketMatchingAction extends AbstractAction {
		public AnimateBracketMatchingAction() {
			putValue(NAME, "Animate Bracket Matching");
		}

		public void actionPerformed(ActionEvent e) {
			textArea.setAnimateBracketMatching(
						!textArea.getAnimateBracketMatching());
		}

	}


	private class BookmarksAction extends AbstractAction {
		public BookmarksAction() {
			putValue(NAME, "Bookmarks");
		}

		public void actionPerformed(ActionEvent e) {
			scrollPane.setIconRowHeaderEnabled(
							!scrollPane.isIconRowHeaderEnabled());
		}

	}

	private class SaveFileAction extends AbstractAction {
		private JFrame frame;
		public SaveFileAction(JFrame frame, String name, URL iconUrl){
			putValue(NAME, name);
			putValue(SMALL_ICON, new ImageIcon(iconUrl));
			this.frame = frame;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			final String defFileName = "TestScript.java";
			switch (((JMenuItem)e.getSource()).getText()){
			case "Save":
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						ViewDataHandler.saveDataToFile(frame, frame.getTitle(), defFileName, textArea.getText());
					}
				});
				break;
			case "SaveAs":
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						ViewDataHandler.saveDataToFile(frame, null, defFileName, textArea.getText());
					}
				});
				break;
			default:
				break;
			}
		}
	}

	private class ChangeSyntaxStyleAction extends AbstractAction {
		private String style;

		public ChangeSyntaxStyleAction(String name, String style) {
			putValue(NAME, name);
			this.style = style;
		}

		public void actionPerformed(ActionEvent e) {
			textArea.setCaretPosition(0);
			textArea.setSyntaxEditingStyle(style);
		}

	}


	private class CodeFoldingAction extends AbstractAction {
		public CodeFoldingAction() {
			putValue(NAME, "Code Folding");
		}

		public void actionPerformed(ActionEvent e) {
			textArea.setCodeFoldingEnabled(!textArea.isCodeFoldingEnabled());
		}

	}


	private class MarkOccurrencesAction extends AbstractAction {
		public MarkOccurrencesAction() {
			putValue(NAME, "Mark Occurrences");
		}

		public void actionPerformed(ActionEvent e) {
			textArea.setMarkOccurrences(!textArea.getMarkOccurrences());
		}

	}


	private class TabLinesAction extends AbstractAction {
		private boolean selected;

		public TabLinesAction() {
			putValue(NAME, "Tab Lines");
		}

		public void actionPerformed(ActionEvent e) {
			selected = !selected;
			textArea.setPaintTabLines(selected);
		}

	}


	private class ThemeAction extends AbstractAction {
		private String xml;

		public ThemeAction(String name, String xml) {
			putValue(NAME, name);
			this.xml = xml;
		}

		public void actionPerformed(ActionEvent e) {
			try {
				InputStream in = ThemeAction.class.getResourceAsStream(xml);
//				InputStream in = new FileInputStream(new File(xml));
				Theme theme = Theme.load(in);
				theme.apply(textArea);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

	}


	private class ToggleAntiAliasingAction extends AbstractAction {
		public ToggleAntiAliasingAction() {
			putValue(NAME, "Anti-Aliasing");
		}

		public void actionPerformed(ActionEvent e) {
			textArea.setAntiAliasingEnabled(!textArea.getAntiAliasingEnabled());
		}

	}


	private class ViewLineHighlightAction extends AbstractAction {
		public ViewLineHighlightAction() {
			putValue(NAME, "Current Line Highlight");
		}

		public void actionPerformed(ActionEvent e) {
			textArea.setHighlightCurrentLine(
					!textArea.getHighlightCurrentLine());
		}

	}


	private class ViewLineNumbersAction extends AbstractAction {
		public ViewLineNumbersAction() {
			putValue(NAME, "Line Numbers");
		}

		public void actionPerformed(ActionEvent e) {
			scrollPane.setLineNumbersEnabled(
					!scrollPane.getLineNumbersEnabled());
		}

	}


	private class WordWrapAction extends AbstractAction {
		public WordWrapAction() {
			putValue(NAME, "Word Wrap");
		}

		public void actionPerformed(ActionEvent e) {
			textArea.setLineWrap(!textArea.getLineWrap());
		}

	}

}
