package GroupBuy;

import Adt.ListInterface;
import Entity.GroupBuyMemberEntity;
import Entity.GroupBuyEntity;
import Main.*;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ng Theng Yang
 */

public class groupBuyJoinGui extends javax.swing.JFrame {
    private ListInterface<GroupBuyEntity> currentGroupBuys;
    private GroupBuyEntity currentGroupBuy;
    private GroupBuyMemberEntity[] groupBuyMembers;
    private int joinPosition;
    
    public groupBuyJoinGui(int joinPosition){
        initComponents();
        setLocationRelativeTo(null);
        initValues(joinPosition);
        initFieldValues();
        setTableValue();
    }
    
    private void initValues(int joinPosition){
        currentGroupBuys = mainMenu.currentGroupBuys;
        currentGroupBuy = new GroupBuyEntity();
        this.joinPosition = joinPosition;
    }
    
    private void initFieldValues(){
        currentGroupBuy = currentGroupBuys.getEntry(joinPosition);
        
        groupBuyMembers = currentGroupBuy.getMemberEntity();
        
        JoinGroupBuyId.setText(currentGroupBuy.getGroupBuyId());
        JoinItemGroupId.setText(currentGroupBuy.getItemGroupId());
        JoinDesc.setText(currentGroupBuy.getGroupBuyDescription());
        JoinMaxMember.setValue(currentGroupBuy.getGroupBuyMaxMember());
        JoinOriPrice.setText(String.valueOf(currentGroupBuy.getGroupBuyOriginalPrice()));
        JoinDiscount.setText(String.valueOf(currentGroupBuy.getGroupBuyDiscountRate()));
        JoinDiscPrice.setText(String.valueOf(currentGroupBuy.getGroupBuyFinalPrice()));
    }
    
    private void setTableValue(){
        int totalMemberOfGroupBuy = currentGroupBuy.getTotalNumberOfGroupBuyMembers();
        DefaultTableModel model = (DefaultTableModel) JoinTable.getModel();
        for(int row = 0; row < 5; row++){
            Object[] jTableData = new Object[5];
            jTableData[0] = "-";
            jTableData[1] = "-";
            jTableData[2] = "-";
            jTableData[3] = "-";
            jTableData[4] = "-";
            if(row < totalMemberOfGroupBuy){
                jTableData[0] = groupBuyMembers[row].getMemberId();
                jTableData[1] = groupBuyMembers[row].getName();
                jTableData[2] = groupBuyMembers[row].getGender();
                jTableData[3] = groupBuyMembers[row].getIcNo();
                jTableData[4] = groupBuyMembers[row].getPhoneNo();
            }
            for(int columnIndex = 0; columnIndex < 5; columnIndex++)
                model.setValueAt(jTableData[columnIndex], row, columnIndex);
        }
    }
    
