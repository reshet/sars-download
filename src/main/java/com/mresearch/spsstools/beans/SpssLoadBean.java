package com.mresearch.spsstools.beans;

import com.mresearch.spsstools.cup.AnketLoader;
import com.mresearch.spsstools.cup.Setting_AssocPair;
import com.pmstation.spss.MissingValue;
import com.pmstation.spss.SPSSWriter;
import com.pmstation.spss.ValueLabels;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.DefaultTableModel;

public class SpssLoadBean
{
  private DefaultTableModel _Model;
  private DefaultTableModel _M_Model;

  public byte[] loadFromMSS(String anketId)
  {
    Setting_AssocPair<DefaultTableModel, DefaultTableModel> models = new AnketLoader(Integer.parseInt(anketId)).performAction();
    fillModels((DefaultTableModel)models.getLeft(), (DefaultTableModel)models.getRight());
    byte[] file = produceSPSSfile();
    return file;
  }

  public static int findColoumn(DefaultTableModel model, String col_name)
  {
    for (int i = 0; i < model.getColumnCount(); i++) {
      if (model.getColumnName(i).equals(col_name)) {
        return i;
      }
    }
    return -1;
  }

  private void fillModels(DefaultTableModel model, DefaultTableModel model_m)
  {
    this._Model = model;
    this._M_Model = model_m;
  }

  private byte[] produceSPSSfile()
  {
    byte[] arr = new byte[10];
    try
    {
      OutputStream str = new ByteArrayOutputStream();

      SPSSWriter writer = new SPSSWriter(str, "windows-1251");

      writer.setCalculateNumberOfCases(false);
      writer.addDictionarySection(-1);
      int col_type = findColoumn(this._M_Model, "Type");
      int col_format = findColoumn(this._M_Model, "Format");
      int col_name = findColoumn(this._M_Model, "VarName");
      int col_width = findColoumn(this._M_Model, "Width");
      int col_decimals = findColoumn(this._M_Model, "Decimals");
      int col_label = findColoumn(this._M_Model, "Label");

      int col_mv_val = findColoumn(this._M_Model, "NMissingValue1");

      int col_columns = findColoumn(this._M_Model, "WriteWidth");

      int col_align = findColoumn(this._M_Model, "Alignment");

      int col_measure = findColoumn(this._M_Model, "MeasLevel");

      int col_vlabel = findColoumn(this._M_Model, "ValueLabelTableName");

      Map<Integer, Integer> map_types = new HashMap();
      Map<Integer, Integer> map_types_hacker = new HashMap();
      for (int i = 0; i < this._M_Model.getRowCount(); i++)
      {
        MissingValue mv = new MissingValue();
        mv.setOneDescreteMissingValue(Double.parseDouble((String)this._M_Model.getValueAt(i, col_mv_val)));

        int type_c = Integer.parseInt((String)this._M_Model.getValueAt(i, col_format));

        map_types.put(Integer.valueOf(i), Integer.valueOf(type_c));

        System.out.println("type " + type_c + " code " + (String)this._M_Model.getValueAt(i, col_name) + " label " + (String)this._M_Model.getValueAt(i, col_label));
        if (type_c == 5) {
          writer.addNumericVar((String)this._M_Model.getValueAt(i, col_name), Integer.parseInt((String)this._M_Model.getValueAt(i, col_width)), Integer.parseInt((String)this._M_Model.getValueAt(i, col_decimals)), (String)this._M_Model.getValueAt(i, col_label), mv);
        } else {
          writer.addStringVar((String)this._M_Model.getValueAt(i, col_name), Integer.parseInt((String)this._M_Model.getValueAt(i, col_type)), (String)this._M_Model.getValueAt(i, col_label));
        }
      }
      for (int i = 0; i < this._M_Model.getRowCount(); i++)
      {
        ValueLabels labs = new ValueLabels();
        String str_label = (String)this._M_Model.getValueAt(i, col_vlabel);
        if (str_label != null)
        {
          String[] strs = str_label.split(";");
          for (String st : strs)
          {
            String[] s = st.split("=");
            if (s.length > 1)
            {
              s[0] = s[0].substring(1, s[0].length() - 1);
              s[1] = s[1].substring(1, s[1].length() - 1);
              double er = 0.0D;
              try
              {
                er = Double.parseDouble(s[0]);
                labs.putLabel(er, s[1]);
              }
              catch (NumberFormatException e) {}
            }
          }
        }
        if (((Integer)map_types.get(Integer.valueOf(i))).intValue() == 5) {
          writer.addValueLabels(i + 2, labs);
        }
      }
      writer.addDataSection();
      for (int i = 0; i < this._Model.getRowCount(); i++) {
        for (int j = 0; j < this._M_Model.getRowCount(); j++) {
          if (this._Model.getValueAt(i, j + 1) != null) {
            if (((Integer)map_types.get(Integer.valueOf(j))).intValue() == 5) {
              try
              {
                writer.addData(Double.valueOf(Double.parseDouble((String)this._Model.getValueAt(i, j + 1))));
              }
              catch (NumberFormatException e)
              {
                writer.addData(Double.valueOf(0.0D));
              }
            } else {
              writer.addData((String)this._Model.getValueAt(i, j + 1));
            }
          }
        }
      }
      writer.addFinishSection();

      str.close();
      return ((ByteArrayOutputStream)str).toByteArray();
    }
    catch (NullPointerException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return arr;
  }
}
