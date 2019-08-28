package gui;

import reportversion.Report;
import utils.FileUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class gui extends JDialog {
    private JPanel contentPane;
    private JTabbedPane tabbedPane1;
    private JTextField nameField;
    private JButton changeDirGenerateButton;
    private JTextField pathToDirGenerateField;
    private JTextField includeGenerateField;
    private JTextField excludeGenerateField;
    private JButton checkFilesButton;
    private JButton generateReportButton;
    private JTable listFileInGeneratedReport;
    private JTextField nameLoadField;
    private JTextField dateLoadField;
    private JTextField includeLoadField;
    private JTextField excludeLoadField;
    private JTextField pathToDirLoad;
    private JButton changeDirLoadButton;
    private JTable loadedFilesTable;
    private JPanel statusLoadReportPanel;
    private JLabel statusLoadReportLabel;
    private JTextField versionGenerateField;
    private JTextField versionLoadField;
    private JFileChooser saveReportFileChooser;
    private JFileChooser loadReportFileChooser;
    private Report report;

    public gui() {
        saveReportFileChooser = new JFileChooser();
        loadReportFileChooser = new JFileChooser();

        setContentPane(contentPane);
        setModal(true);
        setTitle("JC Integrity Report Tool");

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        saveReportFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        changeDirGenerateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               saveReportFileChooser.showDialog(null, "Attach");
               if (saveReportFileChooser.getSelectedFile() != null) {
                   pathToDirGenerateField.setText(String.valueOf(saveReportFileChooser.getSelectedFile()));
               }
            }
        });

        checkFilesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                report = new Report(pathToDirGenerateField.getText(),
                        nameField.getText(),
                        versionGenerateField.getText(),
                        includeGenerateField.getText(),
                        excludeGenerateField.getText());
                try {
                    report.generate();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (NoSuchAlgorithmException e1) {
                    e1.printStackTrace();
                }
                addHeaderToGenerateFilesTable();
            }
        });

        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (report != null){
                    try {
//                        String cryptedJson = report.cryptedJson();
                        String cryptedJson = report.toJson();
                        String path = pathToDirGenerateField.getText();
                        if (!path.endsWith("/")) {
                            path = path + "/";
                        }
                        String nameClean =  report.getName().replace(" ", "_");
                        FileUtils.saveStringInFile(cryptedJson, path + "integrity_"+ nameClean +".report");
                        FileUtils.saveStringInFile(report.toCSV(), path + "integrity_"+nameClean+".csv");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        changeDirLoadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadReportFileChooser.showDialog(null, "Attach");
                String path = loadReportFileChooser.getSelectedFile().toString();
                if (path.endsWith(".report")) {
                    pathToDirLoad.setText(path);
                    String jsonCrypted = FileUtils.openFileAndRead(path);
                    try {
//                        report = Report.decryptedJson(jsonCrypted);
                        report = Report.fromJson(jsonCrypted);
                        nameLoadField.setText(report.getName());
                        dateLoadField.setText(report.getCreatedAt().toString());
                        excludeLoadField.setText(report.getExcludeRegex());
                        includeLoadField.setText(report.getIncludeRegex());
                        versionLoadField.setText(report.getVersion());
                        addHeaderToLoadedFilesTable();
                        if (report.isValid()) {
                            statusLoadReportPanel.setBackground(Color.green);
                            statusLoadReportLabel.setForeground(Color.black);
                            statusLoadReportLabel.setText("VALID");
                        } else {
                            statusLoadReportPanel.setBackground(Color.red);
                            statusLoadReportLabel.setForeground(Color.black);
                            statusLoadReportLabel.setText("FAIL");
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        this.addHeaderToLoadedFilesTable();
        this.addHeaderToGenerateFilesTable();

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        gui dialog = new gui();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public void addHeaderToLoadedFilesTable(){
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Validity");
        tableModel.addColumn("File");
        if (this.report != null){
            this.report.getFiles().stream()
                    .forEach(f -> {
                        try {
                            tableModel.addRow(new Object[]{f.check(), f.getPath()});
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                    });
        }
        this.loadedFilesTable.setModel(tableModel);
        this.loadedFilesTable.getTableHeader().setReorderingAllowed(false);
        this.loadedFilesTable.getColumnModel().getColumn(0).setMaxWidth(100);
        this.loadedFilesTable.getColumnModel().getColumn(0).setCellRenderer(new validityCellRender());
    }

    public void addHeaderToGenerateFilesTable(){
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("File");
        tableModel.addColumn("Checksum");
        if (this.report != null){
            this.report.getFiles().stream()
                    .forEach(f -> tableModel.addRow(new Object[]{f.getPath(), f.getShortHash()}));
        }
        this.listFileInGeneratedReport.setModel(tableModel);
        this.listFileInGeneratedReport.getTableHeader().setReorderingAllowed(false);
        this.listFileInGeneratedReport.getColumnModel().getColumn(1).setMaxWidth(100);
    }
}
