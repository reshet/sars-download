package com.mresearch.spsstools.cup;


import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class AnketLoader
{
  public static String URL_meta_all = "http://mresearch.survey-archive.com/MSS/MainGate.php";
  public static String URL_data_all = "http://mresearch.survey-archive.com/MSS/MainGate.php";
  public static String login = "10000";
  public static String pswd = "mysecret";
  private static final long serialVersionUID = 6663011277138149044L;
  private MSS_RQ_Admin reqHandler = new MSS_RQ_Admin("Tool", login, pswd);
  private MSS_RQ_TableDescriptor QuizTDesc = new MSS_RQ_TableDescriptor(new String[] { "���" }, new Class[] { Integer.class });
  private MSS_RQ_XMLtoTableDescriptor QuizT_XML_Desc = new MSS_RQ_XMLtoTableDescriptor(new String[] { "name", "ID" });
  private MSS_RQ_TableFiller QuizUpdater = new MSS_RQ_TableFiller(this.QuizTDesc, this.QuizT_XML_Desc);
  private int anketID;
  private String task_data_unfolded;
  private JTable cases_tbl;
  private JTable meta_tbl;
  private String[] col_idents;
  private Map<Integer, Setting_AssocPair<DefaultTableModel, DefaultTableModel>> ankets_meta_cases = new HashMap();

  public static void fillWithMissingValues(JTable cases_table, JTable meta_table)
  {
    for (int i = 0; i < cases_table.getRowCount(); i++) {
      for (int j = 0; j < cases_table.getColumnCount(); j++)
      {
        String cur_val = "";
        if ((cases_table.getModel().getValueAt(i, j) instanceof String)) {
          cur_val = (String)cases_table.getModel().getValueAt(i, j);
        }
        if ((cases_table.getModel().getValueAt(i, j) instanceof Integer)) {
          cur_val = String.valueOf(cases_table.getModel().getValueAt(i, j));
        }
        if ((cases_table.getModel().getValueAt(i, j) instanceof Double)) {
          cur_val = String.valueOf(cases_table.getModel().getValueAt(i, j));
        }
        if ((cur_val == null) || (cur_val.equals("")) || (cur_val.equals("null")))
        {
          String col_name = cases_table.getColumnName(j);
          int row_in_meta = -1;
          for (int k = 0; k < meta_table.getRowCount(); k++)
          {
            String cur_var_name = (String)meta_table.getModel().getValueAt(k, 0);
            if (cur_var_name.equals(col_name))
            {
              row_in_meta = k;
              break;
            }
          }
          int col_in_meta = -1;
          for (int k = 0; k < meta_table.getColumnCount(); k++) {
            if (meta_table.getModel().getColumnName(k).equals("NMissingValue1"))
            {
              col_in_meta = k;
              break;
            }
          }
          if (row_in_meta == -1)
          {
            String name = col_name;
            int have_ = name.lastIndexOf("_");
            if (have_ > -1)
            {
              String base_name = name.substring(have_ + 1, name.length());
              for (int k = 0; k < meta_table.getRowCount(); k++)
              {
                String cur_var_name = (String)meta_table.getModel().getValueAt(k, 0);
                System.out.println("Curr - " + cur_var_name);
                if (cur_var_name.equals(base_name))
                {
                  row_in_meta = k;
                  System.out.println("FOUND: " + k);
                  break;
                }
              }
            }
          }
          if ((row_in_meta != -1) && (col_in_meta != -1))
          {
            String mis_str = (String)meta_table.getModel().getValueAt(row_in_meta, col_in_meta);
            cases_table.getModel().setValueAt(mis_str, i, j);
          }
        }
      }
    }
  }

  public AnketLoader(int anketID)
  {
    this.anketID = anketID;
  }

  public void unfoldTask()
  {
    this.task_data_unfolded = MSS_RQ_Request.http_request(this.reqHandler.unfoldTask(this.anketID), URL_meta_all);
    String ans = this.QuizUpdater.requestLocalData(this.task_data_unfolded, 0);
    if (ans != null) {}
  }

  public void constructMetadata(JTable cases_tbl, JTable meta_tbl) {
    this.cases_tbl = cases_tbl;
    this.meta_tbl = meta_tbl;

    MSS_RQ_XML xmler = new MSS_RQ_XML();
    System.out.println("METADATA DEBUG:");
    System.out.println(this.task_data_unfolded);

    ArrayList<MSS_RQ_XML_Pattern> ptn = xmler.parseXML(this.task_data_unfolded);
    for (MSS_RQ_XML_Pattern ptrn : ptn)
    {
      ArrayList<MSS_Pair> pairs = ptrn.getAttributes();
      String sars_content;
      if (ptrn.getNodename().equals("content"))
      {
        String sars_id = ((MSS_Pair)pairs.get(1)).getValue();
        sars_content = ((MSS_Pair)pairs.get(0)).getValue();
      }
      if (ptrn.getNodename().equals("metadata"))
      {
        String xml_meta = ((MSS_Pair)pairs.get(0)).getValue();
        xml_meta = new String(Base64.getDecoder().decode(xml_meta));
        System.out.println(xml_meta);
        ArrayList<MSS_RQ_XML_Pattern> pt = xmler.parseXML(xml_meta);
        int col_type = 2;
        int col_format = 3;
        int col_width = 4;
        int col_decimals = 5;
        int col_mv = 6;

        DefaultTableModel tmodel = new DefaultTableModel(pt.size(), 9);
        int counter = 0;
        DefaultTableModel cases_model = new DefaultTableModel(0, pt.size());
        this.col_idents = new String[pt.size()];
        for (MSS_RQ_XML_Pattern prn : pt)
        {
          this.col_idents[counter] = prn.getNodename();
          ArrayList<MSS_Pair> pairss = prn.getAttributes();
          String missingV = "0.0";
          String vartype = "";
          String quest = "";
          int found_type = -1;
          int found_alts = -1;
          int found_quest = -1;
          int iter = 0;
          for (MSS_Pair pair : pairss)
          {
            if (pair.getName().equals("vartype")) {
              found_type = iter;
            }
            if (pair.getName().equals("question")) {
              found_quest = iter;
            }
            if (pair.getName().equals("alternatives")) {
              found_alts = iter;
            }
            iter++;
          }
          if (found_quest > -1) {
            try
            {
              quest = new String(Base64.getDecoder().decode(((MSS_Pair)pairss.get(found_quest)).getValue()), "windows-1251");
            }
            catch (UnsupportedEncodingException e)
            {
              e.printStackTrace();
            }
          }
          if (found_type > -1) {
            vartype = ((MSS_Pair)pairss.get(found_type)).getValue();
          }
          if (found_alts > -1)
          {
            String alts = ((MSS_Pair)pairss.get(found_alts)).getValue();
            ArrayList<MSS_RQ_XML_Pattern> alters = xmler.parseXML(new String(Base64.getDecoder().decode(alts)));
            String altern = "";
            for (MSS_RQ_XML_Pattern alter : alters)
            {
              ArrayList<MSS_Pair> alter_item_atrs = alter.getAttributes();
              String ID = new String(Base64.getDecoder().decode(((MSS_Pair) alter_item_atrs.get(0)).getValue()));
              String name1 = ((MSS_Pair)alter_item_atrs.get(1)).getName();
              String text = "";
              if ((name1.equals("MARK")) && (alter_item_atrs.size() > 2)) {
                try
                {
                  text = new String(Base64.getDecoder().decode(((MSS_Pair)alter_item_atrs.get(2)).getValue()), "windows-1251");
                }
                catch (UnsupportedEncodingException e)
                {
                  e.printStackTrace();
                }
              } else {
                try
                {
                  text = new String(Base64.getDecoder().decode(((MSS_Pair)alter_item_atrs.get(1)).getValue()), "windows-1251");
                }
                catch (UnsupportedEncodingException e)
                {
                  e.printStackTrace();
                }
              }
              if (ID.equals("MV")) {
                missingV = text;
              } else {
                altern = altern + "\"" + ID + "\"=\"" + text + "\";";
              }
            }
            altern = altern.substring(0, altern.length() - 1);
            tmodel.setValueAt(altern, counter, 8);
          }
          tmodel.setValueAt(prn.getNodename(), counter, 0);
          tmodel.setValueAt(quest, counter, 1);
          if ((vartype.equals("double")) || (vartype.equals("integer")) || (vartype.equals("defined")))
          {
            tmodel.setValueAt("0", counter, col_type);
            tmodel.setValueAt("5", counter, col_format);
          }
          else if (vartype.equals("datetime"))
          {
            tmodel.setValueAt("20", counter, col_type);
            tmodel.setValueAt("22", counter, col_format);
          }
          else
          {
            tmodel.setValueAt("500", counter, col_type);
            tmodel.setValueAt("1", counter, col_format);
          }
          tmodel.setValueAt("8", counter, col_width);
          tmodel.setValueAt("5", counter, col_decimals);
          tmodel.setValueAt("1", counter, col_mv);
          tmodel.setValueAt(missingV, counter, 7);
          counter++;
        }
        tmodel.setColumnIdentifiers(new String[] { "VarName", "Label", "Type", "Format", "Width", "Decimals", "MvCode", "NMissingValue1", "ValueLabelTableName" });

        meta_tbl.setModel(tmodel);
        int col_labels = 8;

        TableColumnModel tcol = meta_tbl.getColumnModel();

        tcol.getColumn(0).setPreferredWidth(25);
        tcol.getColumn(1).setPreferredWidth(400);
        tcol.getColumn(2).setPreferredWidth(25);
        tcol.getColumn(col_labels).setPreferredWidth(400);

        meta_tbl.setColumnModel(tcol);

        cases_model.setColumnIdentifiers(this.col_idents);

        cases_tbl.setModel(cases_model);
      }
    }
  }

  public JTable getCases_tbl()
  {
    return this.cases_tbl;
  }

  public void constructCases()
  {
    Class[] classes = new Class[this.col_idents.length + 1];
    classes[0] = Integer.class;
    for (int i = 1; i < classes.length; i++) {
      classes[i] = String.class;
    }
    String[] col_idents_2 = new String[this.col_idents.length + 1];
    col_idents_2[0] = "���";
    for (int i = 1; i < col_idents_2.length; i++) {
      col_idents_2[i] = new String(this.col_idents[(i - 1)]);
    }
    MSS_RQ_TableDescriptor QuizTDesc2 = new MSS_RQ_TableDescriptor(col_idents_2, classes);

    MSS_RQ_XMLtoTableDescriptor QuizT_XML_Desc2 = new MSS_RQ_XMLtoTableDescriptor(col_idents_2);
    MSS_RQ_TableFiller QuizUpdater2 = new MSS_RQ_TableFiller(QuizTDesc2, QuizT_XML_Desc2);
    MSS_RQ_Admin reqHandlerAdmin = new MSS_RQ_Admin("Tool", login, pswd);
    String xml_ans_2 = MSS_RQ_Request.http_request(reqHandlerAdmin.representStatistics(this.anketID), URL_data_all);

    QuizUpdater2.updateData(xml_ans_2);

    QuizUpdater2.fillTable(this.cases_tbl);

    TableColumnModel cm = this.cases_tbl.getColumnModel();

    fillWithMissingValues(this.cases_tbl, this.meta_tbl);

    this.cases_tbl.setColumnModel(cm);
  }

  public void constructCases(int by_user_id)
  {
    Class[] classes = new Class[this.col_idents.length + 1];
    classes[0] = Integer.class;
    for (int i = 1; i < classes.length; i++) {
      classes[i] = String.class;
    }
    String[] col_idents_2 = new String[this.col_idents.length + 1];
    col_idents_2[0] = "���";
    for (int i = 1; i < col_idents_2.length; i++) {
      col_idents_2[i] = new String(this.col_idents[(i - 1)]);
    }
    MSS_RQ_TableDescriptor QuizTDesc2 = new MSS_RQ_TableDescriptor(col_idents_2, classes);

    MSS_RQ_XMLtoTableDescriptor QuizT_XML_Desc2 = new MSS_RQ_XMLtoTableDescriptor(col_idents_2);
    MSS_RQ_TableFiller QuizUpdater2 = new MSS_RQ_TableFiller(QuizTDesc2, QuizT_XML_Desc2);
    MSS_RQ_Admin reqHandlerAdmin = new MSS_RQ_Admin("Tool", login, pswd);
    String xml_ans_2 = MSS_RQ_Request.http_request(reqHandlerAdmin.representStatistics(this.anketID), URL_data_all);
    QuizUpdater2.updateData(xml_ans_2);

    QuizUpdater2.fillTable(this.cases_tbl);

    TableColumnModel cm = this.cases_tbl.getColumnModel();

    fillWithMissingValues(this.cases_tbl, this.meta_tbl);
    for (int i = 0; i < this.col_idents.length; i++)
    {
      cm.getColumn(i).setPreferredWidth(100);
      cm.getColumn(i).setWidth(100);
    }
    this.cases_tbl.setColumnModel(cm);
  }

  public Setting_AssocPair<DefaultTableModel, DefaultTableModel> performAction() {
    unfoldTask();

    JTable cases_tbl = new JTable();
    JTable meta_tbl = new JTable();

    constructMetadata(cases_tbl, meta_tbl);

    constructCases();

    Setting_AssocPair<DefaultTableModel, DefaultTableModel> mypair = new Setting_AssocPair((DefaultTableModel)cases_tbl.getModel(), (DefaultTableModel)meta_tbl.getModel());

    return mypair;
  }
}
