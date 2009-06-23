package net.sourceforge.xmlfit.fit.stepper.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class DebuggerViewModel extends AbstractTableModel
{

  private Object[][] data;
  
  private int maxCellCount;
  
  public DebuggerViewModel(ArrayList<List<String>> tabel, int maxCellCount)
  {
    this.maxCellCount = maxCellCount;
    this.data = initData(tabel);
  }
  
  private Object[][] initData(ArrayList<List<String>> tabel)
  {
    Object[][] result = new Object[tabel.size()][maxCellCount+1];
    clean(result);
    for (int i = 0; i < tabel.size(); i++)
    {
      Object[] cellsObjects = result[i];
      List<String> cells = tabel.get(i);
      if(cells.size() > 0)
      {
        cellsObjects[0] = new Boolean(false);
      }
      for (int j = 0; j < cells.size(); j++)
      {
        cellsObjects[j+1] = cells.get(j);
      }
    }
    return result;
  }
  
  private void clean(Object[][] result)
  {
    for (Object[] objects : result)
    {
      for (Object object : objects)
      {
        object = "";
      }
    }
  }
  
  public Class getColumnClass(int c) {
     if(c == 0)
     {
       return Boolean.class;
     }
     return super.getColumnClass(c);
  }

  public boolean isCellEditable(int row, int col) {
      if (col == 0) {  
        Object object = data[row][col];
        if (object instanceof Boolean)
        {
          return true;
        }
      }
      return false;
  }
  
  public void setValueAt(Object value, int row, int col) {
      data[row][col] = value;
      fireTableCellUpdated(row, col);
  }
  
  public int getColumnCount()
  {
    return maxCellCount;
  }

  public int getRowCount()
  {
    return data.length;
  }

  public Object getValueAt(int rowIndex, int columnIndex)
  {
    return data[rowIndex][columnIndex];
  }

}
