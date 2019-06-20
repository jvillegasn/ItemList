
//Final Project CIT 285
//Student: Javier Villegas
//Date: 16/05/2017
//Item list program
//Add items to a list, edit item from a list, delete items from a list.

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.Handler;
import java.text.NumberFormat;
import java.text.DecimalFormat;

public class ItemList extends JFrame {
    private static Logger log = Logger.getLogger(ItemList.class.getName());
	

    private ItemDAO itemDAO;

    private JTextField idField;
    private JTextField nameField;
    private JTextField quantityField;
    private JTextField priceField;
    private JTextField saleField;
   

    private JList list;
    private DefaultListModel listModel;
	

    private ArrayList<JTextField> editableTextFields;
    private ArrayList<JTextField> allTextFields;

    public ItemList() {
        super("Item List");
		System.out.println(ItemList.class.getName());

        try {
            itemDAO = new ItemDAO();
        } catch (Exception e) {
            handleFatalException(e);
        }

        editableTextFields = new ArrayList<JTextField>();
        allTextFields = new ArrayList<JTextField>();

        //center panel
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new GridLayout(1, 1));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(400, 400);

        //the ID field does not get shown, but it gets used by this class
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(4, 2));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(300, 300);
		
        idField = new JTextField("-1");
        nameField = addLabelAndTextField("Name", 100, true, centerPanel);
        quantityField = addLabelAndTextField("Quantity", 10, true, centerPanel);
        priceField = addLabelAndTextField("Price", 10, true, centerPanel);
        saleField = addLabelAndTextField("Sale", 10, true, centerPanel);

        add(centerPanel, BorderLayout.CENTER);
		
        listModel = new DefaultListModel();
        list = new JList(listModel);
        list.setLayoutOrientation(JList.VERTICAL_WRAP);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.setVisibleRowCount(7);
        try {
            refreshList();
        } catch (Exception e) {
            handleFatalException(e);
        }
        northPanel.add(list);
		
		add(northPanel, BorderLayout.NORTH);
		
		JPanel southPanel = new JPanel(new GridLayout(1, 2));
		JButton addB = new JButton("Add");
		addB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                       //saveItem();
                    }
                }
        );
		southPanel.add(addB);
		
		JButton editB = new JButton("Edit");
        editB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                       editItem();
                    }
                }
        );
        southPanel.add(editB);
		
		JButton deleteB = new JButton("Delete");
        deleteB.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        deleteItem();
                    }
                }
        );
        southPanel.add(deleteB);
		
		add(southPanel, BorderLayout.SOUTH);
		
		setVisible(true);
	}

    private void deleteItem() {
        Item item = getItem();
        if (item != null) {
            int result = JOptionPane.showOptionDialog(this, "Are you sure you want to delete " + item + "?", "Confirm Delete",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

            if (result == JOptionPane.YES_OPTION) {
                try {
                    itemDAO.deleteItem(item);
                    refreshList();
                } catch (Exception e) {
                    handleFatalException(e);
                }

                if (Integer.parseInt(idField.getText()) == item.getRegNum()) {
                    clearTextFields();
                    setFieldsEditable(false);
                    //addB.setEnabled(false);
                }
            }
        }
    }

    private void editItem() {
        Item item = getItem();
        if (item != null) {
            clearTextFields();
            setFieldsEditable(true);
            populateFields(item);
            //addB.setEnabled(true);
        }
    }


    private Item getItem() {
        int selectedIndex = list.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(null, "Please select an item from the list.", "ERROR", JOptionPane.PLAIN_MESSAGE);
            return null;
        }

        return (Item) listModel.getElementAt(selectedIndex);
    }

	
    private void saveItem() throws Exception {
        Item item = populateItem();
        if (item != null) {
            
            if (item.getRegNum() != -1) {
                itemDAO.editItem(item);
            } else {
                itemDAO.addItem(item);
            }

            clearTextFields();
            setFieldsEditable(false);
            refreshList();
            //addB.setEnabled(false);
        }
    }

    private void refreshList() throws Exception {
        List<Item> items = itemDAO.getList();

        listModel.clear();

        for (Item item : items) {
            log.fine("Adding item: " + item);
            listModel.addElement(item);
        }
    }

    private void setFieldsEditable(boolean b) {
        for (JTextField textField : editableTextFields) {
            textField.setEditable(b);
        }
    }

    protected void clearTextFields() {
        for (JTextField textField : allTextFields) {
            textField.setText("");
        }

        idField.setText("-1");
    }

    private void handleFatalException(Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.PLAIN_MESSAGE);
        System.exit(1);
    }

	
    private Item populateItem() {
        try {
            Item item = new Item();
            item.setRegNum(Integer.parseInt(idField.getText()));
            item.setName(nameField.getText());
            item.setQuantity(Integer.parseInt(quantityField.getText()));
            item.setPrice(getDoubleValue(priceField.getText(), "Price"));
            item.setSale(Boolean.valueOf(saleField.getText()));

            return item;
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, nfe.getMessage(), "ERROR", JOptionPane.PLAIN_MESSAGE);
            return null;
        }
    }

    private void populateFields(Item item) {
        NumberFormat dollarsFormat = new DecimalFormat("$0.00");

        idField.setText(String.valueOf(item.getRegNum()));
        nameField.setText(item.getName());
        quantityField.setText(Integer.toString(item.getQuantity()));
		priceField.setText(dollarsFormat.format(item.getPrice()));
        saleField.setText(String.valueOf(item.getSale()));
    }

    private double getDoubleValue(String input, String fieldName) {
        try {
            return Double.valueOf(input);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(fieldName + " must contain a numeric value!");
        }
    }
    private JTextField addLabelAndTextField(String label, int fieldLength, boolean textFieldIsEditable, JPanel panel) {
        panel.add(new JLabel(label));

        JTextField textField = new JTextField(fieldLength);
        textField.setEditable(false);
        panel.add(textField);

        if (textFieldIsEditable)
            editableTextFields.add(textField);

        allTextFields.add(textField);

        return textField;
    }

    public static void main(String[] args) {
        Level enabledLoggingLevel = Level.FINEST;

        Logger.getLogger("Final Project CIT 285").setLevel(enabledLoggingLevel);

        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        for (Handler handler : handlers) {
            handler.setLevel(enabledLoggingLevel);
        }

        new ItemList();
    }
}