    public void exitForm() {
        this.setVisible(false);
        new groupBuy().setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        AddClearButton1 = new javax.swing.JButton();
        addMemberButton1 = new javax.swing.JButton();
        retrieveSmallPanel = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        JoinGroupBuyId = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        JoinDesc = new javax.swing.JTextPane();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        JoinMaxMember = new javax.swing.JSpinner();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        JoinOriPrice = new javax.swing.JTextField();
        JoinDiscount = new javax.swing.JTextField();
        JoinDiscPrice = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        JoinItemGroupId = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        JoinTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Group Buy Maintenance");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 204, 204));

        AddClearButton1.setText("Back");
        AddClearButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddClearButton1ActionPerformed(evt);
            }
        });

        addMemberButton1.setText("Add Member");
        addMemberButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addMemberButton1ActionPerformed(evt);
            }
        });

        retrieveSmallPanel.setBackground(new java.awt.Color(255, 255, 255));
        retrieveSmallPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "JOIN GROUP BUY", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Microsoft JhengHei", 1, 18))); // NOI18N

        jLabel31.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel31.setText("Group Buy Code: ");

        JoinGroupBuyId.setEditable(false);

        JoinDesc.setEditable(false);
        jScrollPane5.setViewportView(JoinDesc);

        jLabel32.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel32.setText("Group Description: ");

        jLabel33.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel33.setText("Group Maximum Member: ");

        JoinMaxMember.setModel(new javax.swing.SpinnerNumberModel(2, 2, 5, 1));
        JoinMaxMember.setEnabled(false);

        jLabel34.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel34.setText("Item Original Price / Unit: ");

        jLabel35.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel35.setText("Item Discount Rate: ");

        jLabel36.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel36.setText("Item Discounted Price / Unit: ");

        jLabel37.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel37.setText("RM ");

        jLabel38.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel38.setText("RM ");

        JoinOriPrice.setEditable(false);

        JoinDiscount.setEditable(false);

        JoinDiscPrice.setEditable(false);

        jLabel1.setText("%");

        jLabel39.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel39.setText("Item Group Id: ");

        JoinItemGroupId.setEditable(false);

        javax.swing.GroupLayout retrieveSmallPanelLayout = new javax.swing.GroupLayout(retrieveSmallPanel);
        retrieveSmallPanel.setLayout(retrieveSmallPanelLayout);
        retrieveSmallPanelLayout.setHorizontalGroup(
            retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(retrieveSmallPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(retrieveSmallPanelLayout.createSequentialGroup()
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JoinMaxMember, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(retrieveSmallPanelLayout.createSequentialGroup()
                        .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(JoinGroupBuyId, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(retrieveSmallPanelLayout.createSequentialGroup()
                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JoinDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, retrieveSmallPanelLayout.createSequentialGroup()
                            .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel37)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(JoinDiscPrice))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, retrieveSmallPanelLayout.createSequentialGroup()
                            .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel38)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(JoinOriPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(retrieveSmallPanelLayout.createSequentialGroup()
                        .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JoinItemGroupId, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        retrieveSmallPanelLayout.setVerticalGroup(
            retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(retrieveSmallPanelLayout.createSequentialGroup()
                .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JoinGroupBuyId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JoinItemGroupId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JoinMaxMember, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(jLabel38)
                    .addComponent(JoinOriPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(JoinDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(10, 10, 10)
                .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(jLabel37)
                    .addComponent(JoinDiscPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        JoinTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "MemberId", "Name", "Gender", "IC No.", "Phone No"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        JoinTable.setRowHeight(30);
        jScrollPane1.setViewportView(JoinTable);

        jLabel2.setFont(new java.awt.Font("Microsoft JhengHei", 1, 18)); // NOI18N
        jLabel2.setText("Member List");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(retrieveSmallPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(AddClearButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addMemberButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 735, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 54, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(retrieveSmallPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(addMemberButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(AddClearButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addMemberButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addMemberButton1ActionPerformed
        // TODO add your handling code here:
        int groupBuyMaxMember = currentGroupBuy.getGroupBuyMaxMember();
        int totalNumberOfMemberCurrently = currentGroupBuy.getTotalNumberOfGroupBuyMembers();
        if(totalNumberOfMemberCurrently < groupBuyMaxMember){
            setVisible(false);
            new GroupBuy.groupBuyJoinMemberGui(joinPosition, -1).setVisible(true);
        }
        else
            JOptionPane.showMessageDialog(this, "Sorry, currently this group buy is full!");
    }//GEN-LAST:event_addMemberButton1ActionPerformed

    private void AddClearButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddClearButton1ActionPerformed
        // TODO add your handling code here:
        exitForm();
    }//GEN-LAST:event_AddClearButton1ActionPerformed

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
//            java.util.logging.Logger.getLogger(groupBuyJoinGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(groupBuyJoinGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(groupBuyJoinGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(groupBuyJoinGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new groupBuyJoinGui().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddClearButton1;
    private javax.swing.JTextPane JoinDesc;
    private javax.swing.JTextField JoinDiscPrice;
    private javax.swing.JTextField JoinDiscount;
    private javax.swing.JTextField JoinGroupBuyId;
    private javax.swing.JTextField JoinItemGroupId;
    private javax.swing.JSpinner JoinMaxMember;
    private javax.swing.JTextField JoinOriPrice;
    private javax.swing.JTable JoinTable;
    private javax.swing.JButton addMemberButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JPanel retrieveSmallPanel;
    // End of variables declaration//GEN-END:variables
}