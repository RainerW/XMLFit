package net.sourceforge.xmlfit.fit.mojo.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

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

  private int maxCount;

  private int totalFail = 0;

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
    frame.setSize(300, 200);
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
    lastFails.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent e)
      {
        if (e.getClickCount() == 2)
        {
          int index = lastFails.locationToIndex(e.getPoint());
          ListModel dlm = lastFails.getModel();
          String item = (String) dlm.getElementAt(index);
          lastFails.ensureIndexIsVisible(index);

          item = item.substring(1, item.indexOf(" : "));
          item = item.trim();

          StringSelection ss = new StringSelection(item);
          Toolkit.getDefaultToolkit().getSystemClipboard()
              .setContents(ss, null);

          try
          {
            Runtime.getRuntime().exec("start " + item);
          }
          catch (IOException e1)
          {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
        }
      }
    });
  }

  protected void initFrame()
  {
    Container pane = frame.getContentPane();
    JPanel statusPanel = new JPanel(new GridLayout(5, 1));
    JPanel listPanel = new JPanel(new GridLayout(1, 1));

    pane.setLayout(new BorderLayout());
    statusPanel.add(right);
    statusPanel.add(wrong);
    statusPanel.add(irgnored);
    statusPanel.add(execptions);
    statusPanel.add(lastFile);

    listPanel.add(lastFails);
    listPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));

    pane.add(statusPanel, BorderLayout.NORTH);
    pane.add(listPanel, BorderLayout.CENTER);

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

  public void update(Counts totalCount, String fileName, Integer idx,
      Counts fileCount)
  {
    if (lastWrong != totalCount.wrong)
    {
      markFail(fileCount, fileName);
      lastWrong = totalCount.wrong;
    }
    if (lastIgnores != totalCount.ignores)
    {
      markFail(fileCount, fileName);
      lastIgnores = totalCount.ignores;
    }
    if (lastExceptions != totalCount.exceptions)
    {
      markFail(fileCount, fileName);
      lastExceptions = totalCount.exceptions;
    }
    update(totalCount);
    updateLastFile(fileName);

    if (idx != null)
    {
      frame.setTitle("Fit - GUI [" + idx + "/" + maxCount + "] fails : "
          + totalFail);
    }
  }

  private void markFail(Counts counts2, String fileName)
  {
    DefaultListModel dml = (DefaultListModel) lastFails.getModel();
    dml.addElement(" " + fileName + " : " + counts2);
    totalFail++;
  }

  public void setTestCount(int count)
  {
    maxCount = count;
  }

}
