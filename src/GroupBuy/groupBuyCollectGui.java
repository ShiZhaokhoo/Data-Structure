package GroupBuy;

import Entity.MemberHistoriesGroupBuy;
import Adt.QueueInterface;
import Entity.Member;
import Adt.StackInterface;
import Entity.ItemGroup;
import Adt.ListInterface;
import Entity.GroupBuyMemberEntity;
import Entity.GroupBuyEntity;
import ItemGroup.*;
import Main.*;
import Member.*;
import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ng Theng Yang
 */

public class groupBuyCollectGui extends javax.swing.JFrame {
    
    private int collectPosition;
    private ListInterface<GroupBuyEntity> currentGroupBuys;
    private ListInterface<GroupBuyEntity> historyGroupBuys;
    private GroupBuyMemberEntity[] groupBuyMembers;
    private GroupBuyEntity collectGroupBuy;
    
    private StackInterface<ItemGroup> itemGroups;
    private QueueInterface<Member> members;
    private ItemGroup itemGroup;
    private Member member;
    private MemberHistoriesGroupBuy memberHistoryData;
    
    public groupBuyCollectGui(int collectPosition) {
        initComponents();
        setLocationRelativeTo(null);
        initValues(collectPosition);
        initFieldValues();
        setTableValue();
    }
    
    public void initValues(int collectPosition){
        this.collectPosition = collectPosition;
        currentGroupBuys = mainMenu.currentGroupBuys;
        historyGroupBuys = mainMenu.historyGroupBuys;
        collectGroupBuy = new GroupBuyEntity();
        
        itemGroups = mainMenu.itemGroups;
        itemGroup = new ItemGroup();
        members = mainMenu.members;
        member = new Member();
        memberHistoryData = new MemberHistoriesGroupBuy();
    }
    
    public void initFieldValues(){
        collectGroupBuy = currentGroupBuys.getEntry(collectPosition);
        GroupId.setText(collectGroupBuy.getGroupBuyId());
        ItemId.setText(collectGroupBuy.getItemGroupId());
        Desc.setText(collectGroupBuy.getGroupBuyDescription());
        MaxMember.setValue(collectGroupBuy.getGroupBuyMaxMember());
        OriPrice.setText(String.valueOf(collectGroupBuy.getGroupBuyOriginalPrice()));
        Discount.setText(String.valueOf(collectGroupBuy.getGroupBuyDiscountRate()));
        DiscPrice.setText(String.valueOf(collectGroupBuy.getGroupBuyFinalPrice()));
    }
    
    private void setTableValue(){
        groupBuyMembers = collectGroupBuy.getMemberEntity();
        int totalMemberOfGroupBuy = collectGroupBuy.getTotalNumberOfGroupBuyMembers();
        DefaultTableModel model = (DefaultTableModel) Table.getModel();
        for(int row = 0; row < 5; row++){
            Object[] jTableData = new Object[6];
            jTableData[0] = "-";
            jTableData[1] = "-";
            jTableData[2] = "-";
            jTableData[3] = "-";
            jTableData[4] = "-";
            jTableData[5] = false;
            if(row < totalMemberOfGroupBuy){
                jTableData[0] = groupBuyMembers[row].getMemberId();
                jTableData[1] = groupBuyMembers[row].getName();
                jTableData[2] = groupBuyMembers[row].getGender();
                jTableData[3] = groupBuyMembers[row].getIcNo();
                jTableData[4] = groupBuyMembers[row].getPhoneNo();
                jTableData[5] = groupBuyMembers[row].getCollected();
            }
            for(int columnIndex = 0; columnIndex < 6; columnIndex++)
                model.setValueAt(jTableData[columnIndex], row, columnIndex);
        }
    }
    
    private void memberAddGroupBuyHistory(){
        int totalMember = collectGroupBuy.getTotalNumberOfGroupBuyMembers();
        int match = 0;
        int position = 1;
        
        Iterator<Member> memberIterator = members.getQueueIterator();
        while(memberIterator.hasNext() && match != totalMember){
            member = memberIterator.next();
            String memberId = member.getMemberId();
            for(int memberIndex = 0; memberIndex < totalMember; memberIndex++){
                if(groupBuyMembers[memberIndex].getMemberId().equals(memberId)){
                    match++;
                    memberHistoryData = new MemberHistoriesGroupBuy(collectGroupBuy);
                    member.enqueueHistoriesGroupBuy(memberHistoryData);
                    members.replace(member, position);
                }
            }
            position++;
        }
    }
    
    private void itemGroupIncreaseGroupBuyTimes(){
        int itemGroupPosition = 1;
        boolean successful = false;
        
        Iterator<ItemGroup> itemGroupIterator = itemGroups.getStackIterator();
        while(itemGroupIterator.hasNext()&&!successful){
            itemGroup = itemGroupIterator.next();
            String itemGroupId = itemGroup.getItemGroupId();
            String groupBuyItemGroupId = collectGroupBuy.getItemGroupId();
            if(itemGroupId.equals(groupBuyItemGroupId)){
                itemGroup.addItemGroupTotalTransaction();
                successful = true;
                itemGroups.replace(itemGroup, itemGroupPosition);
            }
            itemGroupPosition++;
        }
    }
    
