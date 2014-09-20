/*
 * Copyright (C) 2014 Nils Ryter
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import libraries.PackageClassList;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import ranking.Ranker;

/**
 *
 * @author Nils Ryter
 */
public class RankerOCR {

    //Show help on the CLI
    private static final HelpFormatter hf = new HelpFormatter();
    //Manage options of CLI
    private static final Options options = new Options();
    //Wide of the CLI screen
    private static final int width = 50;

    static {
        //<editor-fold defaultstate="collapsed" desc="Initialization">
        //Adding option
        Option o = new Option("", "");
        options.addOption("gui", false, "Launch a graphical user interface");
        options.addOption("indoc1", true,
                "Set the file name of the original document");
        options.getOption("indoc1").setRequired(true);
        options.addOption("indoc2", true,
                "Set the file name of the document to compare");
        options.getOption("indoc2").setRequired(true);
        options.addOption("ranker", true,
                "Set the ranker used for the comparison");
        options.addOption("help", false, "Show the help");
        options.addOption("outdoc", true,
                "Set the document where write the results as CSV");
        options.getOption("outdoc").setRequired(true);
        options.addOption("separator", true,
                "Set the delimiter char use in the CSV out file");
        //Help formater
        hf.setOptPrefix("-");
        //</editor-fold>
    }

