package core;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author jad nasser
 */
public class SecondNormalForm {

    private final ArrayList<ArrayList<String>> dataIn1NF;

    protected SecondNormalForm(ArrayList<ArrayList<String>> dataIn1NF) {
        this.dataIn1NF = dataIn1NF;
    }

    //converts the data to 2nf by comparing the data and returning the
    //relational matrix that define the relations between columns
    //for example if relations[x][y] is true then column y is dependent to column x
    protected ArrayList<ArrayList<Boolean>> convert() {
        int nbOfColumns = dataIn1NF.get(0).size();
        //at the beginning all its values are true except the values at
        //the diagonal for example the value of matrix relations[i][i] is false
        ArrayList<ArrayList<Boolean>> relationsIn2NF = new ArrayList<>();
        for (int i = 0; i < nbOfColumns; i++) {
            ArrayList<Boolean> temp = new ArrayList<>();
            for (int j = 0; j < nbOfColumns; j++) {
                if (i == j) {
                    temp.add(false);
                } else {
                    temp.add(true);
                }
            }
            relationsIn2NF.add(temp);
        }
        //discover the relations from the data matrix by comparing each 2 rows in
        //the table
        for (int i = 1; i < dataIn1NF.size(); i++) {
            for (int j = i + 1; j < dataIn1NF.size(); j++) {
                //traversing through the columns of the two rows
                for (int k = 0; k < nbOfColumns; k++) {
                    if (!dataIn1NF.get(i).get(k).equals(dataIn1NF.get(j).get(k))) {
                        continue;
                    }
                    //this for loop will only executes if the values of the two
                    //elements in the two rows are equal, then we traverse
                    //again throught the columns of the two rows and when we find
                    //two different columns then we set the values corresponding
                    //to the columns to false. For example we have 2 rows row1 and
                    //row2 and we have found that row1[3]=row2[3], then we compare
                    //again the columns and if we found that for example row1[1] different
                    //than row2[1] then we set the relations[3][1] value to false;
                    for (int l = 0; l < nbOfColumns; l++) {
                        if (l == k) {
                            continue;
                        }
                        //checking if the values are different between the two rows
                        if (!dataIn1NF.get(i).get(l).equals(dataIn1NF.get(j).get(l))) {
                            relationsIn2NF.get(k).set(l, false);
                        }
                    }
                }
            }
        }
        //make a double for loop  for the relations matrix and
        //check if 'x is dependent on y' is true then set 'y is dependent to x' to false
        for (int i = 0; i < nbOfColumns; i++) {
            for (int j = 0; j < nbOfColumns; j++) {
                if (relationsIn2NF.get(i).get(j)) {
                    relationsIn2NF.get(j).set(i, false);
                }
            }
        }
        return relationsIn2NF;
    }

    //this method is to write a table query with its data
    //this method is only used by writeSQLServerQueries()
    private void writeTableSQLServerQueries(FileOutputStream file,
            ArrayList<Integer> tableColumnsIndexes, int primaryKeyIndex) throws IOException {
        String query;
        String newLine = "\n";
        ArrayList<String> columns = dataIn1NF.get(0);
        //writing create table query with its first columns name that it is
        //the primary key
        query = "create table table" + primaryKeyIndex + " ( "
                + columns.get(primaryKeyIndex) + " varchar(50) primary key,";
        file.write(query.getBytes());
        //writing the other column names of the table except the last one
        for (int i = 0; i < tableColumnsIndexes.size() - 1; i++) {
            query = columns.get(tableColumnsIndexes.get(i)) + " varchar(50),";
            file.write(query.getBytes());
        }
        //writing the last column name of the table
        query = columns.get(tableColumnsIndexes.get(tableColumnsIndexes.size() - 1))
                + " varchar(50) )";
        file.write(query.getBytes());
        file.write(newLine.getBytes());
        //traversing through data to write the insert data queries of the table
        //this hashset is for saving the values of the primary key so this will
        //avoid repetition of inserting the same data
        HashSet<String> without_repetition = new HashSet<>();
        for (int i = 1; i < dataIn1NF.size(); i++) {
            //checking if the primary key value of the table is already
            //existed in the without_repetition hash so we
            //can skip this row
            if (without_repetition.contains(dataIn1NF.get(i).get(primaryKeyIndex))) {
                continue;
            }
            //adding this primary key value to the without repetion list so
            //in the next time if this value occures in other row that row will
            //be skipped
            without_repetition.add(dataIn1NF.get(i).get(primaryKeyIndex));
            //writing an inset data query with the first value that it
            //is the primary key
            query = "insert into table" + primaryKeyIndex + " values( '"
                    + dataIn1NF.get(i).get(primaryKeyIndex) + "',";
            file.write(query.getBytes());
            //writing the other values of an insert query except the last one
            for (int j = 0; j < tableColumnsIndexes.size() - 1; j++) {
                query = "'" + dataIn1NF.get(i).get(tableColumnsIndexes.get(j)) + "',";
                file.write(query.getBytes());
            }
            //writing the last value of an insert query
            query = "'"
                    + dataIn1NF.get(i).get(tableColumnsIndexes.get(tableColumnsIndexes.size() - 1))
                    + "')";
            file.write(query.getBytes());
            file.write(newLine.getBytes());
        }
    }

