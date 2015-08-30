package com.mresearch.spsstools.cup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class MSS_RQ_TableFiller
{
  private MSS_RQ_XML xmlparser;
  private JTable table;
  private String RQ_STR;
  private String[] descriptor;
  private String[] xml_descr;
  private Class[] class_desc;
  private ArrayList<MSS_RQ_XML_Pattern> DATA;
  private DefaultTableModel model;

  public MSS_RQ_TableFiller(MSS_RQ_TableDescriptor desc, MSS_RQ_XMLtoTableDescriptor desc_xml)
  {
    this.xmlparser = new MSS_RQ_XML();
    this.descriptor = desc.getDescription();
    this.xml_descr = desc_xml.getDescription();
    this.class_desc = desc.getClassDescription();
    this.model = new DefaultTableModel(this.descriptor, 0);
  }

  MSS_RQ_TableFiller(MSS_RQ_TableDescriptor desc)
  {
    this.xmlparser = new MSS_RQ_XML();
    this.descriptor = desc.getDescription();
    this.xml_descr = desc.getDescription();
    this.model = new DefaultTableModel(this.descriptor, 0);
  }

  public void updateData(String RQ)
  {
    this.model = new DefaultTableModel(this.descriptor, 0);
    this.RQ_STR = RQ;
    this.DATA = this.xmlparser.parseXML(this.RQ_STR);
    int i = 1;
    for (MSS_RQ_XML_Pattern cortage : this.DATA)
    {
      Vector<String> row = new Vector(cortage.getAttributes().size() + 1);
      ArrayList<MSS_Pair> rower = new ArrayList(cortage.getAttributes().size());
      row.add(String.valueOf(i++));
      for (MSS_Pair str : cortage.getAttributes())
      {
        int pos = 0;
        for (int j = 0; j < this.xml_descr.length; j++) {
          if (this.xml_descr[j].compareTo(str.getName()) == 0)
          {
            pos = j; break;
          }
        }
        String val = str.getValue();

        String name = str.getName();
        rower.add(new MSS_Pair(String.valueOf(pos), val));
      }
      int posit = 1;
      int ower = 0;

      int skipped = 0;
      while (posit < this.xml_descr.length)
      {
        for (MSS_Pair elem : rower) {
          if (elem.getName().compareTo(String.valueOf(posit)) == 0)
          {
            row.add(elem.getValue());posit++;ower = 0;
          }
          else
          {
            ower++;
          }
        }
        if (ower + 1 >= rower.size())
        {
          ower = 0;
          row.add("");
          skipped++;
          posit++;
        }
      }
      this.model.addRow(CastRowElements(row));
    }
  }

  private Vector<Object> CastRowElements(Vector<String> row)
  {
    int posit = 0;
    Vector<Object> raw = new Vector(row.size());
    while ((posit < row.size()) && (posit < this.class_desc.length))
    {
      String element = (String)row.get(posit);
      Object outer = element;
      if (this.class_desc[posit].equals(Boolean.class)) {
        if (element.equals("0")) {
          outer = Boolean.valueOf(false);
        } else {
          outer = Boolean.valueOf(true);
        }
      }
      raw.add(outer);
      posit++;
    }
    return raw;
  }

  public String requestLocalData(String RQ, int N)
  {
    this.RQ_STR = RQ;
    this.DATA = this.xmlparser.parseXML(this.RQ_STR);
    if (this.DATA.size() != 0)
    {
      MSS_RQ_XML_Pattern cortage = (MSS_RQ_XML_Pattern)this.DATA.get(0);
      Vector<String> row = new Vector(cortage.getAttributes().size());
      MSS_Pair str;
      for (Iterator i$ = cortage.getAttributes().iterator(); i$.hasNext(); row.add(str.getValue())) {
        str = (MSS_Pair)i$.next();
      }
      return (String)row.get(N);
    }
    return null;
  }

  public void setLocalData(String data, int row, int col)
  {
    this.model.setValueAt(data, row, col);
  }

  public void fillTable(JTable tbl)
  {
    DefaultTableModel prevModel = (DefaultTableModel)tbl.getModel();
    boolean eqls = true;

    int r1 = tbl.getRowCount();
    int r2 = this.model.getRowCount();
    int c1 = tbl.getColumnCount();
    int c2 = this.model.getColumnCount();
    if ((c1 != c2) || (r1 != r2)) {
      eqls = false;
    }
    if (eqls) {
      for (int i = 0; i < r1; i++)
      {
        if (!eqls) {
          break;
        }
        for (int j = 0; j < c1; j++) {
          if ((this.model.getValueAt(i, j) != null) && (!this.model.getValueAt(i, j).equals(tbl.getValueAt(i, j))))
          {
            eqls = false;
            break;
          }
        }
      }
    }
    if (!eqls)
    {
      tbl.setModel(this.model);
      prepareTableRenderers(tbl);
    }
  }

  private void prepareTableRenderers(JTable tbl)
  {
    TableColumnModel tcol = tbl.getColumnModel();

    int class_count = this.class_desc.length;
    for (int i = 0; (i < tcol.getColumnCount()) && (i < class_count); i++)
    {
      if (this.class_desc[i].equals(Boolean.class)) {}
      tcol.getColumn(i).setPreferredWidth(this.descriptor[i].length());
    }
    tbl.setColumnModel(tcol);
  }
}
