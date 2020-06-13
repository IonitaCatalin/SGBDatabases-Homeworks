package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class UI extends JFrame{

    private JButton startBtn=new JButton("Start Query");
    private JTable datasFromDb=new JTable();
    private JScrollPane scrollableDatas;
    private DatabaseController dbContr=new DatabaseController();
    private JComboBox sortByName=new JComboBox(new String[]{"ASC","DESC","NONE"});
    private JComboBox sortBySurname=new JComboBox(new String[]{"ASC","DESC","NONE"});
    private JComboBox sortByStock=new JComboBox(new String[]{"ASC","DESC","NONE"});
    private JRadioButton less=new JRadioButton("<");
    private JRadioButton greater=new JRadioButton(">");
    private JRadioButton equal=new JRadioButton("=");
    private JRadioButton is=new JRadioButton("IS");
    private JTextField filterCriteria=new JTextField(20);
    private JFrame mainFrame=new JFrame();
    private JPanel mainPanel=new JPanel();
    private JPanel secondaryPanel=new JPanel();
    private JPanel thirdPanel=new JPanel();


    public UI() {

        mainFrame.setSize(800,800);
        mainPanel.setLayout(new FlowLayout());
        mainPanel.add(new Label("Nume:"));
        mainPanel.add(sortByName);
        mainPanel.add(new Label("Prenume:"));
        mainPanel.add(sortBySurname);
        mainPanel.add(new Label("Bursa:"));
        mainPanel.add(sortByStock);
        secondaryPanel.setLayout(new BoxLayout(secondaryPanel,BoxLayout.Y_AXIS));
        secondaryPanel.add(mainPanel);
        thirdPanel.setLayout(new FlowLayout());
        thirdPanel.add(new Label("Bursa"));
        thirdPanel.add(less);
        thirdPanel.add(greater);
        thirdPanel.add(equal);
        thirdPanel.add(is);
        thirdPanel.add(filterCriteria);
        filterCriteria.setSize(new Dimension(50,50));
        secondaryPanel.add(thirdPanel);
        mainFrame.getContentPane().add(BorderLayout.NORTH,secondaryPanel);
        scrollableDatas=new JScrollPane(datasFromDb);
        scrollableDatas.setVisible(true);
        mainFrame.getContentPane().add(BorderLayout.CENTER,scrollableDatas);
        mainFrame.getContentPane().add(BorderLayout.SOUTH,startBtn);
        mainFrame.show(true);
        mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        startBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    setTableModel(dbContr.getQueryResult(getComposedQuery()));
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


    public void setTableModel(DefaultTableModel dm){
        datasFromDb.setModel(dm);
    }

    public String getSortByName()
    {
        return (String)sortByName.getSelectedItem();
    }
    public String getSortBySurname()
    {
        return (String)sortByName.getSelectedItem();
    }
    public String getSortByStock()
    {
        return (String)sortByStock.getSelectedItem();
    }
    public String getComposedQuery()
    {

//        return "SELECT nume,prenume,bursa FROM STUDENTI "
//                +((!filterCriteria.getText().isEmpty())?((less.isSelected())?"WHERE bursa<"+(String)filterCriteria.getText():((greater.isSelected())?"WHERE bursa>"+(String)filterCriteria.getText():"WHERE bursa="+(String)filterCriteria.getText())):"")+
//                " ORDER BY NUME "+sortByName.getSelectedItem()+",PRENUME "+sortBySurname.getSelectedItem()+",BURSA "+sortByStock.getSelectedItem();
        StringBuilder sb=new StringBuilder();
        sb.append("SELECT nume,prenume,bursa FROM STUDENTI ");
        if(!filterCriteria.getText().isEmpty())
        {
            sb.append("WHERE bursa");
            if(less.isSelected())
            {
                sb.append("<"+filterCriteria.getText());
            }
            else if(greater.isSelected())
            {
                sb.append(">"+ filterCriteria.getText());
            }
            else if(equal.isSelected())
            {
                sb.append("="+filterCriteria.getText());
            }
            else if(is.isSelected())
            {
                sb.append(" IS "+filterCriteria.getText());
            }
            sb.append(" ");
        }
        if(!sortByName.getSelectedItem().equals("NONE") || !sortBySurname.getSelectedItem().equals("NONE") || !sortByStock.getSelectedItem().equals("NONE") )
        {
            sb.append("ORDER BY ");
        }
        if(!sortByName.getSelectedItem().equals("NONE"))
        {
            sb.append("nume "+ sortByName.getSelectedItem());
            if(!sortBySurname.getSelectedItem().equals("NONE"))
            {
                sb.append(",");
            }
        }
        if(!sortBySurname.getSelectedItem().equals("NONE"))
        {
            sb.append("prenume "+ sortBySurname.getSelectedItem());
            if(!sortByStock.getSelectedItem().equals("NONE"))
            {
                sb.append(",");
            }
        }
        if(!sortByStock.getSelectedItem().equals("NONE"))
        {
            sb.append("bursa "+ sortByStock.getSelectedItem());
        }
        return sb.toString();
    }


}
