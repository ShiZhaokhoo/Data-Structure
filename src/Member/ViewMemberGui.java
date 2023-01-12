/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Member;

import Entity.MemberHistoriesGroupBuy;
import Adt.QueueInterface;
import Entity.Member;
import java.awt.*;
import javax.swing.*;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Khoo Shi Zhao
 */

public class ViewMemberGui extends javax.swing.JFrame {

    private static final int DEFAULT_TABLE_ROWS = 5;
    private Member member = new Member();
    private QueueInterface<Member> members = Main.mainMenu.members;
    private MemberHistoriesGroupBuy[] historiesData;
    private int currentPageNo = 1;
    private int jTableRowIndex = 0;
    
    public ViewMemberGui(int viewPosition) {
        initComponents();
        setLocationRelativeTo(null);
        viewMember(viewPosition);
    }
    
    public void setText(Member member){
        ViewIcjTextField.setText(member.getIcNo());
        ViewMemberIdjTextField.setText(member.getMemberId());
        ViewNamejTextField.setText(member.getName());
        ViewPhonejTextField.setText(member.getPhoneNo());
        String groupBuyTimes = String.format("%d", member.getCountOfHistoriesGroupBuy());
        NumberOfGroupBuyjTextField.setText(groupBuyTimes);
        if(member.getGender() == "Male")
            ViewMalejRadioButton.setSelected(true);
        else{
            ViewFemalejRadioButton.setSelected(true);
        }
        setTableValue();
    }
    
    private void viewMember(int viewPosition){
        member = members.getEntry(viewPosition);
        getTableValue();
        setText(member);
    }
    
    private void nextPage(){
        int totalHistoriesOfGroupBuy = member.getCountOfHistoriesGroupBuy();
        int totalPageNo = (int) Math.ceil(((double)totalHistoriesOfGroupBuy/DEFAULT_TABLE_ROWS));
        if(currentPageNo < totalPageNo){
            currentPageNo++;
            jTableRowIndex+=5;
            setTableValue();
        }
        else
            JOptionPane.showMessageDialog(this, "This is Last Page Already!");
    }
    
    private void previousPage(){
        if(currentPageNo > 1){
            currentPageNo--;
            jTableRowIndex-=5;
            setTableValue();
        }
        else
            JOptionPane.showMessageDialog(this, "This is First Page Already!");
    }
    
    private void getTableValue(){
        int totalHistoriesOfGroupBuy = member.getCountOfHistoriesGroupBuy();
        historiesData = new MemberHistoriesGroupBuy[totalHistoriesOfGroupBuy];
        historiesData = member.getHistoriesGroupBuy();
    }
    
    private void setTableValue(){
        int totalHistoriesOfGroupBuy = member.getCountOfHistoriesGroupBuy();
        int totalPageNo = (int) Math.ceil(((double)totalHistoriesOfGroupBuy/DEFAULT_TABLE_ROWS));
        JTable jTable = MemberjTable;
        DefaultTableModel model = (DefaultTableModel) jTable.getModel();
        for(int r = 0; r < 5; r++){
            Object[] jTableData = new Object[5];
            jTableData[0] = "-";
            jTableData[1] = "-";
            jTableData[2] = "-";
            jTableData[3] = "-";
            
            if(totalHistoriesOfGroupBuy > (r + jTableRowIndex)){
                jTableData[0] = historiesData[r + jTableRowIndex].getGroupBuyId();
                jTableData[1] = historiesData[r + jTableRowIndex].getItemGroupId();
                jTableData[2] = historiesData[r + jTableRowIndex].getGroupBuyDescription();
                jTableData[3] = String.format("%.2f", historiesData[r + jTableRowIndex].getGroupBuyFinalPrice());
            }
            for(int columnIndex = 0; columnIndex < 4; columnIndex++)
                model.setValueAt(jTableData[columnIndex], r, columnIndex);
        }
        setTablePageValue();
    }
    
    private void setTablePageValue(){
        int totalHistoriesOfGroupBuy = member.getCountOfHistoriesGroupBuy();
        int totalPageNo = (int) Math.ceil(((double)totalHistoriesOfGroupBuy/DEFAULT_TABLE_ROWS));
        if(totalHistoriesOfGroupBuy == 0)
            totalPageNo = 1;
        String pageText = "Page " + currentPageNo + " of " + totalPageNo;
        PagejLabel.setText(pageText);
    }
    
