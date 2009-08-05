package net.sourceforge.xmlfit.fit.stepper.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.text.StyleConstants.ColorConstants;

import fit.Parse;

public class DebuggerFrame extends JFrame
{

  private static final String TITEL_DEBUGGER_FRAME = "FIT Action Fixture Debugger";

  private int maxCellCount = 0;

  private JButton nextStepButton = new JButton("step");

  private JButton runButtonp = new JButton("run");

  private JPanel buttonPanel = new JPanel();

  private HashMap<Parse, Integer> parseToCount = new HashMap<Parse, Integer>();

  private JTable tableView;

  private boolean isStepMode = false;

  private static Boolean lock = false;

  public DebuggerFrame()
  {
    super(TITEL_DEBUGGER_FRAME);
    initButtons();
  }

  public void initTabeles(Parse tables)
  {
    int count = 1;
    Parse table = tables;
    ArrayList<List<String>> rowList = new ArrayList<List<String>>();
    while (table != null)
    {
      Parse rows = table.parts;
      count = doRows(count, rowList, rows);
      count++;
      rowList.add(new ArrayList<String>());
      table = table.more;
    }
    initTableView(rowList);
    pack();
    setVisible(true);
    warten();
  }

  private int doRows(int count, ArrayList<List<String>> rowList, Parse rows)
  {
    while (rows != null)
    {
      parseToCount.put(rows.parts, count++);
      ArrayList<String> cellList = doCells(rows.parts);
      rowList.add(cellList);
      rows = rows.more;
    }
    return count;
  }

  private ArrayList<String> doCells(Parse cells)
  {
    ArrayList<String> cellList = new ArrayList<String>();
    int tmpCellCount = 0;
    while (cells != null)
    {
      cellList.add(cells.text());
      cells = cells.more;
      tmpCellCount++;
    }
    setMaxCellCount(tmpCellCount);
    return cellList;
  }

  private void setMaxCellCount(int count)
  {
    if (count > maxCellCount)
    {
      maxCellCount = count;
    }
  }

  private void initTableView(ArrayList<List<String>> rowList)
  {
    tableView = new JTable(new DebuggerViewModel(rowList, maxCellCount));
    tableView.getColumnModel().getColumn(0).setWidth(20);
    tableView.getColumnModel().getColumn(0).setPreferredWidth(20);
    tableView.getColumnModel().getColumn(0).setMaxWidth(20);
    tableView.getColumnModel().getColumn(0).setMinWidth(20);
    getContentPane().add(new JScrollPane(tableView), BorderLayout.CENTER);
  }

  private void initButtons()
  {
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);

    buttonPanel.add(nextStepButton);
    buttonPanel.add(runButtonp);

    nextStepButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        isStepMode = true;
        fortsetzen();
      }
    });

    runButtonp.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        fortsetzen();
      }
    });
  }

  public void doRow(Parse parse)
  {
    if (isDebuggerEnable(parse))
    {
      isStepMode = false;
      Integer row = parseToCount.get(parse);
      setActualRowSelected(row);
      scrollToRow(row);
      setVisible(true);
      warten();
    }
  }

  private void warten()
  {
    try
    {
      synchronized (lock)
      {
        tableView.setForeground(Color.BLACK);
        lock.wait();
        tableView.setForeground(Color.LIGHT_GRAY);
      }
    }
    catch (InterruptedException e1)
    {
    }
  }

  private void fortsetzen()
  {
    synchronized (lock)
    {
      lock.notifyAll();
    }
  }

  private void scrollToRow(Integer row)
  {
    Rectangle rect = tableView.getCellRect(row - 1, 0, true);
    tableView.scrollRectToVisible(rect);
  }

  private void setActualRowSelected(Integer row)
  {
    ListSelectionModel selectionModel = tableView.getSelectionModel();
    selectionModel.setSelectionInterval(row - 1, row - 1);
  }

  private boolean isDebuggerEnable(Parse parse)
  {
    return isStepMode || isBreakPoint(parse);
  }

  private boolean isBreakPoint(Parse parse)
  {
    Integer count = parseToCount.get(parse);
    Object object = tableView.getModel().getValueAt(count - 1, 0);
    if (object instanceof Boolean)
    {
      Boolean result = (Boolean) object;
      return result;
    }
    return false;
  }
}