    private void exitForm() {                          
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
        retrieveSmallPanel2 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        GroupId = new javax.swing.JTextField();
        jScrollPane11 = new javax.swing.JScrollPane();
        Desc = new javax.swing.JTextPane();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        MaxMember = new javax.swing.JSpinner();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        OriPrice = new javax.swing.JTextField();
        Discount = new javax.swing.JTextField();
        DiscPrice = new javax.swing.JTextField();
        ItemId = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        Cancel = new javax.swing.JButton();
        Confirm = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Table = new javax.swing.JTable();
        Reset = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Group Buy Maintenance");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 204, 204));

        retrieveSmallPanel2.setBackground(new java.awt.Color(255, 255, 255));
        retrieveSmallPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "COLLECT ITEM DETAILS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Microsoft JhengHei", 1, 18))); // NOI18N

        jLabel51.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel51.setText("Group Buy Code: ");

        GroupId.setEditable(false);

        Desc.setEditable(false);
        jScrollPane11.setViewportView(Desc);

        jLabel52.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel52.setText("Group Description: ");

        jLabel53.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel53.setText("Group Maximum Member: ");

        MaxMember.setModel(new javax.swing.SpinnerNumberModel(2, 2, 5, 1));
        MaxMember.setEnabled(false);

        jLabel54.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel54.setText("Item Original Price / Unit: ");

        jLabel55.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel55.setText("Item Discount Rate: ");

        jLabel56.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel56.setText("Item Discounted Price / Unit: ");

        jLabel57.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel57.setText("RM ");

        jLabel58.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel58.setText("RM ");

        OriPrice.setEditable(false);

        Discount.setEditable(false);

        DiscPrice.setEditable(false);

        ItemId.setEditable(false);

        jLabel60.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel60.setText("Item Group ID:");

        jLabel61.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel61.setText("%");

        javax.swing.GroupLayout retrieveSmallPanel2Layout = new javax.swing.GroupLayout(retrieveSmallPanel2);
        retrieveSmallPanel2.setLayout(retrieveSmallPanel2Layout);
        retrieveSmallPanel2Layout.setHorizontalGroup(
            retrieveSmallPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(retrieveSmallPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(retrieveSmallPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(retrieveSmallPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(MaxMember, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(retrieveSmallPanel2Layout.createSequentialGroup()
                        .addGroup(retrieveSmallPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(retrieveSmallPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(GroupId, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(retrieveSmallPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Discount, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel61))
                    .addGroup(retrieveSmallPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, retrieveSmallPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel57)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(DiscPrice))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, retrieveSmallPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel58)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(OriPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(retrieveSmallPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ItemId, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        retrieveSmallPanel2Layout.setVerticalGroup(
            retrieveSmallPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(retrieveSmallPanel2Layout.createSequentialGroup()
                .addGroup(retrieveSmallPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(GroupId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(retrieveSmallPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ItemId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(retrieveSmallPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(retrieveSmallPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MaxMember, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(retrieveSmallPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel54)
                    .addComponent(jLabel58)
                    .addComponent(OriPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(retrieveSmallPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55)
                    .addComponent(Discount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel61))
                .addGap(10, 10, 10)
                .addGroup(retrieveSmallPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel56)
                    .addComponent(jLabel57)
                    .addComponent(DiscPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel59.setFont(new java.awt.Font("Microsoft JhengHei", 1, 18)); // NOI18N
        jLabel59.setText("Member List");

        Cancel.setText("Cancel");
        Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelActionPerformed(evt);
            }
        });

        Confirm.setText("Confirm");
        Confirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConfirmActionPerformed(evt);
            }
        });

        Table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Member ID", "Name", "Gender", "IC No.", "Phone No.", "Collected"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Table.setRowHeight(30);
        jScrollPane1.setViewportView(Table);
        if (Table.getColumnModel().getColumnCount() > 0) {
            Table.getColumnModel().getColumn(5).setMaxWidth(55);
        }

        Reset.setText("Reset");
        Reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 751, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(retrieveSmallPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Reset, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Confirm, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(retrieveSmallPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(Confirm, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Reset, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jLabel59)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelActionPerformed
        // TODO add your handling code here:
        exitForm();
    }//GEN-LAST:event_CancelActionPerformed

    private void ConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConfirmActionPerformed
        int totalMember = collectGroupBuy.getTotalNumberOfGroupBuyMembers();
        int countCollected = 0;
        DefaultTableModel model = (DefaultTableModel) Table.getModel();
        
        for(int row = 0; row < totalMember; row++){
            boolean collected = (boolean)model.getValueAt(row, 5);
            if(collected == true)
                countCollected++;
            collectGroupBuy.setGroupBuyMemberCollected(row + 1, collected);
        }
        
        if(countCollected == totalMember){
            historyGroupBuys.add(collectGroupBuy, 1);
            currentGroupBuys.remove(collectPosition);
            memberAddGroupBuyHistory();
            itemGroupIncreaseGroupBuyTimes();
            JOptionPane.showMessageDialog(this, "All member of this group buy have collected, this group buy will transfer to History");
        }
        else{
            currentGroupBuys.replace(collectGroupBuy, collectPosition);
        }
        exitForm();
    }//GEN-LAST:event_ConfirmActionPerformed

    private void ResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResetActionPerformed
        setTableValue();
    }//GEN-LAST:event_ResetActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        exitForm();
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
//     */
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
//            java.util.logging.Logger.getLogger(groupBuyCollectGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(groupBuyCollectGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(groupBuyCollectGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(groupBuyCollectGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new groupBuyCollectGui().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Cancel;
    private javax.swing.JButton Confirm;
    private javax.swing.JTextPane Desc;
    private javax.swing.JTextField DiscPrice;
    private javax.swing.JTextField Discount;
    private javax.swing.JTextField GroupId;
    private javax.swing.JTextField ItemId;
    private javax.swing.JSpinner MaxMember;
    private javax.swing.JTextField OriPrice;
    private javax.swing.JButton Reset;
    private javax.swing.JTable Table;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JPanel retrieveSmallPanel2;
    // End of variables declaration//GEN-END:variables
}
