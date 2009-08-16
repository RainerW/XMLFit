package fit.gui;

import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;

import fit.Counts;

public class FitGUI
{
  private JFrame frame;

  private JLabel right;

  private JLabel wrong;

  private JLabel irgnored;

  private JLabel execptions;

  private JTextField lastFile;

  private int lastWrong = 0;

  private int lastIgnores = 0;

  private int lastExceptions = 0;

  private JList lastFails;

  public static void main(String[] args)
  {
    new FitGUI();
  }

  public FitGUI()
  {
    frame = new JFrame("FIT - GUI");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    initControls();
    initFrame();

    frame.pack();
    frame.setAlwaysOnTop(true);
    frame.setVisible(true);
  }

  protected void initControls()
  {
    right = new JLabel(text("Right", 0));
    wrong = new JLabel(text("Wrong", 0));
    irgnored = new JLabel(text("Ignored", 0));
    execptions = new JLabel(text("Exceptions", 0));
    lastFile = new JTextField("-");
    lastFails = new JList(new DefaultListModel());
  }

  protected void initFrame()
  {
    Container pane = frame.getContentPane();
    pane.setLayout(new GridLayout(6, 1));
    pane.add(right);
    pane.add(wrong);
    pane.add(irgnored);
    pane.add(execptions);
    pane.add(lastFile);
    pane.add(lastFails);
  }

  public void update(Counts counts)
  {
    right.setText(text("Right", counts.right));
    wrong.setText(text("Wrong", counts.wrong));
    irgnored.setText(text("Ignored", counts.ignores));
    execptions.setText(text("Exceptions", counts.exceptions));
  }

  public void updateLastFile(String file)
  {
    lastFile.setText(file);
  }

  protected String text(String text, int count)
  {
    return text + " : " + count;
  }

  public void update(Counts counts2, String fileName)
  {
    if (lastWrong != counts2.wrong)
    {
      markFail(counts2, fileName);
      lastWrong=counts2.wrong;
    }
    if (lastIgnores  != counts2.ignores)
    {
      markFail(counts2, fileName);
      lastIgnores=counts2.ignores;
    }
    if (lastExceptions  != counts2.exceptions)
    {
      markFail(counts2, fileName);
      lastExceptions =counts2.exceptions;
    }
    update(counts2);
    updateLastFile(fileName);
  }

  private void markFail(Counts counts2, String fileName)
  {
   DefaultListModel dml=(DefaultListModel) lastFails.getModel();
   dml.addElement( " " + fileName + " : " + counts2 );
  }

}