    /**
     * Command line interface for RankerOCR.
     * <p>
     * <b>Command line input:</b>
     * <ul>
     * usage: java -jar Ranker-OCR [options]
     * </ul>
     * <b>Options list:</b>
     * <ul>
     * <li>-gui Launch a graphical user interface</li>
     * <li>-help Show the help</li>
     * <li>-indoc1 [arg] Set the file name of the original document</li>
     * <li>-indoc2 [arg] Set the file name of the document to compare</li>
     * <li>-ranker [arg] Set the ranker used for the comparison</li>
     * <li>-outdoc [arg] Set the document where write the results</li>
     * <li>-separator [arg] Set the delimiter char use in the CSV out file</li>
     * </ul>
     * <b>Return values are if error:</b>
     * <ul>
     * <li>(-1) The precision parameter is not a number.</li>
     * <li>(-2) The precision parameter is lower than 0.</li>
     * <li>(-3) The precision parameter is greater than 10.</li>
     * <li>(-11) The ranker name is wrong</li>
     * <li>(-21) The separator char is empty</li>
     * <li>(-22) The separator is not a char</li>
     * <li>(-31) File name doesn't exist</li>
     * <li>(-32) File name is not a file</li>
     * <li>(-33) Error when access to documents files</li>
     * <li>(-34) Output file can not be write</li>
     * <li>(-35) Output file can not be created</li>
     * <li>(-41) Error when parsing parameters</li>
     * <li>(-100) Internal error when creating the ranker. Please report a
     * bug</li>
     * <li>(-101) Internal error when get the ranker list. Please report a
     * bug.</li>
     * <li>(-102) Error when access to help file. Please report a bug.</li>
     * </ul>
     * <p>
     * @param args Argument array which can have the values from options list
     */
    public static void main(String[] args) {
        //Parse command line
        CommandLineParser parser = new GnuParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("help")) {
                showHelp(hf, options);
            } else if (cmd.hasOption("gui")) {
                display.DispalyRankerOCR.main(new String[]{});
            } else if (cmd.hasOption("indoc1")
                    && cmd.hasOption("indoc2")
                    && cmd.hasOption("outdoc")) {
                //<editor-fold defaultstate="collapsed" desc="Rank documents">
                //Prepare parameter
                Class ranker = evalRanker(cmd.getOptionValue("ranker",
                        "SimpleRanker"));
                char separator = evalSeparator(cmd.getOptionValue("separator",
                        "\t"));
                File f1 = evalInputFile(cmd.getOptionValue("indoc1", ""));
                File f2 = evalInputFile(cmd.getOptionValue("indoc2", ""));
                File f3 = evalOutputFile(cmd.getOptionValue("outdoc", ""),
                        separator);
                //Read file
                String s1 = readInputDocText(f1);
                String s2 = readInputDocText(f2);
                //Compare file
                double percent = rankDocuments(s1, s2, ranker);
                //Write result
                String[] s = {Double.toString(percent), ranker.getSimpleName(),
                    f1.getName(), f2.getName(), f1.getParent(), f2.getParent(),
                    new Date().toString()};
                writeOutpuDocCsv(f3, separator, s);
                //</editor-fold>
            } else {
                printFormated("java -jar Ranker-OCR [options]  please type "
                        + "-help for more info");
            }
        } catch (ParseException ex) {
            printFormated(ex.getLocalizedMessage());
            System.exit(-41);
        }
    }

    /**
     * Evaluate any input files parameters.
     * <p>
     * @param s File path as a string
     * @return File if possible. Otherwise, stop the program and return an error
     * code
     */
    private static File evalInputFile(String s) {
        File f = new File(s);
        if (!f.exists()) {
            printFormated("File not exist : " + s);
            System.exit(-31);
        } else if (!f.isFile()) {
            printFormated("Not a file path : " + s);
            System.exit(-32);
        }
        return f;
    }

    /**
     * Evaluate any output files parameters.
     * <p>
     * @param s File path as a string
     * @param c Separator charter, require if must to create a new file
     * @return File if possible. Otherwise, stop the program and return an error
     * code
     */
    private static File evalOutputFile(String s, char c) {
        File f = new File(s);
        if (!f.exists()) {
            try {
                f.createNewFile();
                //Write CSV title
                String[] t = {"Difference %", "Ranker name", "Original name",
                    "Comparative name", "Original path", "Comparative path",
                    "Date & Time"};
                writeOutpuDocCsv(f, c, t);
            } catch (IOException ex) {
                printFormated(ex.getLocalizedMessage());
                System.exit(-35);
            }
        }
        if (!f.canWrite()) {
            printFormated("Can not write : " + s);
            System.exit(-34);
        }
        return f;
    }

    /**
     * Evaluate the precision parameter.
     * <p>
     * @param s precision parameter as a string
     * @return Precision parameter as a integer if possible. Otherwise, stop the
     * program and return an error code
     */
    private static int evalPrecisionOption(String s) {
        try {
            int precision;
            precision = Integer.valueOf(s);
            if (precision < 0) {
                System.exit(-2);
            } else if (precision > 10) {
                System.exit(-3);
            }
            return precision;
        } catch (NumberFormatException ex) {
            printFormated(ex.getLocalizedMessage());
            System.exit(-1);
            return 0;
        }
    }

    /**
     * Evaluate the ranker parameter.
     * <p>
     * @param s ranker parameter as a string
     * @return Ranker class if possible. Otherwise, stop the program and return
     * an error code
     */
    private static Class evalRanker(String s) {
        try {
            return Class.forName("ranking." + s);
        } catch (ClassNotFoundException ex) {
            printFormated(ex.getLocalizedMessage());
            System.exit(-11);
            return null;
        }
    }

    /**
     * Evaluate the separator parameter.
     * <p>
     * @param s Separator parameter as a string
     * @return Separator char if possible. Otherwise, stop the program and
     * return an error code
     */
    private static char evalSeparator(String s) {
        if (s.isEmpty()) {
            System.exit(-21);
        }
        if (s.length() != 1) {
            System.exit(-22);
        }
        return s.charAt(0);
    }

    /**
     * Print a text formatted for the CLI interface on the CLI interface.
     * <p>
     * @param s String to print
     */
    private static void printFormated(String s) {
        System.out.println();
        hf.printWrapped(new PrintWriter(System.out, true), width, s);
        System.out.println();
    }

    /**
     * Compare the documents and rank them.
     * <p>
     * @param d1 Origin text
     * @param d2 Comparative text
     * @param ranker Class of the ranker to use
     * @return Return the percent of difference between documents
     */
    private static double rankDocuments(String d1, String d2, Class ranker) {
        try {
            Ranker instance = (Ranker) ranker.newInstance();
            return instance.compare(d1, d2);
        } catch (InstantiationException | IllegalAccessException ex) {
            printFormated(ex.getLocalizedMessage());
            System.exit(-100);
            return -1;
        }
    }

    /**
     * Extract a file content as a string.
     * <p>
     * @param f Document file
     * @return Text of the document
     */
    private static String readInputDocText(File f) {
        try (FileReader fr = new FileReader(f)) {
            StringBuilder s;
            try (BufferedReader b = new BufferedReader(fr)) {
                s = new StringBuilder();
                while (true) {
                    String line = b.readLine();
                    if (line == null) {
                        break;
                    } else {
                        s.append(line);
                        s.append("\n");
                    }
                }
            }
            return s.toString();
        } catch (IOException ex) {
            printFormated(ex.getLocalizedMessage());
            System.exit(-33);
            return null;
        }
    }

    /**
     * Show the help on CLI.
     * <p>
     * Help is from <br>
     * -The file help.txt in the JAR <br>
     * -The HelpFormatter <br>
     * -Options.
     */
    private static void showHelp(HelpFormatter hf, Options options) {
        try {
            //First, print the header of help and brief summary of the behaviour
            //<editor-fold defaultstate="collapsed" desc="Print summary">
            InputStream f = RankerOCR.class.getResourceAsStream(
                    "/resources/help.txt");
            InputStreamReader fr = new InputStreamReader(f);
            BufferedReader b = new BufferedReader(fr);
            StringBuilder s = new StringBuilder();
            while (true) {
                String line = b.readLine();
                if (line == null) {
                    break;
                } else {
                    s.append(line);
                    s.append("\n");
                }
            }
            printFormated(s.toString());
            //</editor-fold>
            //Second, print the options list
            hf.printHelp("java -jar Ranker-OCR [options]", options);
            //Finnaly print the rankers list
            //<editor-fold defaultstate="collapsed" desc="Print rankers">
            try {

                System.out.println("\nRankers list:");
                List<Class> lst = PackageClassList.getClasses("ranking");
                lst.remove(ranking.Ranker.class);
                lst.stream().forEach((c) -> {
                    if (!c.isInterface()) {
                        System.out.println(" -" + c.getSimpleName());
                    }
                });
            } catch (ClassNotFoundException | IOException ex) {
                printFormated(ex.getLocalizedMessage());
                System.exit(-101);
            }
            //</editor-fold>
        } catch (IOException ex) {
            printFormated(ex.getLocalizedMessage());
            System.exit(-102);
        }
    }

    /**
     * Write all the given text in a new line in a CSV file
     * <p>
     * @param f CSV file to write
     * @param c Separator charter
     * @param s Values to write
     */
    private static void writeOutpuDocCsv(File f, char c, String[] s) {
        FileWriter w = null;
        try {
            w = new FileWriter(f, true);
            for (String txt : s) {
                w.append(txt + c);
            }
            w.append("\n\r");
        } catch (IOException ex) {
            printFormated(ex.getLocalizedMessage());
            System.exit(-34);
        } finally {
            try {
                w.close();
            } catch (IOException ex) {
                printFormated(ex.getLocalizedMessage());
                System.exit(-34);
            }
        }
    }
}
