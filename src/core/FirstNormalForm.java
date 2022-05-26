package core;

import java.util.ArrayList;

/**
 *
 * @author jad nasser
 */
public class FirstNormalForm {

    private final ArrayList<ArrayList<String>> data;

    protected FirstNormalForm(ArrayList<ArrayList<String>> data) {
        this.data = data;
    }

    //returns a strings that tells if the data is not valid or the data is OK
    protected String checkValidity() {
        //checking if the data is empty
        if (data.isEmpty()) {
            return "Empty excel sheet";
        }
        if (data.size() == 1) {
            return "Table with only columns names";
        }
        //checking if there are any empty entries
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).size(); j++) {
                String entry = data.get(i).get(j);
                if (entry.equals("")) {
                    return "Empty fields or row/column span are not allowed";
                }
            }
        }
        //checking if there are same column names
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < data.get(0).size(); i++) {
            String columnName = data.get(0).get(i);
            if (temp.contains(columnName)) {
                return "Columns with same names are not allowed";
            }
            temp.add(columnName);
        }
        return "no problem";
    }

    //correct a row that is not in 1nf
    //returns a matrix of rows because a row may have a fields that contains multiple
    //value so to correct multiple values the row may results to many rows
    //for example if we have a row that have 2 fields that its fields contains
    //2 values and 3 values respectively then this row will results to 2*3 rows that
    //mean this row will be replaced by six rows that does not contains any field with
    //multiple values
    //if the row is already correct the function will return a matrix that only
    //contains this row
    private ArrayList<ArrayList<String>> correctRow(ArrayList<String> row) {
        ArrayList<ArrayList<String>> resultRows = new ArrayList<>();
        resultRows.add(new ArrayList<String>(row));
        //traversing through each field of the row
        for (int i = 0; i < row.size(); i++) {
            ArrayList<ArrayList<String>> temp = new ArrayList<>();
            for (int j = 0; j < resultRows.size(); j++) {
                //in this system a field that contains multiple values is the
                //field that contains semicolons
                //we need to split this field by semicolon that results in an
                //array of values
                String[] values = resultRows.get(j).get(i).split(";");
                //for each value we need to create a row for it
                for (String value : values) {
                    ArrayList<String> newRow = new ArrayList<String>(resultRows.get(j));
                    newRow.set(i, value);
                    temp.add(newRow);
                }
            }
            //the new rows will replace the old rows
            resultRows = temp;
        }
        return resultRows;
    }

    //this method is to compare if two rows are equal
    private boolean compareRows(ArrayList<String> row1, ArrayList<String> row2) {
        for (int i = 0; i < row1.size(); i++) {
            if (!row1.get(i).equals(row2.get(i))) {
                return false;
            }
        }
        return true;
    }

    //convert the data to first normal form
    protected ArrayList<ArrayList<String>> convert() {
        ArrayList<ArrayList<String>> dataIn1NF = new ArrayList<>();
        //adding the columns names to the new data
        ArrayList<String> columnsNames = new ArrayList<String>(data.get(0));
        dataIn1NF.add(columnsNames);
        //correcting the fields that contains multiple values
        for (int i = 1; i < data.size(); i++) {
            ArrayList<ArrayList<String>> newRows = correctRow(data.get(i));
            dataIn1NF.addAll(newRows);
        }
        //checking if there are any duplicated and saving there indexes
        ArrayList<Integer> rowsToBeRemoved = new ArrayList<>();
        for (int i = 1; i < dataIn1NF.size(); i++) {
            if (rowsToBeRemoved.contains(i)) {
                continue;
            }
            for (int j = i + 1; j < dataIn1NF.size(); j++) {
                if (rowsToBeRemoved.contains(j)) {
                    continue;
                }
                if (compareRows(dataIn1NF.get(i), dataIn1NF.get(j))) {
                    rowsToBeRemoved.add(j);
                }
            }
        }
        //removing the duplicated rows
        for (int i = 0; i < rowsToBeRemoved.size(); i++) {
            dataIn1NF.remove((int) rowsToBeRemoved.get(i));
        }
        return dataIn1NF;
    }
}
