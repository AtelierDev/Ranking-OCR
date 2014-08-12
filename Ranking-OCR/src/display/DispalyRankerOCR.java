
package display;

import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import ranking.Ranker;
import ranking.SimpleRanker;

/**
 * 
 * @author Nils Ryter
 */
public class DispalyRankerOCR extends javax.swing.JFrame {

    /**
     * Creates new form DispalyRankerOCR
     */
    public DispalyRankerOCR() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleRankerSelection = new javax.swing.JLabel();
        cbxRankerSelection = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtOriginalDoc = new javax.swing.JTextArea();
        titleOriginalDoc = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtComparativeDoc = new javax.swing.JTextArea();
        titleComparativeDoc = new javax.swing.JLabel();
        btRank = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("OCR ranker");

        titleRankerSelection.setText("Ranker");

        cbxRankerSelection.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Simple ranker" }));

        txtOriginalDoc.setColumns(20);
        txtOriginalDoc.setRows(5);
        jScrollPane1.setViewportView(txtOriginalDoc);

        titleOriginalDoc.setText("Original document");

        txtComparativeDoc.setColumns(20);
        txtComparativeDoc.setRows(5);
        jScrollPane2.setViewportView(txtComparativeDoc);

        titleComparativeDoc.setText("Comparative document");

        btRank.setText("Rank !");
        btRank.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRankActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(titleOriginalDoc)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(titleComparativeDoc)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)))
                    .addComponent(titleRankerSelection)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cbxRankerSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btRank)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleRankerSelection)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxRankerSelection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btRank))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(titleOriginalDoc)
                    .addComponent(titleComparativeDoc))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btRankActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRankActionPerformed
        Ranker r;
        double errorRate = Double.NaN;
        switch (cbxRankerSelection.getSelectedIndex()) {
            case 0:
                r = new SimpleRanker();
                errorRate = r.compare(txtOriginalDoc.getText(), 
                        txtComparativeDoc.getText());
        }
        JOptionPane.showMessageDialog(this, "Error rate is : " + 
                new DecimalFormat("#.00").format(errorRate) + "%");
    }//GEN-LAST:event_btRankActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DispalyRankerOCR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DispalyRankerOCR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DispalyRankerOCR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DispalyRankerOCR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DispalyRankerOCR().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btRank;
    private javax.swing.JComboBox cbxRankerSelection;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel titleComparativeDoc;
    private javax.swing.JLabel titleOriginalDoc;
    private javax.swing.JLabel titleRankerSelection;
    private javax.swing.JTextArea txtComparativeDoc;
    private javax.swing.JTextArea txtOriginalDoc;
    // End of variables declaration//GEN-END:variables
}
