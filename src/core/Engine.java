package core;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author jad nasser
 */
public class Engine {

    public Engine() {

    }

    //converts a given excel file to 2nf by creating a file that contains the queries of
    //the table and their data. When the created file is executed in sql server database it
    //will create the tables with its data and also with the tables connections (foreign keys)
    public String convert(String excel_file_path, String result_file_path) {
        try {
            //reading the data from the excel file
            ReadExcel readExcel = new ReadExcel(excel_file_path);
            ArrayList<ArrayList<String>> data = readExcel.getData();
            //checking if there is some problems in the data
            FirstNormalForm firstNormalForm = new FirstNormalForm(data);
            String checkValidityResult = firstNormalForm.checkValidity();
            if (!checkValidityResult.equals("no problem")) {
                return checkValidityResult;
            }
            //Converting the data to 1nf
            ArrayList<ArrayList<String>> dataIn1NF = firstNormalForm.convert();
            //convert to 2nf and write the tables and data queries, this will return a file and
            //that file can be executed in sql server database to create the relational database
            SecondNormalForm secondNormalForm = new SecondNormalForm(dataIn1NF);
            secondNormalForm.writeSQLServerQueries(result_file_path);
        } catch (IOException ex) {
            return ex.getMessage();
        }
        return "The file successfully created";
    }

}