    //this method is to write sql server queries for the main table with its data
    //this method is only used by writeSQLServerQueries()
    private void writeMainTableSQLServerQueries(FileOutputStream file,
            ArrayList<ArrayList<Boolean>> relationsIn2NF,
            ArrayList<Integer> tableColumnsIndexes) throws IOException {
        ArrayList<String> columns = dataIn1NF.get(0);
        String query;
        String newLine = "\n";
        //writing the create main table query
        query = "create table mainTable( ";
        file.write(query.getBytes());
        //continiuing writing create main table query by writing all the columns names of
        //the table except the last column
        for (int i = 0; i < tableColumnsIndexes.size() - 1; i++) {
            query = columns.get(tableColumnsIndexes.get(i)) + " varchar(50) ";
            file.write(query.getBytes());
            //checking if a column have a table on its own then this column should
            //be a foreign key in this table referencing to the column's table
            if (relationsIn2NF.get(tableColumnsIndexes.get(i)).contains(true)) {
                query = "foreign key references table" + tableColumnsIndexes.get(i)
                        + "(" + columns.get(tableColumnsIndexes.get(i)) + "),";
                file.write(query.getBytes());
            } else {
                query = ",";
                file.write(query.getBytes());
            }
        }
        //writing the last column name for completing the create main table query
        int lastTableColumnIndex = tableColumnsIndexes.get(tableColumnsIndexes.size() - 1);
        query = columns.get(lastTableColumnIndex) + " varchar(50) ";
        file.write(query.getBytes());
        //checking if that column have a table on its own then this column should
        //be a foreign key in this table referencing to the column's table
        if (relationsIn2NF.get(lastTableColumnIndex).contains(true)) {
            query = "foreign key references table" + lastTableColumnIndex + "("
                    + columns.get(lastTableColumnIndex) + ") )";
            file.write(query.getBytes());
        } else {
            query = ")";
            file.write(query.getBytes());
        }
        file.write(newLine.getBytes());
        //writing the insert data queries of the main table
        for (int i = 1; i < dataIn1NF.size(); i++) {
            //writing an insert data query
            query = "insert into mainTable values(";
            file.write(query.getBytes());
            //completing the insert data query by writing all the values exept the last one
            for (int j = 0; j < tableColumnsIndexes.size() - 1; j++) {
                query = "'" + dataIn1NF.get(i).get(tableColumnsIndexes.get(j)) + "',";
                file.write(query.getBytes());
            }
            //writing the last value of an insert data query
            query = "'" + dataIn1NF.get(i).get(lastTableColumnIndex) + "')";
            file.write(query.getBytes());
            file.write(newLine.getBytes());
        }
    }

    //this method converts the 1NF data to 2NF and write the sql server queries to a file.
    //The execution of the result file in sql server database will create the
    //database tables with its data in 2nf.
    protected void writeSQLServerQueries(String result_file_path) throws IOException {
        int nbOfColumns = dataIn1NF.get(0).size();
        //getting the relational matrix by converting to 2nf
        ArrayList<ArrayList<Boolean>> relationsIn2NF = convert();
        //create an arraylist to store the columns indexes that will present in the
        //main table.
        //the columns that will presents in the main table are the columns that
        //it is not dependent to any other column.
        ArrayList<Integer> mainTableColumnsIndexes = new ArrayList<>();
        for (int i = 0; i < nbOfColumns; i++) {
            boolean isDependent = false;
            for (int j = 0; j < nbOfColumns; j++) {
                if (relationsIn2NF.get(j).get(i)) {
                    isDependent = true;
                    break;
                }
            }
            if (!isDependent) {
                mainTableColumnsIndexes.add(i);
            }
        }
        //writing all the tables queries with its data queries
        try ( FileOutputStream file = new FileOutputStream(result_file_path)) {
            //writing all tables queries with its data except the main table queries
            for (int i = 0; i < nbOfColumns; i++) {
                //a columns will not have a table of its own if:
                //a coulumn index is not in the mainTableColumnsIndexes arraylist
                //or
                //that column does not have any dependent column
                if (!mainTableColumnsIndexes.contains(i) || !relationsIn2NF.get(i).contains(true)) {
                    continue;
                }
                // getting all columns indexes of the table except the primary key index because
                //"i" is the primary key index
                ArrayList<Integer> tableColumnsIndexes = new ArrayList<>();
                for (int j = 0; j < nbOfColumns; j++) {
                    if (relationsIn2NF.get(i).get(j)) {
                        tableColumnsIndexes.add(j);
                    }
                }
                //writing a create table query with its insert data queries
                writeTableSQLServerQueries(file, tableColumnsIndexes, i);
            }
            //writing the create main table query with its insert data queries
            writeMainTableSQLServerQueries(file, relationsIn2NF, mainTableColumnsIndexes);
        }
    }

}