    private void exitForm(){
        setVisible(false);
        new MemberMaintenance().setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        ViewMemberPanel = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        ViewMemberIdjTextField = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        ViewIcjTextField = new javax.swing.JTextField();
        ViewPhonejTextField = new javax.swing.JTextField();
        ViewNamejTextField = new javax.swing.JTextField();
        ViewMalejRadioButton = new javax.swing.JRadioButton();
        ViewFemalejRadioButton = new javax.swing.JRadioButton();
        jLabel43 = new javax.swing.JLabel();
        NumberOfGroupBuyjTextField = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        MemberjTable = new javax.swing.JTable();
        ViewNextButton = new javax.swing.JButton();
        PagejLabel = new javax.swing.JLabel();
        ViewPreviousButton = new javax.swing.JButton();
        CloseButton = new javax.swing.JButton();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Member Maintenance");
        setBackground(new java.awt.Color(255, 204, 204));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 204, 204));

        ViewMemberPanel.setBackground(new java.awt.Color(255, 255, 255));
        ViewMemberPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "MEMBER DETAILS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Microsoft JhengHei", 1, 18))); // NOI18N

        jLabel17.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel17.setText("Member ID: ");

        ViewMemberIdjTextField.setEditable(false);
        ViewMemberIdjTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewMemberIdjTextFieldActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel18.setText("Name: ");

        jLabel19.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel19.setText("Gender: ");

        jLabel41.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel41.setText("No IC: ");

        jLabel42.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel42.setText("Phone No: ");

        ViewIcjTextField.setEditable(false);

        ViewPhonejTextField.setEditable(false);

        ViewNamejTextField.setEditable(false);

        ViewMalejRadioButton.setBackground(new java.awt.Color(255, 255, 255));
        ViewMalejRadioButton.setText("Male");
        ViewMalejRadioButton.setEnabled(false);

        ViewFemalejRadioButton.setBackground(new java.awt.Color(255, 255, 255));
        ViewFemalejRadioButton.setText("Female");
        ViewFemalejRadioButton.setEnabled(false);

        jLabel43.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel43.setText("Group Buy Times");

        NumberOfGroupBuyjTextField.setEditable(false);
        NumberOfGroupBuyjTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NumberOfGroupBuyjTextFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ViewMemberPanelLayout = new javax.swing.GroupLayout(ViewMemberPanel);
        ViewMemberPanel.setLayout(ViewMemberPanelLayout);
        ViewMemberPanelLayout.setHorizontalGroup(
            ViewMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ViewMemberPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ViewMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ViewMemberPanelLayout.createSequentialGroup()
                        .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ViewIcjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ViewMemberPanelLayout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ViewMalejRadioButton)
                        .addGap(18, 18, 18)
                        .addComponent(ViewFemalejRadioButton))
                    .addGroup(ViewMemberPanelLayout.createSequentialGroup()
                        .addGroup(ViewMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ViewMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ViewMemberIdjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ViewNamejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(ViewMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ViewMemberPanelLayout.createSequentialGroup()
                            .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(NumberOfGroupBuyjTextField))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ViewMemberPanelLayout.createSequentialGroup()
                            .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(ViewPhonejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(62, Short.MAX_VALUE))
        );
        ViewMemberPanelLayout.setVerticalGroup(
            ViewMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ViewMemberPanelLayout.createSequentialGroup()
                .addGroup(ViewMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ViewMemberIdjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ViewMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ViewNamejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ViewMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(ViewMalejRadioButton)
                    .addComponent(ViewFemalejRadioButton))
                .addGap(13, 13, 13)
                .addGroup(ViewMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(ViewIcjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(ViewMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(ViewPhonejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ViewMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(NumberOfGroupBuyjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel44.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel44.setText("Group Buy Histories");

        MemberjTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Group Buy ID", "Group Buy Name", "Group Buy Description", "Final Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(MemberjTable);

        ViewNextButton.setText(">");
        ViewNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewNextButtonActionPerformed(evt);
            }
        });

        PagejLabel.setFont(new java.awt.Font("Microsoft JhengHei", 0, 12)); // NOI18N
        PagejLabel.setText("Page 1 of 1");

        ViewPreviousButton.setText("<");
        ViewPreviousButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewPreviousButtonActionPerformed(evt);
            }
        });

        CloseButton.setText("Back");
        CloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(ViewMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(229, 229, 229)
                .addComponent(ViewPreviousButton, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(PagejLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(ViewNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(61, 61, 61)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 626, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 54, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(CloseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(ViewMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel44)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ViewPreviousButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ViewNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(PagejLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(CloseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseButtonActionPerformed
        // TODO add your handling code here:
        exitForm();
    }//GEN-LAST:event_CloseButtonActionPerformed

    private void NumberOfGroupBuyjTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NumberOfGroupBuyjTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NumberOfGroupBuyjTextFieldActionPerformed

    private void ViewNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewNextButtonActionPerformed
        // TODO add your handling code here:
        nextPage();
    }//GEN-LAST:event_ViewNextButtonActionPerformed

    private void ViewPreviousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewPreviousButtonActionPerformed
        // TODO add your handling code here:
        previousPage();
    }//GEN-LAST:event_ViewPreviousButtonActionPerformed

    private void ViewMemberIdjTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewMemberIdjTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ViewMemberIdjTextFieldActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        exitForm();
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(ViewMemberGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ViewMemberGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ViewMemberGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ViewMemberGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new ViewMemberGui().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CloseButton;
    private javax.swing.JTable MemberjTable;
    private javax.swing.JTextField NumberOfGroupBuyjTextField;
    private javax.swing.JLabel PagejLabel;
    private javax.swing.JRadioButton ViewFemalejRadioButton;
    private javax.swing.JTextField ViewIcjTextField;
    private javax.swing.JRadioButton ViewMalejRadioButton;
    private javax.swing.JTextField ViewMemberIdjTextField;
    private javax.swing.JPanel ViewMemberPanel;
    private javax.swing.JTextField ViewNamejTextField;
    private javax.swing.JButton ViewNextButton;
    private javax.swing.JTextField ViewPhonejTextField;
    private javax.swing.JButton ViewPreviousButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
