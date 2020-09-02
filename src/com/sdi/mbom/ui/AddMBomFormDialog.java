package com.sdi.mbom.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import com.sdi.mbom.MBOM;
import com.sdi.mbom.PreMBOM;
import com.teamcenter.rac.aif.AbstractAIFDialog;
import com.teamcenter.rac.aif.InterfaceAIFOperationListener;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.util.PropertyLayout;
import com.teamcenter.rac.util.VerticalLayout;
import com.teamcenter.rac.util.iTextField;

/**
 * 
 * @author 
 *
 */
public class AddMBomFormDialog extends AbstractAIFDialog implements ActionListener, InterfaceAIFOperationListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3620775456570162810L;
	
	private JPanel contentPane;
	private JButton createButton;
	private JButton cancelButton;
	private iTextField mBomTopId;
	private iTextField smdPhantomId;
	private iTextField autoId;
	private iTextField manualId;
	
	private JLabel  jlblSourceObjInfo;
	
	private JTable resultTable;
	private String[] header;
	private DialogTableModel tableModel;
	private JScrollPane resultScrollPane;

	private MBOM mbom;

	private boolean isPreMBOM;
	
	public AddMBomFormDialog(Frame frame)
	{
	  super(frame, false);
	
	  initUI();
	}
	
	private void initUI()
	{
		//Header header = new Header(, "M-BOM 생성");
		setTitle("M-BOM 생성");
		
		this.contentPane = ((JPanel)getContentPane());
		this.contentPane.setBackground(new Color(250,250,250));
		this.contentPane.setLayout(new VerticalLayout());
		this.contentPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		//this.contentPane.add("bound.bind.center.center", header);
		this.contentPane.add("bound.bind.center.center", getHeaderPanel());
		this.contentPane.add("bound.bind.center.center", getDataPanel());
		this.contentPane.add("bound.bind.center.center", getButtonPanel());
	}
	
	private JPanel getHeaderPanel() {
		
		JPanel headerPanel = new JPanel(new BorderLayout(5,5));
		headerPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		
		//headerPanel.setOpaque(false);
		headerPanel.setBackground(Color.lightGray);
		
		//JPanel leftPanel = new JPanel(new BorderLayout(3,3));
		
		JLabel  jlblTargetID = new JLabel("Source Item : ");
		jlblTargetID.setPreferredSize(new Dimension(120, 25));
		jlblTargetID.setHorizontalAlignment(SwingConstants.RIGHT);
		//leftPanel.add(jlblTargetID);
		
		//JPanel centerPanel = new JPanel(new BorderLayout(3,3));
		
		this.jlblSourceObjInfo = new JLabel(" - ");
		this.jlblSourceObjInfo.setPreferredSize(new Dimension(400, 25));
		this.jlblSourceObjInfo.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		//leftPanel.add(jlblTargetObjInfo);
		
		
		headerPanel.add(this.jlblSourceObjInfo, BorderLayout.CENTER);
		headerPanel.add(jlblTargetID, BorderLayout.WEST);
		
		return headerPanel;
	}

	private JPanel getButtonPanel()
	{
	  JPanel buttonPanel = new JPanel(new FlowLayout(2, 10, 0));
	  buttonPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
	
	  this.createButton = new JButton("생성");
	  this.cancelButton = new JButton("취소");
	
	  buttonPanel.setOpaque(false);
	
	  this.createButton.addActionListener(this);
	  this.cancelButton.addActionListener(this);
	
	  buttonPanel.add(this.createButton);
	  buttonPanel.add(this.cancelButton);
	
	  return buttonPanel;
	}
	
	private JPanel getDataPanel()
	{
		JPanel panel = new JPanel(new BorderLayout(10,10));
		panel.setOpaque(false);
		JPanel dataPanel = new JPanel(new PropertyLayout(5,5));
		dataPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		this.mBomTopId = new iTextField(15, true);
		this.smdPhantomId = new iTextField(15, true);
		this.autoId = new iTextField(15, false);
		this.manualId = new iTextField(15, false);
		
		dataPanel.setOpaque(false);
		JLabel  jlblTopID = new JLabel("M-BOM TOP ID : ");
		JLabel  jlblSMDID = new JLabel("SMD Phantom ID : ");
		JLabel  jlblAutoID = new JLabel("Auto ID : ");
		JLabel  jlblManualID = new JLabel("Manual ID : ");
		
		
		jlblTopID.setPreferredSize(new Dimension(120, 25));
		jlblTopID.setHorizontalAlignment(SwingConstants.RIGHT);
		jlblSMDID.setPreferredSize(new Dimension(120, 25));
		jlblSMDID.setHorizontalAlignment(SwingConstants.RIGHT);
		jlblAutoID.setPreferredSize(new Dimension(120, 25));
		jlblAutoID.setHorizontalAlignment(SwingConstants.RIGHT);
		jlblManualID.setPreferredSize(new Dimension(120, 25));
		jlblManualID.setHorizontalAlignment(SwingConstants.RIGHT);
		
		dataPanel.add("1.1.right.center", jlblTopID);
		dataPanel.add("1.2.right.center", this.mBomTopId);
		dataPanel.add("2.1.right.center", jlblSMDID);
		dataPanel.add("2.2.right.center", this.smdPhantomId);
		dataPanel.add("1.3.right.center", jlblAutoID);
		dataPanel.add("1.4.right.center", this.autoId);
		dataPanel.add("2.3.right.center", jlblManualID);
		dataPanel.add("2.4.right.center", this.manualId);
		
		JPanel tablePanel = new JPanel(new BorderLayout());
		this.resultScrollPane = new JScrollPane();
		
		this.header = new String[] { "Lvl", "부품ID", "부품명", "MBOMAddress", "참조지정자", "처리결과" };
		this.tableModel = new DialogTableModel(this.header, 0) ;
		
		this.resultTable = new JTable(this.tableModel);
		
		this.resultTable.getTableHeader().setBackground(Color.lightGray);
		this.resultTable.getTableHeader().setBorder(BorderFactory.createRaisedBevelBorder());
		
		this.resultTable.getColumnModel().getColumn(0).setPreferredWidth(80);
		this.resultTable.getColumnModel().getColumn(1).setPreferredWidth(200);
		this.resultTable.getColumnModel().getColumn(2).setPreferredWidth(200);
		this.resultTable.getColumnModel().getColumn(3).setPreferredWidth(200);
		this.resultTable.getColumnModel().getColumn(4).setPreferredWidth(200);
		this.resultTable.getColumnModel().getColumn(5).setPreferredWidth(80);
		
		this.resultTable.setAutoResizeMode(1);
		this.resultScrollPane.getViewport().add(this.resultTable);
		this.resultScrollPane.setPreferredSize(new Dimension(800, 500));
		
		tablePanel.add(resultScrollPane);
		
		panel.add("North", dataPanel);
		panel.add("Center", tablePanel);
		
		return panel;
	}
	
	public void actionPerformed(ActionEvent event)
	{
	  Object obj = event.getSource();
	
	  if (obj.equals(this.createButton))
	  {
	    
	  }
	  else if (obj.equals(this.cancelButton))
	  {
	    setVisible(false);
	    dispose();
	  }
	  
	}

	@Override
	public void endOperation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startOperation(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setMBOM(MBOM mbom) {
		this.mbom = mbom;
		
		if(mbom != null) {
			loadMBOM(mbom);
		}else {
			clearMBOM(mbom);
		}
		
	}
	
	public MBOM getMBOM() {
		return this.mbom;
	}
	
	
	protected void updateSourceInfo(TCComponent sourceComp) {
		
		try {
			String sourceInfo = sourceComp.getObjectString();
			this.jlblSourceObjInfo.setText(sourceInfo);
		}catch(Throwable t) {
			t.printStackTrace();
			this.jlblSourceObjInfo.setText("");
		}
	}
	
	protected void loadMBOM(MBOM mbom) {

		updateSourceInfo(mbom.getSourceComponent());
		
		if(mbom instanceof PreMBOM) {
			 this.isPreMBOM = true;
		}else {
			
			//update 기존 M-BOM 정보 업데이트
			
		}
	}

	protected void clearMBOM(MBOM mbom) {
		
		//target clear
		this.jlblSourceObjInfo.setText(" - ");
		
		// mbom item clear
		this.mBomTopId.setText("");
		this.smdPhantomId.setText("");
		this.autoId.setText("");
		this.manualId.setText("");
		
		//table clear
		this.tableModel = new DialogTableModel(this.header, 0) ;
		this.resultTable.setModel(tableModel);
		this.resultTable.updateUI();
	}

	
}
